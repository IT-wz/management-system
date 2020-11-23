package com.itheima.web.controller.cargo;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.itheima.domain.cargo.*;
import com.itheima.service.cargo.ContractService;
import com.itheima.service.cargo.ExportProductService;
import com.itheima.service.cargo.ExportService;
import com.itheima.utils.DownloadUtil;
import com.itheima.web.controller.BaseController;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/cargo/export")
public class ExportController extends BaseController {

    @Reference
    private ContractService contractService;

    @Reference
    private ExportService exportService;

    @Reference
    private ExportProductService exportProductService;

    @RequestMapping(value = "/contractList", name = "已提交合同列表查询")
    public String contractList(
            @RequestParam(value = "page", defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        ContractExample contractExample = new ContractExample();
        ContractExample.Criteria criteria = contractExample.createCriteria();
        criteria.andCompanyIdEqualTo(getCompanyId());
        criteria.andStateEqualTo(1);
        contractExample.setOrderByClause("create_time desc");
        PageInfo<Contract> pageInfo = contractService.findByPage(pageNum, pageSize, contractExample);
        request.setAttribute("page", pageInfo);

        return "/cargo/export/export-contractList";
    }


    @RequestMapping(value = "/list", name = "报运单列表查询")
    public String list(
            @RequestParam(value = "page", defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        ExportExample exportExample = new ExportExample();
        exportExample.createCriteria().andCompanyIdEqualTo(getCompanyId());

        PageInfo pageInfo = exportService.findByPage(pageNum, pageSize, exportExample);
        request.setAttribute("page", pageInfo);

        return "/cargo/export/export-list";
    }


    //String[] id
    //String   1,2,3
    @RequestMapping(value = "/toExport", name = "跳转报运单新增页面")
    public String toAdd(String id) {

        //回显合同id的集合
        request.setAttribute("id", id);
        return "/cargo/export/export-toExport";
    }


    @RequestMapping(value = "/toUpdate", name = "跳转报运单修改页面")
    public String toUpdate(String id) {
        //1 根据报运单id查询报运单信息
        Export export = exportService.findById(id);
        request.setAttribute("export", export);

        //2 根据报运单id查询报运单下的货物信息.
        //null.   null+
        List<ExportProduct> eps = exportProductService.findByExportId(id);
        request.setAttribute("eps", eps);

        return "/cargo/export/export-update";
    }

    @RequestMapping(value = "/edit", name = "报运单新增或修改")
    public String edit(Export export) {
        if (StringUtils.isEmpty(export.getId())) {
            //1 设置id
            export.setId(UUID.randomUUID().toString());

            //2. 设置企业信息
            export.setCompanyId(getCompanyId());
            export.setCompanyName(getCompanyName());

            //3. 草稿和制单时间
            export.setState(0);
            export.setInputDate(new Date());

            //4. 调用service保存操作
            exportService.save(export);
        } else {
            exportService.update(export);

        }
        return "redirect:/cargo/export/list.do";
    }

    @RequestMapping(value = "/exportE", name = "海关电子报运")
    public String exportE(String id) {
        exportService.exportE(id);
        return "redirect:/cargo/export/list.do";
    }

    @RequestMapping(value = "/findExportResult", name = "查询海关电子报运结果")
    public String findExportResult(String id) {
        exportService.findExportResult(id);
        return "redirect:/cargo/export/list.do";
    }

    @RequestMapping(value = "/exportPdf", name = "导出Pdf")
    public void exportPdf(String id) throws JRException, IOException {
        //1. 读取模板
        String realPath = session.getServletContext().getRealPath("/jasper/export.jasper");

        //2. 准备数据map
        //2-1)准备报运单(报运单id)
        Export export = exportService.findById(id);
        Map<String, Object> map = BeanUtil.beanToMap(export);
        //2-2) 准备报运单下货物(报运单id)
        ExportProductExample exportProductExample = new ExportProductExample();
        exportProductExample.createCriteria().andExportIdEqualTo(id);
        List<ExportProduct> list = exportProductService.findAll(exportProductExample);

        //3. 数据填充
        JasperPrint jasperPrint = JasperFillManager.fillReport(realPath, map, new JRBeanCollectionDataSource(list));

        //4. 导出下载
        //JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint, byteArrayOutputStream);
        DownloadUtil.download(byteArrayOutputStream, response, "报运单.pdf");
    }


    @RequestMapping(value = "/exportPdf1", name = "导出Pdf")
    public void exportPdf1(String id) throws JRException, IOException {
        //1. 读取模板
        String realPath = session.getServletContext().getRealPath("/jasper/demo1.jasper");

        //2. 数据填充
        //参数1: 模板文件位置  参数2: Map类型数据   参数3: List类型数据
        JasperPrint jasperPrint = JasperFillManager.fillReport(realPath, new HashMap<>(), new JREmptyDataSource());

        //3. 导出
        //参数1:模板+数据     参数2:输出流
        JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
    }


    @RequestMapping(value = "/exportPdf2", name = "导出Pdf")
    public void exportPdf2(String id) throws JRException, IOException {
        //1. 读取模板
        String realPath = session.getServletContext().getRealPath("/jasper/demo2.jasper");

        //2. 准备数据map
        Map<String, Object> map = new HashMap<>();
        map.put("username", "安妮");
        map.put("age", "18");
        map.put("address", "家里蹲");
        map.put("company", "腾讯");

        //3. 数据填充
        //参数1: 模板文件位置  参数2: Map类型数据   参数3: List类型数据
        JasperPrint jasperPrint = JasperFillManager.fillReport(realPath, map, new JREmptyDataSource());

        //4. 导出
        //参数1:模板+数据     参数2:输出流
        JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
    }


    @RequestMapping(value = "/exportPdf3", name = "导出Pdf")
    public void exportPdf3(String id) throws JRException, IOException {
        //1. 读取模板
        String realPath = session.getServletContext().getRealPath("/jasper/demo3.jasper");

        //2. 准备数据map
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("userName", "用户" + i);
            map.put("email", i + "@export.com");
            map.put("deptName", "部门" + i);
            map.put("companyName", "传智播客");

            list.add(map);
        }

        //3. 数据填充
        //参数1: 模板文件位置  参数2: Map类型数据   参数3: List类型数据
        JasperPrint jasperPrint = JasperFillManager.fillReport(realPath, new HashMap<>(), new JRBeanCollectionDataSource(list));

        //4. 导出
        //参数1:模板+数据     参数2:输出流
        JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
    }


    @RequestMapping(value = "/exportPdf4", name = "导出Pdf")
    public void exportPdf4(String id) throws JRException, IOException {
        //1. 读取模板
        String realPath = session.getServletContext().getRealPath("/jasper/demo4.jasper");

        //2. 准备数据map
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("userName", "用户" + i);
            map.put("email", i + "@export.com");
            map.put("deptName", "部门" + i);
            map.put("companyName", "传智播客");

            list.add(map);
        }


        for (int i = 1; i <= 2; i++) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("userName", "用户" + i);
            map.put("email", i + "@export.com");
            map.put("deptName", "部门" + i);
            map.put("companyName", "腾讯");

            list.add(map);
        }


