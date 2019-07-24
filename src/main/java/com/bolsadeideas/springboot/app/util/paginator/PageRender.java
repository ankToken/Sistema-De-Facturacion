package com.bolsadeideas.springboot.app.util.paginator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;

public class PageRender<T> {

	private String url;

	private Page<T> page;

	private int totalPage;

	private int elementsPerPage;

	private int actualPage;

	private List<PageItem> pages;

	public PageRender(String url, Page<T> page) {
		super();
		this.url = url;
		this.page = page;
		this.pages = new ArrayList<PageItem>();

		elementsPerPage = page.getSize();
		totalPage = page.getTotalPages();
		actualPage = page.getNumber() + 1;

		int from, to;

		if (totalPage <= elementsPerPage) {
			from = 1;
			to = totalPage;
		} else {
			if (actualPage <= elementsPerPage / 2) {
				from = 1;
				to = elementsPerPage;
			} else if (actualPage >= totalPage - elementsPerPage / 2) {
				from = totalPage - elementsPerPage + 1;
				to = elementsPerPage;
			} else {
				from = actualPage - elementsPerPage / 2;
				to = elementsPerPage;
			}
		}
		
		for(int i = 0; i < to; i++) {
			pages.add(new PageItem(from + i, actualPage == from + i));
		}
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Page<T> getPage() {
		return page;
	}

	public void setPage(Page<T> page) {
		this.page = page;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getElementsPerPage() {
		return elementsPerPage;
	}

	public void setElementsPerPage(int elementsPerPage) {
		this.elementsPerPage = elementsPerPage;
	}

	public int getActualPage() {
		return actualPage;
	}

	public void setActualPage(int actualPage) {
		this.actualPage = actualPage;
	}

	public List<PageItem> getPages() {
		return pages;
	}

	public void setPages(List<PageItem> pages) {
		this.pages = pages;
	}
	
	public boolean isFirst() {
		return page.isFirst();
	}
	
	public boolean isLast() {
		return page.isLast();
	}
	
	public boolean isHasNext() {
		return page.hasNext();
	}
	
	public boolean isHasPrevious() {
		return page.hasPrevious();
	}
}