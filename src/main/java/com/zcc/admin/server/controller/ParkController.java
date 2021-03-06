package com.zcc.admin.server.controller;

import com.github.pagehelper.PageHelper;
import com.zcc.admin.server.service.ParkService;
import com.zcc.admin.server.common.BaseResponse;
import com.zcc.admin.server.common.ResponseBuilder;
import com.zcc.admin.server.constants.HttpConstans;
import com.zcc.admin.server.dao.ParkMapper;
import com.zcc.admin.server.model.Park;
import com.zcc.admin.server.page.table.PageTableHandler;
import com.zcc.admin.server.page.table.PageTableRequest;
import com.zcc.admin.server.page.table.PageTableResponse;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * @ClassName ParkController
 * @Description 车位管理Controller
 * @Author superlewy
 */
@RestController
@RequestMapping("/park")
public class ParkController {

    @Autowired
    private ParkService parkService;

    @Autowired
    private ParkMapper parkMapper;

    /**
     * 根据ID查询车位信息
     *
     * @param parkNo
     * @return
     */
    @RequiresPermissions("park:query")
    @GetMapping("/selectPark")
    public BaseResponse selectPark(String parkNo) {
        ResponseBuilder custom = ResponseBuilder.custom();
        Park park = new Park();
        try {
            park = parkService.selectByPrimaryKey(parkNo);
            if (park != null) {
                custom.data(park).success(HttpConstans.SUCCESS, HttpConstans.SUCCESS_CODE);
            } else {
                custom.failed(HttpConstans.FAIL, HttpConstans.ERROR_CODE);
            }
        } catch (Exception e) {
            custom.failed(HttpConstans.FAIL, HttpConstans.EXCEPTION_CODE);
        }
        return custom.build();
    }

    /**
     * 添加车位信息
     *
     * @param park
     * @return
     */
    @RequiresPermissions("park:add")
    @PostMapping("/savePark")
    public BaseResponse savePark(@RequestBody Park park) {
        ResponseBuilder custom = ResponseBuilder.custom();
        try {
            int result = parkService.insertSelective(park);
            if (result != 0) {
                custom.data(park).success(HttpConstans.SUCCESS, HttpConstans.SUCCESS_CODE);
            } else {
                custom.failed(HttpConstans.FAIL, HttpConstans.EXCEPTION_CODE);
            }
        } catch (Exception e) {
            custom.failed(e.toString(), HttpConstans.EXCEPTION_CODE);
        }
        return custom.build();
    }

    /**
     * 更新/修改车位信息
     *
     * @param park
     * @return
     */
    @RequiresPermissions("park:add")
    @PostMapping("/updatePark")
    public BaseResponse updatePark(@RequestBody Park park) {
        ResponseBuilder custom = ResponseBuilder.custom();
        try {
            int result = parkService.updateByPrimaryKeySelective(park);
            if (result != 0) {
                custom.data(park).success(HttpConstans.SUCCESS, HttpConstans.SUCCESS_CODE);
            } else {
                custom.failed(HttpConstans.FAIL, HttpConstans.EXCEPTION_CODE);
            }
        } catch (Exception e) {
            custom.failed(HttpConstans.FAIL, HttpConstans.EXCEPTION_CODE);
        }
        return custom.build();
    }

    /**
     * 删除车位信息
     *
     * @param parkNo
     * @return
     */
    @RequiresPermissions("park:del")
    @DeleteMapping("/deletePark")
    public BaseResponse deletePark(String parkNo) {
        ResponseBuilder custom = ResponseBuilder.custom();
        try {
            int result = parkService.deleteByPrimaryKey(parkNo);
            if (result != 0) {
                custom.success(HttpConstans.SUCCESS, HttpConstans.SUCCESS_CODE);
            } else {
                custom.failed(HttpConstans.FAIL, HttpConstans.EXCEPTION_CODE);
            }
        } catch (Exception e) {
            custom.failed(HttpConstans.FAIL, HttpConstans.EXCEPTION_CODE);
        }
        return custom.build();
    }

    /**
     * 分页获取停车位信息列表
     * @param currPage 当前页
     * @param pageSize 每页显示数量
     * @return
     */
    @RequiresPermissions("park:query")
    @GetMapping("/getParkList")
    public BaseResponse getParkList(@RequestParam Integer currPage,
                                     @RequestParam Integer pageSize) {
        ResponseBuilder custom = ResponseBuilder.custom();

        if (Objects.isNull(currPage) || currPage == 0) currPage = 1;
        if (Objects.isNull(pageSize) || pageSize == 0) pageSize = 10;
        //分页
        PageHelper.startPage(currPage, pageSize);
        try {
            List<Park> parkList = parkMapper.getParkList();
            Long count = parkMapper.getParkCount();
            if (count != 0 && parkList != null) {
                custom.data(parkList).currPage(currPage)
                        .pageSize(pageSize).totalCount(count.intValue());
            } else {
                custom.failed(HttpConstans.FAIL, HttpConstans.ERROR_CODE);
            }
        } catch (Exception e) {
            custom.failed(HttpConstans.FAIL, HttpConstans.EXCEPTION_CODE);
        }
        return custom.build();
    }

    /**
     * 多参数分页查询列表
     *
     * @param request
     * @return
     */
    @RequiresPermissions("park:query")
    @GetMapping("/selectParkList")
    public PageTableResponse selectCarList(PageTableRequest request) {
        return new PageTableHandler(new PageTableHandler.CountHandler() {
            @Override
            public int count(PageTableRequest request) {
                return parkMapper.count(request.getParams());
            }
        }, new PageTableHandler.ListHandler() {
            @Override
            public List<Park> list(PageTableRequest request) {
                return parkMapper.selectParkList(request.getParams(), request.getOffset(), request.getLimit());
            }
        }).handle(request);
    }

    /**
     * 导入车位信息
     *
     * @param file
     * @return
     */
    @RequiresPermissions("park:add")
    @PostMapping("/excelPark")
    public BaseResponse excelBuild(MultipartFile file) throws IOException {
        ResponseBuilder custom = ResponseBuilder.custom();
        try {
            String result = parkService.excelPark(file);
            if (result.equals("0")) {
                custom.success(HttpConstans.SUCCESS, HttpConstans.SUCCESS_CODE);
            } else {
                custom.failed(HttpConstans.FAIL, HttpConstans.EXCEPTION_CODE);
            }
        } catch (Exception e) {
            custom.failed(HttpConstans.FAIL, HttpConstans.EXCEPTION_CODE);
        }
        return custom.build();
    }
}