        for (int i = 1; i <= 4; i++) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("userName", "用户" + i);
            map.put("email", i + "@export.com");
            map.put("deptName", "部门" + i);
            map.put("companyName", "传智播客");

            list.add(map);
        }


        //3. 数据填充
        //参数1: 模板文件位置  参数2: Map类型数据   参数3: List类型数据
        JasperPrint jasperPrint = JasperFillManager.fillReport(realPath, new HashMap<>(), new JRBeanCollectionDataSource(list));

        //4. 导出
        //参数1:模板+数据     参数2:输出流
        JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
    }


    @RequestMapping(value = "/exportPdf5", name = "导出Pdf")
    public void exportPdf5(String id) throws JRException, IOException {
        //1. 读取模板
        String realPath = session.getServletContext().getRealPath("/jasper/demo5.jasper");

        //2. 准备数据map
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("name", i + "号商铺");
            map.put("value", i);
            list.add(map);
        }

        //3. 数据填充
        //参数1: 模板文件位置  参数2: Map类型数据   参数3: List类型数据
        JasperPrint jasperPrint = JasperFillManager.fillReport(realPath, new HashMap<>(), new JRBeanCollectionDataSource(list));

        //4. 导出
        //参数1:模板+数据     参数2:输出流
        JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
    }
}
