package com.itdr.controllers.backend;

import com.itdr.common.ServerResponse;
import com.itdr.pojo.Categorys;
import com.itdr.services.CategoryManageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/manage/category/")
public class CategoryManageController {


    @Autowired
    CategoryManageService categoryManageService;

    /**
     * 根据分类id查询所有的子分类（包括本身）
     * @param categoryId
     * @return
     */
    @PostMapping("get_deep_category.do")
    public ServerResponse<Categorys> getDeepCategory(Integer categoryId){
        return categoryManageService.getDeepCategory(categoryId);
    }

}
