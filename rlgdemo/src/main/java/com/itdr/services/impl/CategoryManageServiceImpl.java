package com.itdr.services.impl;

import com.itdr.common.ServerResponse;
import com.itdr.mappers.CategorysMapper;
import com.itdr.pojo.Categorys;
import com.itdr.services.CategoryManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryManageServiceImpl implements CategoryManageService {

    @Autowired
    CategorysMapper categorysMapper;

    /**
     *根据分类id查询递归子节点categoryId（包括本身）
     * @param categoryId
     * @return
     */
    @Override
    public ServerResponse<Categorys> getDeepCategory(Integer categoryId) {
        if (categoryId == null || categoryId < 0 ){
            return ServerResponse.defeatedRs("非法的参数");
        }
        List<Integer> list = new ArrayList<>();
        list.add(categoryId);
        getAll(categoryId,list);

        return ServerResponse.successRS(list);
    }


    private void getAll(Integer pid,List<Integer> list){

        List<Categorys> li = categorysMapper.selectByParentId(pid);

        if (li != null && li.size() != 0){
            for (Categorys ca: li
            ) {
                list.add(ca.getId());
                getAll(ca.getId(),list);
            }
        }
    }
}
