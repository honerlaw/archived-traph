package org.traph.rs.net.update.segment.player;

import org.traph.rs.model.Entity;
import org.traph.rs.model.MovementDirection;
import org.traph.rs.model.player.Player;
import org.traph.rs.net.update.EntityUpdate.UpdateFlag;
import org.traph.rs.net.update.segment.SegmentMovement;
import org.traph.util.net.GameBuffer;

public class PlayerMovement extends SegmentMovement {

	public PlayerMovement(Entity entity) {
		super(entity);
	}

	@Override
	public void build(GameBuffer buffer, boolean local) {
		Player player = getPlayer();
		if(local && player.isSet(UpdateFlag.TELEPORT)) {
			player.getBlock(UpdateFlag.TELEPORT).build(buffer);
		} else {
			MovementDirection direction = player.getMovementDirection();
			if(direction.isWalking()) {
				buffer.putBit(true)
					.putBits(2, direction.isRunning() ? 2 : 1) // put whether we are running or walking
					.putBits(3, direction.getPrimary()); // put the first direction
				if(direction.isRunning()) {
					buffer.putBits(3, direction.getSecondary()); // if running put section direction
				}
				buffer.putBit(player.isAttributesUpdate()); // put attributes update
			} else {
				if(player.isAttributesUpdate()) {
					buffer.putBit(true).putBits(2, 0); // we are standing
				} else {
					buffer.putBit(false);
				}
			}
		}
	}

}
