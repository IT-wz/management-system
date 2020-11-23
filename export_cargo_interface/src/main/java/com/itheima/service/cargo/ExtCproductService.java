package com.itheima.service.cargo;


import com.github.pagehelper.PageInfo;
import com.itheima.domain.cargo.ExtCproduct;
import com.itheima.domain.cargo.ExtCproductExample;

import java.util.List;

// 附件
public interface ExtCproductService {
    /**
     * 保存
     */
    void save(ExtCproduct extCproduct);

    /**
     * 更新
     */
    void update(ExtCproduct extCproduct);

    /**
     * 删除
     */
    void delete(String id);

    /**
     * 根据id查询
     */
    ExtCproduct findById(String id);

    /*
     *   查询所有
     * */
    List<ExtCproduct> findAll(ExtCproductExample example);

    /**
     * 分页查询
     */
    PageInfo findByPage(int pageNum, int pageSize, ExtCproductExample example);
}
