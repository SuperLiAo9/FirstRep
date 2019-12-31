package com.zw.admin.server.dao;

import com.zw.admin.server.model.Owner;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OwnerMapper {
    int deleteByPrimaryKey(Integer ownerId);

    int insert(Owner record);

    int insertSelective(Owner record);

    Owner selectByPrimaryKey(Integer ownerId);

    int updateByPrimaryKeySelective(Owner record);

    int updateByPrimaryKey(Owner record);
}