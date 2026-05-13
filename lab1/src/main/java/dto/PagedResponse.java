package dto;

import java.util.List;

public class PagedResponse<T> {
	
	private List<T> items;
	private int totalItems;
	private int page;
	private int size;

	public PagedResponse(List<T> items, int totalItems, int page, int size) {
		this.items = items;
		this.totalItems = totalItems;
		this.page = page;
		this.size = size;
	}

	public List<T> getItems() {
		return items;
	}

	public void setItems(List<T> items) {
		this.items = items;
	}

	public int getTotalItems() {
		return totalItems;
	}

	public void setTotalItems(int totalItems) {
		this.totalItems = totalItems;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}


}
