package com.itheima.dao.system;

import com.itheima.domain.system.Dept;
import java.util.List;

public interface DeptDao {

    //companyId的作用是为了做数据的隔离
    List<Dept> findAll(String companyId);

    void save(Dept dept);

    Dept findById(String id);

    void update(Dept dept);

    void delete(String id);

    List<Dept> findChildrenDept(String id);

}
