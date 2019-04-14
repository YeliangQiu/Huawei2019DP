package com.huawei.Status;

public enum RunStatus {
	
	FINISHED(0),
	WAITING(1);
	private final int status;
	private RunStatus(int status) {
		this.status = status;
	}
	public int getStatus() {
		return status;
	}
	
}
