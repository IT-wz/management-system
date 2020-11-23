package com.itheima.service.cargo;

import com.github.pagehelper.PageInfo;
import com.itheima.domain.cargo.Export;
import com.itheima.domain.cargo.ExportExample;

public interface ExportService {

    Export findById(String id);

    void save(Export export);

    void update(Export export);

    void delete(String id);

    PageInfo findByPage(int pageNum, int pageSize, ExportExample example);

    void exportE(String id);

    void findExportResult(String id);
}
