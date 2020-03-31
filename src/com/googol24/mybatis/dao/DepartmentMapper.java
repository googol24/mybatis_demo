package com.googol24.mybatis.dao;

import com.googol24.mybatis.bean.Department;

public interface DepartmentMapper {
    public Department getDepartmentById(int id);

    // 增强版部门查询：查询部门的同时查询出部门下的所有的员工
    public Department getDepartmentByIdPlus(int id);

    public Department getDepartmentByStep(int id);
}
