package com.zcc.admin.server.dto;

import java.io.Serializable;
import java.util.List;

import com.zcc.admin.server.model.Notice;
import com.zcc.admin.server.model.User;

public class NoticeVO implements Serializable {

	private static final long serialVersionUID = 7363353918096951799L;

	private Notice notice;

	private List<User> users;

	public Notice getNotice() {
		return notice;
	}

	public void setNotice(Notice notice) {
		this.notice = notice;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}
}
