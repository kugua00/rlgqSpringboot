package com.itdr.services.impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itdr.common.ServerResponse;
import com.itdr.mappers.CategorysMapper;
import com.itdr.mappers.ProductsMapper;
import com.itdr.pojo.Categorys;
import com.itdr.pojo.Products;
import com.itdr.pojo.vo.ProductVO;
import com.itdr.services.ProductService;
import com.itdr.utils.PoToVoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;


@Service
public class ProductServiceImpl implements ProductService {

    
    
    @Autowired
    ProductsMapper productsMapper;

    @Autowired
    CategorysMapper categorysMapper;

    /**
     * 获取产品分类
     * @param pid
     * @return
     */
    @Override
    public ServerResponse<Products> topcategory(Integer pid) {
        if (pid == null || pid < 0){
            return ServerResponse.defeatedRs("非法的参数");
        }

        //根据商品分类id 查询子分类
        List<Categorys> list = categorysMapper.selectByParentId(pid);

        if (list == null){
            return ServerResponse.defeatedRs("查询的ID不存在");
        }

        if (list.size() == 0){
            return ServerResponse.defeatedRs("没有子分类");
        }
        return ServerResponse.successRS(list);
    }

    /**
     * 获取商品详情
     * @param productId
     * @param is_new
     * @param is_hot
     * @param is_banner
     * @return
     */
    @Override
    public ServerResponse<Products> detail(Integer productId, Integer is_new, Integer is_hot, Integer is_banner) {
        if (productId == null || productId < 0){
            return ServerResponse.defeatedRs("非法的参数");
        }
        Products products = productsMapper.selectById(productId,is_new,is_hot,is_banner);

        if (products == null){
            return ServerResponse.defeatedRs("商品不存在");
        }

        //实体类转VO类
        ProductVO productVO = null;
        try {
            productVO = PoToVoUtil.productToProductVo(products);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ServerResponse.successRS(productVO);
    }

    /**
     * 商品搜索 + 动态排序
     * @param productId
     * @param keyWord
     * @param pageNum
     * @param pageSize
     * @param orderBy
     * @return
     */
    @Override
    public ServerResponse<Products> listProduct(Integer productId, String keyWord, Integer pageNum, Integer pageSize, String orderBy) {
        if ((productId == null || productId < 0) && (keyWord == null || keyWord.equals(""))){
            return ServerResponse.defeatedRs("非法参数");
        }


        //分割排序参数
        String[] split = new String[2];
        if (!orderBy.equals("")){
            split = orderBy.split("_");
        }
        String keys = "%"+keyWord+"%";

        PageHelper.startPage(pageNum,pageSize);
        List<Products> li = productsMapper.selectByIdOrName(productId,keys,split[0],split[1]);
        PageInfo pf = new PageInfo(li);

        return ServerResponse.successRS(pf);
    }
}
