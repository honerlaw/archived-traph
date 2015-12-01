package org.traph.rs.game;

import org.traph.rs.game.ActionManager.ActionType;

public final class ActionHandler {
	
	private final ActionType type;
	
	private final Action action;
	
	private int initialDelay;
	
	private int postDelay;
	
	private boolean executed;
	
	private boolean cancelled;
	
	public ActionHandler(ActionType type, Action action) {
		this(type, 0, 0, action);
	}
	
	public ActionHandler(ActionType type, int initialDelay, Action action) {
		this(type, initialDelay, 0, action);
	}
	
	public ActionHandler(ActionType type, int initialDelay, int postDelay, Action action) {
		this.type = type;
		this.initialDelay = initialDelay;
		this.postDelay = postDelay;
		this.action = action;
	}
	
	public ActionType getType() {
		return type;
	}
	
	public int getInitialDelay() {
		return initialDelay;
	}
	
	public void setInitialDelay(int initialDelay) {
		this.initialDelay = initialDelay;
	}
	
	public int getPostDelay() {
		return postDelay;
	}
	
	public void setPostDelay(int postDelay) {
		this.postDelay = postDelay;
	}
	
	public Action getAction() {
		return action;
	}
	
	public boolean isExecuted() {
		return executed;
	}
	
	public void setExecuted(boolean executed) {
		this.executed = executed;
	}
	
	public boolean isCancelled() {
		return cancelled;
	}
	
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

}
