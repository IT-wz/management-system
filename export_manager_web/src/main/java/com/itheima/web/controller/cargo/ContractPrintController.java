package com.itheima.web.controller.cargo;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.service.cargo.ContractService;
import com.itheima.utils.DownloadUtil;
import com.itheima.vo.ContractProductVo;
import com.itheima.web.controller.BaseController;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

@Controller
@RequestMapping("/cargo/contract")
public class ContractPrintController extends BaseController {

    @Reference
    private ContractService contractService;

    @RequestMapping(value = "/print", name = "跳转出货表导出页面")
    public String print() {
        return "/cargo/print/contract-print";
    }

    @RequestMapping(value = "/printExcel", name = "出货表导出")
    public void printExcel(String inputDate) throws IOException {
        //1. 查询数据库,得到一个列表
        List<ContractProductVo> list = contractService.findContractProductVo(inputDate, getCompanyId());
        System.out.println(list);

        //2. 将列表转换成execl表格( POI操作 )
        //2-1) 创建WorkBook
        Workbook workbook = new XSSFWorkbook();

        //2-2) 创建Sheet
        Sheet sheet = workbook.createSheet();
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 1, 8));//合并单元格
        sheet.setColumnWidth(1, 15 * 256);
        sheet.setColumnWidth(2, 15 * 256);
        sheet.setColumnWidth(3, 15 * 256);
        sheet.setColumnWidth(4, 15 * 256);
        sheet.setColumnWidth(5, 15 * 256);
        sheet.setColumnWidth(6, 15 * 256);
        sheet.setColumnWidth(7, 15 * 256);
        sheet.setColumnWidth(8, 15 * 256);

        //2-3) 创建第0行
        Row row0 = sheet.createRow(0);
        for (int i = 1; i < 9; i++) {
            Cell cell = row0.createCell(i);
            cell.setCellStyle(bigTitleStyle(workbook));
        }
        String title = inputDate.replaceAll("-0", "年").replaceAll("-", "年") + "月份出货表";
        row0.getCell(1).setCellValue(title);

        //2-4) 创建第1行
        Row row1 = sheet.createRow(1);
        for (int i = 1; i < 9; i++) {
            Cell cell = row1.createCell(i);
            cell.setCellStyle(littleTitleStyle(workbook));
        }
        row1.getCell(1).setCellValue("客户");
        row1.getCell(2).setCellValue("合同号");
        row1.getCell(3).setCellValue("货号");
        row1.getCell(4).setCellValue("数量");
        row1.getCell(5).setCellValue("工厂");
        row1.getCell(6).setCellValue("工厂交期");
        row1.getCell(7).setCellValue("船期");
        row1.getCell(8).setCellValue("贸易条款");

        //2-5) 遍历创建剩余行
        int n = 2;
        for (ContractProductVo contractProductVo : list) {
            Row rown = sheet.createRow(n++);
            for (int i = 1; i < 9; i++) {
                Cell cell = rown.createCell(i);
                cell.setCellStyle(textStyle(workbook));
            }
            //2-6) 开始填充数据
            rown.getCell(1).setCellValue(contractProductVo.getCustomName());
            rown.getCell(2).setCellValue(contractProductVo.getContractNo());
            rown.getCell(3).setCellValue(contractProductVo.getProductNo());
            rown.getCell(4).setCellValue(contractProductVo.getCnumber());
            rown.getCell(5).setCellValue(contractProductVo.getFactoryName());
            rown.getCell(6).setCellValue(new SimpleDateFormat("yyyy-MM-dd").format(contractProductVo.getDeliveryPeriod()));
            rown.getCell(7).setCellValue(new SimpleDateFormat("yyyy-MM-dd").format(contractProductVo.getShipTime()));
            rown.getCell(8).setCellValue(contractProductVo.getTradeTerms());
        }


        //3. 文件下载(工具类)
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        workbook.write(byteArrayOutputStream);//将工作簿写入到输出流
        DownloadUtil.download(byteArrayOutputStream, response, "出货表.xlsx");
    }

    @RequestMapping(value = "/printExcelWithTemplate", name = "出货表导出--使用模板")
    public void printExcelWithTemplate(String inputDate) throws IOException {
        //1. 查询数据库,得到一个列表
        List<ContractProductVo> list = contractService.findContractProductVo(inputDate, getCompanyId());
        System.out.println(list);

        //2. 将列表转换成execl表格
        //2-1) 读取一个工作簿
        String filePath = session.getServletContext().getRealPath("/make/xlsprint/tOUTPRODUCT.xlsx");
        Workbook workbook = new XSSFWorkbook(new FileInputStream(new File(filePath)));

        //2-2) 读取工作表
        Sheet sheet = workbook.getSheetAt(0);

        //2-3) 处理第1行的文字
        String title = inputDate.replaceAll("-0", "年").replaceAll("-", "年") + "月份出货表";
        sheet.getRow(0).getCell(1).setCellValue(title);

        //2-4) 先读取到第3行的每个单元格的样式,存起来备用
        Row row2 = sheet.getRow(2);
        CellStyle cellStyle1 = row2.getCell(1).getCellStyle();
        CellStyle cellStyle2 = row2.getCell(2).getCellStyle();
        CellStyle cellStyle3 = row2.getCell(3).getCellStyle();
        CellStyle cellStyle4 = row2.getCell(4).getCellStyle();
        CellStyle cellStyle5 = row2.getCell(5).getCellStyle();
        CellStyle cellStyle6 = row2.getCell(6).getCellStyle();
        CellStyle cellStyle7 = row2.getCell(7).getCellStyle();
        CellStyle cellStyle8 = row2.getCell(8).getCellStyle();


        //2-5) 循环数据,然后设置样式(从上一步读到的样式取)
        int n = 2;
        for (ContractProductVo contractProductVo : list) {
            Row rown = sheet.createRow(n++);
            for (int i = 1; i < 9; i++) {
                Cell cell = rown.createCell(i);
            }
            //2-6 设置单元格的样式
            rown.getCell(1).setCellStyle(cellStyle1);
            rown.getCell(2).setCellStyle(cellStyle2);
            rown.getCell(3).setCellStyle(cellStyle3);
            rown.getCell(4).setCellStyle(cellStyle4);
            rown.getCell(5).setCellStyle(cellStyle5);
            rown.getCell(6).setCellStyle(cellStyle6);
            rown.getCell(7).setCellStyle(cellStyle7);
            rown.getCell(8).setCellStyle(cellStyle8);

            //2-7) 开始填充数据
            rown.getCell(1).setCellValue(contractProductVo.getCustomName());
            rown.getCell(2).setCellValue(contractProductVo.getContractNo());
            rown.getCell(3).setCellValue(contractProductVo.getProductNo());
            rown.getCell(4).setCellValue(contractProductVo.getCnumber());
            rown.getCell(5).setCellValue(contractProductVo.getFactoryName());
            rown.getCell(6).setCellValue(new SimpleDateFormat("yyyy-MM-dd").format(contractProductVo.getDeliveryPeriod()));
            rown.getCell(7).setCellValue(new SimpleDateFormat("yyyy-MM-dd").format(contractProductVo.getShipTime()));
            rown.getCell(8).setCellValue(contractProductVo.getTradeTerms());
        }


        //3. 文件下载(工具类)
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        workbook.write(byteArrayOutputStream);//将工作簿写入到输出流
        DownloadUtil.download(byteArrayOutputStream, response, "出货表.xlsx");
    }



    @RequestMapping(value = "/printExcelMillion", name = "出货表导出--百万数据")
    public void printExcelMillion(String inputDate) throws IOException {
        //1. 查询数据库,得到一个列表
        List<ContractProductVo> list = contractService.findContractProductVo(inputDate, getCompanyId());
        System.out.println(list);

        //2. 将列表转换成execl表格( POI操作 )
        //2-1) 创建WorkBook
        Workbook workbook = new SXSSFWorkbook();

        //2-2) 创建Sheet
        Sheet sheet = workbook.createSheet();
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 1, 8));//合并单元格
        sheet.setColumnWidth(1, 15 * 256);
        sheet.setColumnWidth(2, 15 * 256);
        sheet.setColumnWidth(3, 15 * 256);
        sheet.setColumnWidth(4, 15 * 256);
        sheet.setColumnWidth(5, 15 * 256);
        sheet.setColumnWidth(6, 15 * 256);
        sheet.setColumnWidth(7, 15 * 256);
        sheet.setColumnWidth(8, 15 * 256);

        //2-3) 创建第0行
        Row row0 = sheet.createRow(0);
        for (int i = 1; i < 9; i++) {
            Cell cell = row0.createCell(i);
        }
        String title = inputDate.replaceAll("-0", "年").replaceAll("-", "年") + "月份出货表";
        row0.getCell(1).setCellValue(title);

        //2-4) 创建第1行
        Row row1 = sheet.createRow(1);
        for (int i = 1; i < 9; i++) {
            Cell cell = row1.createCell(i);
        }
        row1.getCell(1).setCellValue("客户");
        row1.getCell(2).setCellValue("合同号");
        row1.getCell(3).setCellValue("货号");
        row1.getCell(4).setCellValue("数量");
        row1.getCell(5).setCellValue("工厂");
        row1.getCell(6).setCellValue("工厂交期");
        row1.getCell(7).setCellValue("船期");
        row1.getCell(8).setCellValue("贸易条款");

        //2-5) 遍历创建剩余行
        int n = 2;
        for (ContractProductVo contractProductVo : list) {
            for (int x = 0; x < 8000; x++) {
                Row rown = sheet.createRow(n++);
                for (int i = 1; i < 9; i++) {
                    Cell cell = rown.createCell(i);
                }
                //2-6) 开始填充数据
                rown.getCell(1).setCellValue(contractProductVo.getCustomName());
                rown.getCell(2).setCellValue(contractProductVo.getContractNo());
                rown.getCell(3).setCellValue(contractProductVo.getProductNo());
                rown.getCell(4).setCellValue(contractProductVo.getCnumber());
                rown.getCell(5).setCellValue(contractProductVo.getFactoryName());
                rown.getCell(6).setCellValue(new SimpleDateFormat("yyyy-MM-dd").format(contractProductVo.getDeliveryPeriod()));
                rown.getCell(7).setCellValue(new SimpleDateFormat("yyyy-MM-dd").format(contractProductVo.getShipTime()));
                rown.getCell(8).setCellValue(contractProductVo.getTradeTerms());
            }
        }

        //3. 文件下载(工具类)
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        workbook.write(byteArrayOutputStream);//将工作簿写入到输出流
        DownloadUtil.download(byteArrayOutputStream, response, "出货表.xlsx");
    }






    //大标题的样式
    public CellStyle bigTitleStyle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 16);
        font.setBold(true);//字体加粗
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);                //横向居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);        //纵向居中
        return style;
    }

    //小标题的样式
    public CellStyle littleTitleStyle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setFontName("黑体");
        font.setFontHeightInPoints((short) 12);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);                //横向居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);        //纵向居中
        style.setBorderTop(BorderStyle.THIN);                        //上细线
        style.setBorderBottom(BorderStyle.THIN);                    //下细线
        style.setBorderLeft(BorderStyle.THIN);                        //左细线
        style.setBorderRight(BorderStyle.THIN);                        //右细线
        return style;
    }

    //文字样式
    public CellStyle textStyle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setFontName("Times New Roman");
        font.setFontHeightInPoints((short) 10);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.LEFT);                //横向居左
        style.setVerticalAlignment(VerticalAlignment.CENTER);        //纵向居中
        style.setBorderTop(BorderStyle.THIN);                        //上细线
        style.setBorderBottom(BorderStyle.THIN);                    //下细线
        style.setBorderLeft(BorderStyle.THIN);                        //左细线
        style.setBorderRight(BorderStyle.THIN);                        //右细线

        return style;
    }


}
