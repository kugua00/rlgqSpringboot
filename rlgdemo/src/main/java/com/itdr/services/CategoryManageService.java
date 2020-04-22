package com.itdr.services;


import com.itdr.common.ServerResponse;
import com.itdr.pojo.Categorys;

public interface CategoryManageService {

    /**
     * 根据分类id查询递归子节点categoryId（包括本身）
     * @param categoryId
     * @return
     */
    ServerResponse<Categorys> getDeepCategory(Integer categoryId);
}
