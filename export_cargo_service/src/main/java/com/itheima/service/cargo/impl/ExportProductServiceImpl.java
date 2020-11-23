package com.itheima.service.cargo.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.cargo.ExportProductDao;
import com.itheima.domain.cargo.ExportProduct;
import com.itheima.domain.cargo.ExportProductExample;
import com.itheima.service.cargo.ExportProductService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class ExportProductServiceImpl implements ExportProductService {

	@Autowired
	private ExportProductDao exportProductDao;

	@Override
	public List<ExportProduct> findAll(ExportProductExample example) {
		return exportProductDao.selectByExample(example);
	}

	@Override
	public List<ExportProduct> findByExportId(String id) {
		ExportProductExample exportProductExample = new ExportProductExample();
		exportProductExample.createCriteria().andExportIdEqualTo(id);
		return exportProductDao.selectByExample(exportProductExample);
	}
}
