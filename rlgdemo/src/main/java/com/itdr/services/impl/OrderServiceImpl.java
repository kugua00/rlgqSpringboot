package com.itdr.services.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itdr.common.ServerResponse;
import com.itdr.mappers.*;
import com.itdr.pojo.*;
import com.itdr.pojo.vo.OrderItemListVO;
import com.itdr.pojo.vo.OrderItemVO;
import com.itdr.pojo.vo.OrderVO;
import com.itdr.services.OrderService;
import com.itdr.utils.BigDecimalUtils;
import com.itdr.utils.DateUtils;
import com.itdr.utils.PoToVoUtil;
import com.itdr.utils.PropertiesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {


    @Autowired
    CartsMapper cartsMapper;
    @Autowired
    ProductsMapper productsMapper;
    @Autowired
    OrdersMapper ordersMapper;
    @Autowired
    OrderItemsMapper orderItemsMapper;
    @Autowired
    ShippingsMapper shippingsMapper;


    /**
     * 创建订单
     * @param uid
     * @param shippingId
     * @return
     */
    @Override
    public ServerResponse createOrder(Integer uid, Integer shippingId) {
        if (shippingId == null || shippingId <= 0){
            return ServerResponse.defeatedRs("非法参数");
        }
        //存储订单选中商品数据
        List<Products> productsList = new ArrayList<>();


        //获取用户购物车中选中的商品数据
        List<Carts> cartsList = cartsMapper.selectByUidAll(uid);

        //获取用户地址信息
        Shippings shippings = shippingsMapper.selectByIdAndUid(shippingId,uid);
        if (shippings == null ){
            return ServerResponse.defeatedRs("用户收货地址不存在");
        }


        //计算订单总价
        if (cartsList.size() == 0){
            return ServerResponse.defeatedRs("至少选中一件商品");
        }
        BigDecimal payment = new BigDecimal("0");
        for (Carts carts : cartsList) {
            //判断商品是否失效
            Integer productId = carts.getProductId();
            //根据商品ID获取商品数据
            Products p = productsMapper.selectByProductId(productId);
            if (p == null){
                return ServerResponse.defeatedRs("商品不存在");
            }
            if( p.getStatus() != 1){
                return ServerResponse.defeatedRs(p.getName()+"已下架");
            }
            //校验库存
            if (carts.getQuantity() > p.getStock()){
                return ServerResponse.defeatedRs(p.getName() + "库存不足");
            }

            //根据购物车购物数量和商品单价计算一条购物车信息的总价
            BigDecimal mul = BigDecimalUtils.mul(p.getPrice().doubleValue(), carts.getQuantity());
            //总订单价格
            payment = BigDecimalUtils.add(payment.doubleValue(),mul.doubleValue());

            //放入集合中备用
            productsList.add(p);

         }

        //创建订单，没有问题要存到数据库中
        Orders order = this.getOrder(uid, shippingId);
        int insert = ordersMapper.insert(order);
        if (insert <= 0){
            return ServerResponse.defeatedRs(order.getOrderNo() + "订单创建失败");
        }

        //创建订单详情（没有问题要存到数据库中，批量插入）
        List<OrderItems> orderItemsList = this.getOrderItem(uid, order.getOrderNo(), productsList, cartsList);
        int orderItemInsert = orderItemsMapper.insertAll(orderItemsList);
        if (orderItemInsert <= 0){
            return ServerResponse.defeatedRs(order.getOrderNo() + "订单详情创建失败");
        }

        //插入成功，减少商品库存
        for (OrderItems orderItems : orderItemsList) {
            for (Products products : productsList) {
                if (orderItems.getProductId() == products.getId()){
                    int count = products.getStock() - orderItems.getQuantity();
                    if (count < 0){
                        return ServerResponse.defeatedRs("库存不足");
                    }
                    products.setStock(count);
                    int productUpdate = productsMapper.updateById(products);
                    if (productUpdate < 0){
                        return ServerResponse.defeatedRs("更新商品库存失败");
                    }
                }
            }
        }
        //清空购物车
        int cartsdelect = cartsMapper.deleteAllByIdAndUid(cartsList,uid);
        if (cartsdelect <= 0){
            return ServerResponse.defeatedRs("清空购物车数据失败");
        }


        // 拼接Vo类 返回数据
        List<OrderItemVO> orderItemVOList = this.getOrderItemVOList(orderItemsList);



        //封装orderVo类
        OrderVO orderVO = new OrderVO();

        orderVO.setOrderNo(order.getOrderNo());
        orderVO.setShippingId(shippingId);
        orderVO.setPayment(order.getPayment());
        orderVO.setPaymentType(order.getPaymentType());
        orderVO.setPostage(order.getPostage());
        orderVO.setStatus(order.getStatus());
        orderVO.setPaymentTime(DateUtils.dateToStr(order.getPaymentTime()));
        orderVO.setSendTime(DateUtils.dateToStr(order.getSendTime()));
        orderVO.setEndTime(DateUtils.dateToStr(order.getEndTime()));
        orderVO.setCloseTime(DateUtils.dateToStr(order.getCloseTime()));
        orderVO.setCreateTime(DateUtils.dateToStr(order.getCreateTime()));
        orderVO.setOrderItemVOList(orderItemVOList);
        orderVO.setShippingVO(PoToVoUtil.getShippingVO(shippings));
        try {
            orderVO.setImageHost(PropertiesUtil.getValue("imageHost"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ServerResponse.successRS(orderVO);
    }





    /**
     * 获取订单详情信息
     * @param id
     * @param orderNo
     * @return
     */
    @Override
    public ServerResponse getOrderCartProduct(Integer id, Long orderNo) {
        OrderItemListVO orderItemListVO = new OrderItemListVO();
        List<OrderItems> orderItemsList = null;
        List<OrderItemVO> orderItemVOList = null;
        BigDecimal payment = new BigDecimal("0");

        //根据用户id和订单编号获取对应的订单详情信息
        if (orderNo != null ){
            orderItemsList = orderItemsMapper.selectByUidAndOrderNo(id,orderNo);
            orderItemVOList = this.getOrderItemVOList(orderItemsList);
            Orders orders = ordersMapper.selectByOrderNo(orderNo);

            if (orders == null){
                return ServerResponse.defeatedRs(orderNo + "号订单不存在");
            }
            payment = orders.getPayment();
        }else {
            //没有订单id， 根据用户id获取订单详情
            //获取用户购物车中选中的商品数据
            List<Carts> cartsList = cartsMapper.selectByUidAll(id);

            //存储订单选中商品数据
            List<Products> productsList = new ArrayList<>();

            //计算订单总价
            if (cartsList.size() == 0){
                return ServerResponse.defeatedRs("至少选中一件商品");
            }

            for (Carts carts : cartsList) {
                //判断商品是否失效
                Integer productId = carts.getProductId();
                //根据商品ID获取商品数据
                Products p = productsMapper.selectByProductId(productId);
                if (p == null){
                    return ServerResponse.defeatedRs("商品不存在");
                }
                if( p.getStatus() != 1){
                    return ServerResponse.defeatedRs(p.getName()+"已下架");
                }
                //校验库存
                if (carts.getQuantity() > p.getStock()){
                    return ServerResponse.defeatedRs(p.getName() + "库存不足");
                }

                //根据购物车购物数量和商品单价计算一条购物车信息的总价
                BigDecimal mul = BigDecimalUtils.mul(p.getPrice().doubleValue(), carts.getQuantity());
                //总订单价格
                payment = BigDecimalUtils.add(payment.doubleValue(),mul.doubleValue());

                //放入集合中备用
                productsList.add(p);
            }

            orderItemsList = this.getOrderItem(id,null,productsList,cartsList);
            orderItemVOList = this.getOrderItemVOList(orderItemsList);

        }

        //拼装orderItemListVO
        orderItemListVO.setOrderItemVOList(orderItemVOList);
        try {
            orderItemListVO.setImageHost(PropertiesUtil.getValue("imageHost"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        orderItemListVO.setProductTotalPrice(payment);

        return ServerResponse.successRS(orderItemListVO);
    }


    /**
     * 获取登录用户的订单列表
     * @param id
     * @return
     */
    @Override
    public ServerResponse listdo(Integer id, Integer pageSize, Integer pageNum) {
        List<OrderVO> orderVOList = new ArrayList<>();

        //该方法后面跟的必须是一条查询语句
        PageHelper.startPage(pageNum,pageSize);
        List<Orders> ordersList = ordersMapper.selectByUid(id);

        //循环创建OrderVo对象
        for (Orders order : ordersList) {
            //封装orderVo类
            OrderVO orderVO = new OrderVO();
            List<OrderItems> orderItemsList = orderItemsMapper.selectByUidAndOrderNo(id,order.getOrderNo());
            List<OrderItemVO> orderItemVOList = this.getOrderItemVOList(orderItemsList);

            orderVO.setOrderNo(order.getOrderNo());
            orderVO.setShippingId(order.getShippingId());
            orderVO.setPayment(order.getPayment());
            orderVO.setPaymentType(order.getPaymentType());
            orderVO.setStatus(order.getStatus());
            orderVO.setPostage(order.getPostage());
            orderVO.setPaymentTime(DateUtils.dateToStr(order.getPaymentTime()));
            orderVO.setSendTime(DateUtils.dateToStr(order.getSendTime()));
            orderVO.setEndTime(DateUtils.dateToStr(order.getEndTime()));
            orderVO.setCreateTime(DateUtils.dateToStr(order.getCreateTime()));
            orderVO.setCloseTime(DateUtils.dateToStr(order.getCloseTime()));
            orderVO.setOrderItemVOList(orderItemVOList);
            orderVO.setShippingVO(PoToVoUtil.getShippingVO(shippingsMapper.selectByIdAndUid(order.getShippingId(),id)));
            try {
                orderVO.setImageHost(PropertiesUtil.getValue("imageHost"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            orderVOList.add(orderVO);
        }

        //分页处理
        PageInfo pageInfo = new PageInfo(ordersList);
        pageInfo.setList(orderVOList);

        return ServerResponse.successRS(pageInfo);
    }


    /**
     * 取消订单
     * @param id
     * @param orderNo
     * @return
     */
    @Override
    public ServerResponse countermandOrder(Integer id, Long orderNo) {
        if (orderNo == null || orderNo <= 0){
            return ServerResponse.defeatedRs("非法参数");
        }

        //判断订单是否存在
        Orders orders = ordersMapper.selectByOrderNo(orderNo);
        if (orders == null || orders.getUserId().equals(id)){
            return ServerResponse.defeatedRs(orderNo + "订单不存在");
        }

        //判断订单是否为未付款
        if (orders.getStatus() != 10){
            return ServerResponse.defeatedRs(orderNo+"订单状态非法");
        }

        //取消订单  （改变订单状态）
        orders.setStatus(0);
        int i = ordersMapper.updateToStatus(orders);
        if (i <= 0){
            return ServerResponse.defeatedRs(orderNo +"订单取消失败");
        }

        //取消库存锁定
        List<OrderItems> orderItemsList = orderItemsMapper.selectByOrderNo(orderNo);
        for (OrderItems orderItems : orderItemsList) {
            Integer productId = orderItems.getProductId();
            Products products = productsMapper.selectByProductId(productId);
            if (products != null){
                products.setStock(products.getStock() + orderItems.getQuantity());
                int i1 = productsMapper.updateById(products);
                if (i1 <= 0){
                    return ServerResponse.defeatedRs("商品更新库存失败");
                }
            }else {
                return ServerResponse.defeatedRs("该订单商品不存在");
            }

        }

        return ServerResponse.successRS("订单取消成功");
    }


    /**
     * 创建一个订单编号
     * @param uid
     * @return
     */
    private Orders getOrder(Integer uid,Integer shippingId){
        Orders order = new Orders();
        order.setUserId(uid);
        order.setOrderNo(this.getOrderNo());
        order.setShippingId(shippingId);
        order.setPaymentType(1);
        order.setPostage(0);
        order.setStatus(10);


        return order;
    }

    /**
     * 获取orderItemVoList
     * @param orderItemsList
     * @return
     */
    public List<OrderItemVO> getOrderItemVOList (List<OrderItems> orderItemsList){
        List<OrderItemVO> orderItemVOList = new ArrayList<>();
        for (OrderItems orderItems : orderItemsList) {
            OrderItemVO orderitemVO = PoToVoUtil.getOrderitemToOrderItemVO(orderItems);
            orderItemVOList.add(orderitemVO);
        }
        return orderItemVOList;
    }



    /**
     * 创建一个订单详情对象
     * @param uid
     * @param orderNo
     * @param productsList
     * @param cartsList
     * @return
     */
    private List<OrderItems> getOrderItem(Integer uid,Long orderNo,List<Products> productsList,List<Carts> cartsList){
        List<OrderItems> itemsList = new ArrayList<>();



        for (Carts carts : cartsList) {
            OrderItems orderItems = new OrderItems();
            orderItems.setQuantity(carts.getQuantity());
            for (Products p : productsList){
                if (carts.getProductId().equals(p.getId() )){
                    orderItems.setUserId(uid);
                    orderItems.setOrderNo(orderNo);
                    orderItems.setProductId(p.getId());
                    orderItems.setProductName(p.getName());
                    orderItems.setProductImage(p.getMainImage());
                    orderItems.setCurrentUnitPrice(p.getPrice());

                    //根据购物车购物数量和商品单价计算一条购物车信息的总价
                    BigDecimal mul = BigDecimalUtils.mul(p.getPrice().doubleValue(), carts.getQuantity());
                    orderItems.setTotalPrice(mul);

                    itemsList.add(orderItems);
                }
            }
        }

        return itemsList;
    }




    //计算订单总价
    private BigDecimal getPayment(){
        return null;
    }

    /**
     * 生成一个订单编号
     * @return
     */
    private Long getOrderNo(){
        long l = System.currentTimeMillis();
        long orderNo = l + Math.round(Math.random()*100) ;
        return orderNo;
    }


}
