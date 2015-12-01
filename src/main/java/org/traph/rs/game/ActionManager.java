package org.traph.rs.game;

import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import org.traph.rs.World;
import org.traph.rs.net.Client;

/**
 * We maintain two lists of the action that is suppose to be executed
 * 
 * The first list (this one) is used so we can cancel actions / etc
 * 
 * Whenever we add an action to the world we add the action to a list in here
 * 
 * We then register the ActionManager with the World however many times we need
 * to so that we can
 * 
 * @author Derek
 *
 */
public class ActionManager {
	
	public static enum ActionType {
		BASIC, WALK
	}
	
	private final Client client;
	private final World world;
	
	private final Queue<ActionHandler> actions;
	
	public ActionManager(Client client, World world) {
		this.client = client;
		this.world = world;
		this.actions = new LinkedBlockingQueue<ActionHandler>();	
	}
	
	public ActionManager cancel(ActionType type) {
		for(Iterator<ActionHandler> it = actions.iterator(); it.hasNext(); ) {
			ActionHandler handler = it.next();
			if(handler.getType() == type) {
				handler.setCancelled(true);
			}
		}
		return this;
	}
	
	public ActionManager cancel() {
		for(Iterator<ActionHandler> it = actions.iterator(); it.hasNext(); ) {
			it.next().setCancelled(true);
		}
		return this;
	}
	
	public ActionManager remove(ActionType type) {
		for(Iterator<ActionHandler> it = actions.iterator(); it.hasNext(); ) {
			ActionHandler handler = it.next();
			if(handler.getType() == type) {
				it.remove();
			}
		}
		return this;
	}
	
	public ActionManager empty() {
		for(Iterator<ActionHandler> it = actions.iterator(); it.hasNext(); ) {
			it.remove();
		}
		return this;
	}
	
	public boolean add(ActionHandler actionHandler) {
		// if the action already exists then we don't add it
		for(Iterator<ActionHandler> it = actions.iterator(); it.hasNext(); ) {
			if(it.next().getType() == actionHandler.getType()) {
				return false;
			}
		}
		
		// add the action and register it with the world
		world.getActionManagers().add(this);
		actions.add(actionHandler);
		return true;
	}
	
	public void execute() {
		ActionHandler handler = actions.peek();
		if(handler == null) {
			return;
		}
		if(handler.isExecuted()) {
			if(handler.getPostDelay() <= 0) {
				actions.remove(handler);
			} else {
				handler.setPostDelay(handler.getPostDelay() - 1);
			}
		} else {
			if(handler.getInitialDelay() <= 0) {
				if(!handler.isCancelled()) {
					ActionHandler next = handler.getAction().execute(new ActionData(world, client));
					handler.setExecuted(true);
					if(next != null) {
						add(next);
					}
				}
				if(handler.getPostDelay() <= 0) {
					actions.remove(handler);
				}
			} else {
				handler.setInitialDelay(handler.getInitialDelay() - 1);
			}
		}
	}

}
