package org.traph.rs.game;

@FunctionalInterface
public interface Action {
	
	public ActionHandler execute(ActionData data);

}
