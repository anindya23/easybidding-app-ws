package com.easybidding.app.ws.ui.model.request;

import java.util.List;

public class JobPagingRequestModel {

	private AccountDetailRequestModel account;
	private String status;
	private List<String> sortBy;
	private int page;
	private int size;

	public AccountDetailRequestModel getAccount() {
		return account;
	}

	public void setAccount(AccountDetailRequestModel account) {
		this.account = account;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<String> getSortBy() {
		return sortBy;
	}

	public void setSortBy(List<String> sortBy) {
		this.sortBy = sortBy;
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

	@Override
	public String toString() {
		return "Job{" + "Status = '" + status + '\'' + ", Accounts = '" + account.getId() + ", "
				+ account.getAccountName() + ", " + ", Page = " + page + ", Size = " + size + '}';
	}

}
