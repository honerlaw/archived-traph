package org.traph.rs.model.item;

public final class Item {
	
	private final int id;
	
	private final int amount;
	
	private Item(int id, int amount) {
		if(id < 0) {
			id = 1;
		}
		if(amount < 1) {
			amount = 1;
		}
		this.id = id;
		this.amount = amount;
	}
	
	public static Item create(int id) {
		return new Item(id, 1);
	}
	
	public static Item create(int id, int amount) {
		return new Item(id, amount);
	}
	
	public Item add(int amount) {
		long total = this.getAmount() + amount;
		if(total > Integer.MAX_VALUE || total < 1) {
			return null;
		}
		return new Item(getID(), (int) total);
	}
	
	public Item remove(int amount) {
		return add(-amount);
	}
	
	public int getID() {
		return id;
	}
	
	public int getAmount() {
		return amount;
	}
	
	public boolean isStackable() {
		return false;
	}

}
