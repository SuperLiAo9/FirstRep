package com.zcc.admin.server.service;

import com.zcc.admin.server.model.Permission;

public interface PermissionService {

	void save(Permission permission);

	void update(Permission permission);

	void delete(Long id);
}
