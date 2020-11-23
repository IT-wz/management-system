package com.itheima.dao.company;

import com.itheima.domain.company.Company;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CompanyDao {

    List<Company> findAll();

    void save(Company company);

    Company findById(String id);

    void update(Company company);

    void delete(String id);

    Long findCount();

    List<Company> findList(@Param("startIndex") int startIndex, @Param("pageSize") Integer pageSize);
}
