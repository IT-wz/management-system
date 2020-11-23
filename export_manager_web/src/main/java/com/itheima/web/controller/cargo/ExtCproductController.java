package com.itheima.web.controller.cargo;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.itheima.domain.cargo.ExtCproduct;
import com.itheima.domain.cargo.ExtCproductExample;
import com.itheima.domain.cargo.Factory;
import com.itheima.domain.cargo.FactoryExample;
import com.itheima.service.cargo.ExtCproductService;
import com.itheima.service.cargo.FactoryService;
import com.itheima.utils.FileUploadUtil;
import com.itheima.web.controller.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/cargo/extCproduct")
public class ExtCproductController extends BaseController {

    @Reference
    private ExtCproductService extCproductService;

    @Reference
    private FactoryService factoryService;

    @Autowired
    private FileUploadUtil fileUploadUtil;

    @RequestMapping(value = "/list", name = "附件列表查询")
    public String list(
            @RequestParam(value = "page", defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            String contractId,
            String contractProductId
    ) {

        //1 查询当前货物下的所有附件
        ExtCproductExample extCproductExample = new ExtCproductExample();
        extCproductExample.createCriteria().andContractProductIdEqualTo(contractProductId);
        PageInfo pageInfo = extCproductService.findByPage(pageNum, pageSize, extCproductExample);
        request.setAttribute("page", pageInfo);


        //2 查询所有生产附件的厂家
        FactoryExample factoryExample = new FactoryExample();
        factoryExample.createCriteria().andCtypeEqualTo("附件");
        List<Factory> factoryList = factoryService.findAll(factoryExample);
        request.setAttribute("factoryList", factoryList);

        //3 回显合同id  货物id
        request.setAttribute("contractId", contractId);
        request.setAttribute("contractProductId", contractProductId);
        return "/cargo/extc/extc-list";
    }


    @RequestMapping(value = "/toUpdate", name = "跳转附件修改页面")
    public String toUpdate(String id) {
        //1 查询当前附件信息
        ExtCproduct extCproduct = extCproductService.findById(id);
        request.setAttribute("extCproduct", extCproduct);

        //2 查询所有生产附件的厂家
        FactoryExample factoryExample = new FactoryExample();
        factoryExample.createCriteria().andCtypeEqualTo("附件");
        List<Factory> factoryList = factoryService.findAll(factoryExample);
        request.setAttribute("factoryList", factoryList);

        return "/cargo/extc/extc-update";
    }

    @RequestMapping(value = "/edit", name = "附件新增或修改")
    public String edit(ExtCproduct extCproduct, MultipartFile productPhoto) throws Exception {

        //调用七牛云存储起来, 将访问路径保存到数据库
        String imagePath = fileUploadUtil.upload(productPhoto);
        extCproduct.setProductImage(imagePath);

        if (StringUtils.isEmpty(extCproduct.getId())) {
            //1 设置id
            extCproduct.setId(UUID.randomUUID().toString());

            //2. 设置企业信息
            extCproduct.setCompanyId(getCompanyId());
            extCproduct.setCompanyName(getCompanyName());

            //3. 调用service保存操作
            extCproductService.save(extCproduct);
        } else {
            extCproductService.update(extCproduct);
        }
        return "redirect:/cargo/extCproduct/list.do?contractId=" + extCproduct.getContractId() + "&contractProductId=" + extCproduct.getContractProductId();
    }

    @RequestMapping(value = "/delete", name = "附件删除")
    public String delete(String id, String contractId, String contractProductId) {
        extCproductService.delete(id);//附件id
        return "redirect:/cargo/extCproduct/list.do?contractId=" + contractId + "&contractProductId=" + contractProductId;
    }
}
