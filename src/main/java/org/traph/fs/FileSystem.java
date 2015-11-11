package org.traph.fs;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.traph.fs.archive.Archive;
import org.traph.fs.archive.ArchiveFile;
import org.traph.util.Constant;

import io.netty.buffer.ByteBuf;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;

public final class FileSystem {

	/**
	 * The map of index file id to index files
	 */
	private final Map<Integer, Index[]> indices = new HashMap<Integer, Index[]>();
	
	/**
	 * The CRC table based on the index files
	 */
	private CrcTable crcTable;
	
	/**
	 * Initializes a new file system and loads all of the files in the cache into memory
	 * 
	 * @param vertx The vertx to load the system for
	 * @param path The path where the files reside
	 * 
	 * @throws FileNotFoundException When no files are found in the given path
	 */
	public FileSystem(Vertx vertx, String path) throws FileNotFoundException {
		Path base = Paths.get(path);
		Path resources = base.resolve("main_file_cache.dat");
		if(Files.exists(resources) && !Files.isDirectory(resources)) {
			Buffer dataFile = vertx.fileSystem().readFileBlocking(resources.toString());
			for(int indexFileId = 0; indexFileId < Constant.FileSystem.MAX_INDEX_FILES; ++indexFileId) {
				Path idx = base.resolve("main_file_cache.idx" + indexFileId);
				if(Files.exists(idx) && !Files.isDirectory(idx)) {
					Buffer indexFile = vertx.fileSystem().readFileBlocking(idx.toString());
					indices.put(indexFileId, getIndices(indexFileId, indexFile, dataFile));						
				}
			}
			if(indices.size() > 0) {		
				crcTable = CrcTable.create(indices);
			} else {
				throw new FileNotFoundException("No index files found.");
			}
		} else {
			throw new FileNotFoundException("No data file found.");
		}
	}
	
	/**
	 * Read the Index files and their associated archives
	 * 
	 * @param index The index file id
	 * @param buffer The Index file buffer
	 * @param data The Data file buffer
	 * @return A map of index files and their associated data
	 * @throws IOException 
	 */
	private Index[] getIndices(int indexFileId, Buffer indexFile, Buffer dataFile) {	
		int indexCount = indexFile.length() / Constant.FileSystem.INDEX_BLOCK_SIZE;
		Index[] indices = new Index[indexCount];
		for(int resourceId = 0; resourceId < indexCount; ++resourceId) {
			int start = resourceId * Constant.FileSystem.INDEX_BLOCK_SIZE;
			int end = start + Constant.FileSystem.INDEX_BLOCK_SIZE;
			if(start >= 0 && end <= indexFile.length()) {
				byte[] buffer = indexFile.getBytes(start, end);
				int size = (buffer[0] & 0xFF) << 16 | (buffer[1] & 0xFF) << 8 | buffer[2] & 0xFF;
				int blockPosition = (buffer[3] & 0xFF) << 16 | (buffer[4] & 0xFF) << 8 | buffer[5] & 0xFF;
				indices[resourceId] = new Index(size, blockPosition, new Archive(ArchiveFile.file(indexFileId, size, blockPosition, resourceId, dataFile)));
			}
		}
		return indices;
	}
	
	/**
	 * Retreive a resource based on the index file id and resource id
	 * 
	 * @param indexFileId The index file's id
	 * @param resourceId The resource id
	 * 
	 * @return A buffer containing the archive (resource) data
	 */
	public Buffer getResource(int indexFileId, int resourceId) {
		return indices.get(indexFileId)[resourceId].getArchive().getArchiveFile().getBuffer();
	}
	
	/**
	 * Get a resource based on the request path
	 * 
	 * @param path The path name of the resource
	 * 
	 * @return A buffer containing the archive (resource) data
	 */
	public Buffer getResource(String path) {
		if (path.startsWith("/crc")) {
			return crcTable.getBuffer();
		} else if (path.startsWith("/title")) {
			return indices.get(0)[1].getArchive().getArchiveFile().getBuffer();
		} else if (path.startsWith("/config")) {
			return indices.get(0)[2].getArchive().getArchiveFile().getBuffer();
		} else if (path.startsWith("/interface")) {
			return indices.get(0)[3].getArchive().getArchiveFile().getBuffer();
		} else if (path.startsWith("/media")) {
			return indices.get(0)[4].getArchive().getArchiveFile().getBuffer();
		} else if (path.startsWith("/versionlist")) {
			return indices.get(0)[5].getArchive().getArchiveFile().getBuffer();
		} else if (path.startsWith("/textures")) {
			return indices.get(0)[6].getArchive().getArchiveFile().getBuffer();
		} else if (path.startsWith("/wordenc")) {
			return indices.get(0)[7].getArchive().getArchiveFile().getBuffer();
		} else if (path.startsWith("/sounds")) {
			return indices.get(0)[8].getArchive().getArchiveFile().getBuffer();
		}
		return null;
	}
	
	/**
	 * Gets the correct response for a given http server request
	 * 
	 * @param req The request to handle
	 * 
	 * @return The buffer containing the response data
	 */
	public Buffer getHttpResponse(HttpServerRequest req) {
		// get the resource based on the path
		Buffer resource = getResource(req.path());
		
		// build the HTTP header string
		String headers = new StringBuilder()
			.append("HTTP/1.1 200 OK\n")
			.append("Server: JAGeX/3.1\n")
			.append("Content-Type: application/octect-stream\n")
			.append("Connection: close\n")
			.append("Content-Length: " + resource.length() + "\n\n")
			.toString();
		
		// return the buffer containing the headers / resource data
		return Buffer.buffer().appendString(headers).appendBuffer(resource);
	}
	
	public Buffer[] getOndemandResponse(Buffer buf) {
		ByteBuf payload = buf.getByteBuf();
		List<Buffer> responses = new ArrayList<Buffer>();
		int requests = payload.readableBytes() / Constant.FileSystem.ONDEMAND_REQUEST_SIZE;
		for(int i = 0; i < requests; ++i) { 
			int position = i * Constant.FileSystem.ONDEMAND_REQUEST_SIZE;
			int indexFileId = (payload.getByte(position) & 0xFF) + 1;
			int resourceId = payload.getShort(position + 1) & 0xFFFF;
			Buffer resource = getResource(indexFileId, resourceId);
			ByteBuf buffer = resource.getByteBuf();
			for(int chunk = 0; buffer.readableBytes() > 0; ++chunk) {
				int chunkSize = Math.min(buffer.readableBytes(), Constant.FileSystem.ONDEMAND_CHUNK_SIZE);
				byte[] data = new byte[chunkSize];
				buffer.readBytes(data, 0, chunkSize);
				Buffer resp = Buffer.buffer()
					.appendByte((byte) (indexFileId - 1))
					.appendShort((short) resourceId)
					.appendShort((short) resource.length())
					.appendByte((byte) chunk)
					.appendBytes(data);
				responses.add(resp);
			}
		}
		return responses.toArray(new Buffer[] { });
	}


}
