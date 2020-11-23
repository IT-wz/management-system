package com.itheima.web.controller.system;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.itheima.domain.system.SysLog;
import com.itheima.service.system.SysLogService;
import com.itheima.web.controller.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/system/log")
public class SysLogController extends BaseController {
    @Reference
    private SysLogService sysLogService;

    @RequestMapping(value = "/list", name = "日志列表查询")
    public String list(
            @RequestParam(value = "page", defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        PageInfo<SysLog> pageInfo = sysLogService.findByPage(getCompanyId(), pageNum, pageSize);
        request.setAttribute("page", pageInfo);

        return "/system/log/log-list";
    }


}
