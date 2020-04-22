package com.itdr.services.impl;

import com.itdr.common.Const;
import com.itdr.common.ServerResponse;
import com.itdr.mappers.CartsMapper;
import com.itdr.mappers.ProductsMapper;
import com.itdr.pojo.Carts;
import com.itdr.pojo.Products;
import com.itdr.pojo.vo.CartProductVO;
import com.itdr.pojo.vo.CartVO;
import com.itdr.services.CartService;
import com.itdr.utils.BigDecimalUtils;
import com.itdr.utils.PoToVoUtil;
import com.itdr.utils.PropertiesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    CartsMapper cartsMapper;
    @Autowired
    ProductsMapper productsMapper;

    /**
     * 购物车添加商品
     * @param productId
     * @param count
     * @param uid
     * @return
     */
    @Override
    public ServerResponse<CartVO> addOne(Integer productId, Integer count, Integer uid) {
        if(productId == null || productId <=0 || count == null || count <=0 ){
            return ServerResponse.defeatedRs("非法参数");
        }


        //查询如果购物车表中有这条信息 更新数量，  如果没有 添加记录
        Carts c = cartsMapper.selectByUidAndProductId(uid,productId);
        if (c != null){
            c.setQuantity(count);
            int i1 = cartsMapper.updateByPrimaryKeySelective(c);
        }else {
            //向购物车表中添加数据
            //创建一个cart对象
            Carts ca = new Carts();
            ca.setProductId(productId);
            ca.setUserId(uid);
            ca.setQuantity(count);
            ca.setChecked(1);

            int insert = cartsMapper.insert(ca);
        }

       /* CartVO cartVo = getCartVo(uid);
        return ServerResponse.successRS(cartVo);*/
        return listCart(uid);
    }


    /**
     * 获取登录用户购物车列表（内部封装CartVo类）
     * @param id
     * @return
     */
    @Override
    public ServerResponse<CartVO> listCart(Integer id) {
        CartVO cartVo = this.getCartVo(id);
        return ServerResponse.successRS(cartVo);
    }


    /**
     * 更新购物车某个产品数量
     * @param productId
     * @param count
     * @param id
     * @return
     */
    @Override
    public ServerResponse<CartVO> updateCart(Integer productId, Integer count, Integer id) {
        if(productId == null || productId <=0 || count == null || count <=0 ){
            return ServerResponse.defeatedRs("非法参数");
        }


        //查询如果购物车表中有这条信息 更新数量，  如果没有 添加记录
        Carts c = cartsMapper.selectByUidAndProductId(id,productId);


        //更新数据
        c.setQuantity(count);
        int i1 = cartsMapper.updateByPrimaryKeySelective(c);


        return listCart(id);
    }


    /**
     * 移除购物车某个产品
     * @param productIds
     * @param id
     * @return
     */
    @Override
    public ServerResponse<CartVO> deleteCart(String productIds, Integer id) {
        if (productIds == null || productIds.equals("")){
            return ServerResponse.defeatedRs("非法参数");
        }
        String[] split = productIds.split(",");
        List<String> list = Arrays.asList(split);
        int i = cartsMapper.deleteByProductIds(list,id);

        return listCart(id);
    }


    /**
     * 查询购物车里的商品信息条数
     * @param id
     * @return
     */
    @Override
    public ServerResponse<Integer> getCartProductCount(Integer id) {
        List<Carts> carts = cartsMapper.selectByUid(id);
        int size = carts.size();

        return ServerResponse.successRS(size);
    }


    /**
     * 购物车全选
     * @param id
     * @return
     */
    @Override
    public ServerResponse<CartVO> selectAll(Integer id, Integer check, Integer productId) {
        int i = cartsMapper.updateByUid(id,check,productId);
        return listCart(id);
    }

    /**
     * 购物车可复用方法
     * @param uid
     * @return
     */
    private CartVO getCartVo(Integer uid){
        //创建CartVo对象
        CartVO cartVO = new CartVO();

        //创建变量存储购物车总价
        BigDecimal cartTotalPrice = new BigDecimal("0");

        //用来存放CartProductVO对象的集合
        List<CartProductVO> cartProductVOList = new ArrayList<CartProductVO>();


        //根据用户id查询所有购物车信息
        List<Carts> liCart =  cartsMapper.selectByUid(uid);

        //从购物信息集合中拿出每一条数据，根据其中的商品id查询需要的商品信息
        if(liCart.size() != 0){
            for (Carts ca :liCart){

                //根据购物信息中的商品ID查询商品的数据
                Products p =  productsMapper.selectById(ca.getProductId(),0 ,0 ,0 );

                //使用工具类进行VO类封装
                CartProductVO cpv = PoToVoUtil.getOne(ca, p);

                //购物车更新有效库存
                Carts cartForQuantity = new Carts();
                cartForQuantity.setId(ca.getId());
                cartForQuantity.setQuantity(cpv.getQuantity());
                cartsMapper.updateByPrimaryKeySelective(cartForQuantity);


                //计算购物车总价(计算选中的商品)
                if (ca.getChecked() == Const.Cart.CHECK){
                    cartTotalPrice = BigDecimalUtils.add(cartTotalPrice.doubleValue(),cpv.getProductTotalPrice().doubleValue());
                }


                //把封装好的对象放入集合中
                cartProductVOList.add(cpv);
            }
        }

        //封装CartVO数据
        cartVO.setCartProductVOList(cartProductVOList);
        cartVO.setAllChecked(this.checkAll(uid));
        cartVO.setCartTotalPrice(cartTotalPrice);
        try {
            //通过工具类获取资源文件内的图片地址
            cartVO.setImageHost(PropertiesUtil.getValue("imageHost"));
        } catch (IOException e) {
            e.printStackTrace();
        }


        return cartVO;

    }


    /**
     * 判断用户购物车是否全选
     */
    private  Boolean checkAll(Integer uid){
        int i = cartsMapper.selectByUidCheck(uid, Const.Cart.CHECK);
        if (i == 0){
            return true;
        }else {
            return false;
        }
    }
}
