package com.itheima.service.cargo;

import com.itheima.domain.cargo.ExportProduct;
import com.itheima.domain.cargo.ExportProductExample;

import java.util.List;


public interface ExportProductService {

	//根据条件查询
	List<ExportProduct> findAll(ExportProductExample example);

	//根据报运单id查询报运单下的货物
	List<ExportProduct> findByExportId(String id);
}
