package org.traph.rs.model.item;

/**
 * Represents an interface that items can be stored in (Equipment / Inventory / etc)
 * 
 */
public abstract class ItemContainer {
	
	
	private final Item[] items;
	
	private boolean stackable;
	
	public ItemContainer(int size) {
		this.items = new Item[size];
	}
	
	public Item get(int index) {
		if(index < 0 || index >= items.length) {
			throw new IllegalArgumentException("Invalid index in storage container.");
		}
		return items[index];
	}
	
	public void set(int index, Item item) {
		if(index < 0 || index >= items.length) {
			throw new IllegalArgumentException("Invalid index in storage container.");
		}
		items[index] = item;
	}
	
	/**
	 * Get the index for the first occurrence of this item
	 * 
	 * @param item The item we are searching for
	 * 
	 * @return The first occurrence index of the item
	 */
	public int getIndexForItem(Item item) {
		for(int i = 0; i < items.length; ++i) {
			if(items[i] == null) {
				continue;
			}
			if(items[i].getID() == item.getID()) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Add a new item at the given index. Adds all of the item's amount or none
	 * 
	 * @param index The index to add the item to
	 * @param item The item to add to the container
	 * 
	 * @return True if the item and its entire amount was added, false otherwise
	 */
	public boolean add(int index, Item item) {
		
		//if the interface is stackable
		if(isStackable() || item.isStackable()) {
			
			// if the index is empty add it
			if(items[index] == null) {
				items[index] = item;
				return true;
			} else {
				// if the index contains the id of the item we add it
				if(items[index].getID() == item.getID()) {
					
					// make sure we can add the given amount
					Item temp = items[index].add(item.getAmount());
					if(temp != null) {
						items[index] = temp;
						return true;
					}
				}
			}
		} else {
			if(items[index] == null) {
				items[index] = item;
				return true;
			}			
		}
		return false;
	}
	
	/**
	 * Removes an item from the given index. Removes all of the amount or none
	 * 
	 * @param index The index to remove the item from
	 * @param item The item we are removing
	 * 
	 * @return True if we removed the given item, false otherwise
	 */
	public boolean remove(int index, Item item) {
		if(isStackable() || item.isStackable()) {
			
			// make sure there is an item to remove from the index
			if(items[index] != null) {
				
				// make sure the item we are trying to remove match
				if(items[index].getID() == item.getID()) {
					
					// if the amounts are the same then we remove all of the item
					if(items[index].getAmount() == item.getAmount()) {
						items[index] = null;
						return true;
					} else {
						
						// otherwise try to remove the specified amount
						Item temp = items[index].remove(item.getAmount());
						if(temp != null) {
							items[index] = temp;
							return true;
						}
					}
				}
			}
		} else {
			
			// if the items exists
			if(items[index] != null) {
				
				// and the items match each other remove it
				if(items[index].getID() == item.getID()) {
					items[index] = null;
					return true;
				}
			}
		}		
		return false;
	}
	
	/**
	 * Check whether the item exists in the container
	 * 
	 * @param item The item to check for
	 * 
	 * @return True if the item exists, false otherwise
	 */
	public boolean contains(Item item) {
		 return getIndexForItem(item) != -1;
	}
	
	/**
	 * @return True if stackable, false otherwise
	 */
	public boolean isStackable() {
		return stackable;
	}

}
