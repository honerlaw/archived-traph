package org.traph.rs.model.path;

import java.util.Deque;
import java.util.LinkedList;

import org.traph.rs.model.Entity;
import org.traph.rs.model.Location;
import org.traph.util.LocationUtil;

public class PathQueue {
	
	private final Entity entity;
	
	private final Deque<PathPoint> points;
	
	public boolean run;
	
	public PathQueue(Entity entity) {
		this.entity = entity;
		this.points = new LinkedList<PathPoint>();
	}
	
	public PathQueue add(Location location) {
		if(points.size() == 0) {
			reset();
		}
		PathPoint last = points.peekLast();
		int deltaX = location.getX() - last.getX();
		int deltaY = location.getY() - last.getY();
		int max = Math.max(Math.abs(deltaX), Math.abs(deltaY));
		for (int i = 0; i < max; i++) {
			if (deltaX < 0) {
				deltaX++;
			} else if (deltaX > 0) {
				deltaX--;
			}
			if (deltaY < 0) {
				deltaY++;
			} else if (deltaY > 0) {
				deltaY--;
			}
			if (points.size() >= 100) {
				return this;
			}
			PathPoint tempLast = points.peekLast();
			int tempDeltaX = location.getX() - deltaX - tempLast.getX();
			int tempDeltaY = location.getY() - deltaY - tempLast.getY();
			int direction = LocationUtil.direction(tempDeltaX, tempDeltaY);
			if(direction > -1) {
				points.add(new PathPoint(location.getX() - deltaX, location.getY() - deltaY, direction));
			}
		}
		return this;
	}
	
	public PathPoint poll() {
		return points.poll();
	}
	
	public PathQueue finish() {
		points.removeFirst();
		return this;
	}
	
	public PathQueue reset() {
		setRun(false);
		points.clear();
		points.add(new PathPoint(entity.getRegion().getLocation(), -1));
		return this;
	}
	
	public PathQueue setRun(boolean run) {
		this.run = run;
		return this;
	}
	
	public boolean isRun() {
		return run;
	}

}
