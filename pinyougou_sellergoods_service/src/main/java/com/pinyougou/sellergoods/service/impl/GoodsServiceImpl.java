package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.abel533.entity.Example;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.mapper.*;
import com.pinyougou.pojo.*;
import com.pinyougou.pojogroup.Goods;
import com.pinyougou.sellergoods.service.GoodsService;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 业务逻辑实现
 *
 * @author Steven
 */
@Service(interfaceClass = GoodsService.class)
@Transactional
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private TbGoodsMapper goodsMapper;
    @Autowired
    private TbGoodsDescMapper tbGoodsDescMapper;
    @Autowired
    private TbItemMapper tbItemMapper;

    /**
     * 查询全部
     */
    @Override
    public List<TbGoods> findAll() {
        return goodsMapper.select(null);
    }

    /**
     * 按分页查询
     */
    @Override
    public PageResult findPage(int pageNum, int pageSize, TbGoods goods) {

            PageResult<TbGoods> result = new PageResult<TbGoods>();
            //设置分页条件
            PageHelper.startPage(pageNum, pageSize);

            //构建查询条件
            Example example = new Example(TbGoods.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andIsNull("isDelete");

            if (goods != null) {
                //如果字段不为空
                if (goods.getSellerId() != null && goods.getSellerId().length() > 0) {
                    //criteria.andLike("sellerId", "%" + goods.getSellerId() + "%");
                    criteria.andEqualTo("sellerId",goods.getSellerId());
                }
                //如果字段不为空
                if (goods.getGoodsName() != null && goods.getGoodsName().length() > 0) {
                    criteria.andLike("goodsName", "%" + goods.getGoodsName() + "%");
                }
                //如果字段不为空
                if (goods.getAuditStatus() != null && goods.getAuditStatus().length() > 0) {
                    criteria.andLike("auditStatus", "%" + goods.getAuditStatus() + "%");
                }
                //如果字段不为空
                if (goods.getIsMarketable() != null && goods.getIsMarketable().length() > 0) {
                    criteria.andLike("isMarketable", "%" + goods.getIsMarketable() + "%");
                }
                //如果字段不为空
                if (goods.getCaption() != null && goods.getCaption().length() > 0) {
                    criteria.andLike("caption", "%" + goods.getCaption() + "%");
                }
                //如果字段不为空
                if (goods.getSmallPic() != null && goods.getSmallPic().length() > 0) {
                    criteria.andLike("smallPic", "%" + goods.getSmallPic() + "%");
                }
                //如果字段不为空
                if (goods.getIsEnableSpec() != null && goods.getIsEnableSpec().length() > 0) {
                    criteria.andLike("isEnableSpec", "%" + goods.getIsEnableSpec() + "%");
                }
                //如果字段不为空
                if (goods.getIsDelete() != null && goods.getIsDelete().length() > 0) {
                    criteria.andLike("isDelete", "%" + goods.getIsDelete() + "%");
                }

            }

            //查询数据
            List<TbGoods> list = goodsMapper.selectByExample(example);
            //返回数据列表
            result.setRows(list);

            //获取总页数
            PageInfo<TbGoods> info = new PageInfo<TbGoods>(list);
            result.setPages((long) info.getPages());

            return result;
    }

    @Autowired
    private TbItemCatMapper tbItemCatMapper;
    @Autowired
    private TbBrandMapper tbBrandMapper;
    @Autowired
    private TbSellerMapper tbSellerMapper;

    /**
     * 增加
     */
    @Override
    public void add(Goods goods) {
        goods.getTbGoods().setAuditStatus("0");
//		保存商品的基本信息;注意状态要为0
        goodsMapper.insertSelective(goods.getTbGoods());
        //int i= 1/0;
        goods.getTbGoodsDesc().setGoodsId(goods.getTbGoods().getId());
//		保存商品的拓展信息,ID可以直接获取
        tbGoodsDescMapper.insertSelective(goods.getTbGoodsDesc());
        List<TbItem> tbItems = goods.getItemList();
        saveItemList(goods,tbItems);

    }

    public TbItem setItemValus(TbItem tbItem ,Goods goods){
        tbItem.setCategoryid(goods.getTbGoods().getCategory3Id());
        tbItem.setCreateTime(new Date());
        tbItem.setUpdateTime(tbItem.getCreateTime());
        tbItem.setGoodsId(goods.getTbGoods().getId());
        tbItem.setSellerId(goods.getTbGoods().getSellerId());
        TbSeller tbSeller = tbSellerMapper.selectByPrimaryKey(tbItem.getSellerId());
        tbItem.setSeller(tbSeller.getName());
        //商品类目id
        tbItem.setCategoryid(goods.getTbGoods().getCategory3Id());

        TbItemCat tbItemCat = tbItemCatMapper.selectByPrimaryKey(tbItem.getCategoryid());
        tbItem.setCategory(tbItemCat.getName());

        TbBrand tbBrand = tbBrandMapper.selectByPrimaryKey(goods.getTbGoods().getBrandId());
        tbItem.setBrand(tbBrand.getName());
        return tbItem;
    }

    /**
     * 修改
     */
    @Override
    public void update(Goods goods) {
        goods.getTbGoods().setAuditStatus("0");
        goodsMapper.updateByPrimaryKeySelective(goods.getTbGoods());
//		保存商品的拓展信息,ID可以直接获取
        tbGoodsDescMapper.updateByPrimaryKeySelective(goods.getTbGoodsDesc());
        TbItem tbItem = new TbItem();
        tbItem.setGoodsId(goods.getTbGoods().getId());
        tbItemMapper.delete(tbItem);
        List<TbItem> itemList = goods.getItemList();
        saveItemList(goods, itemList);
    }

    //iteamlist存储信息是add与up提升出来的方法
    public void saveItemList(Goods goods, List<TbItem> tbItems){
        if ("1".equals(goods.getTbGoods().getIsEnableSpec())) {
            for (TbItem tbItem : tbItems) {
                String title = goods.getTbGoods().getGoodsName();
                Map<String, String> skuMap = JSON.parseObject(tbItem.getSpec(), Map.class);
                for (String key : skuMap.keySet()) {
                    title += " " + skuMap.get(key);
                }
                tbItem.setTitle(title);


                tbItem.setSellPoint(goods.getTbGoods().getCaption());
                //图片
                List<Map> imgList = JSON.parseArray(goods.getTbGoodsDesc().getItemImages(), Map.class);
                if (imgList != null && imgList.size() > 0) {
                    tbItem.setImage(imgList.get(0).get("url").toString());
                }
                setItemValus(tbItem,goods);
                tbItemMapper.insertSelective(tbItem);
            }
        }else {
            TbItem item=new TbItem();
            item.setTitle(goods.getTbGoods().getGoodsName());//商品KPU+规格描述串作为SKU名称
            item.setPrice( goods.getTbGoods().getPrice() );//价格
            item.setStatus("1");//状态
            item.setIsDefault("1");//是否默认
            item.setNum(99999);//库存数量
            item.setSpec("{}");
            setItemValus(item,goods);
            tbItemMapper.insertSelective(item);
        }
    }

    /**
     * 根据ID获取实体
     *
     * @param id
     * @return
     */
    @Override
    public Goods getById(Long id) {
        TbGoods tbGoods = goodsMapper.selectByPrimaryKey(id);
        Goods goods = new Goods();
        goods.setTbGoods(tbGoods);
        TbGoodsDesc tbGoodsDesc = tbGoodsDescMapper.selectByPrimaryKey(id);
        goods.setTbGoodsDesc(tbGoodsDesc);
        TbItem tbItem = new TbItem();
        tbItem.setGoodsId(id);
        List<TbItem> select = tbItemMapper.select(tbItem);
        goods.setItemList(select);
        return goods;
    }

    /**
     * 批量删除
     */
    @Override
    public void delete(Long[] ids) {
        //数组转list
        List longs = Arrays.asList(ids);
        //构建查询条件
        Example example = new Example(TbGoods.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id", longs);
        TbGoods tbGoods = new TbGoods();
        tbGoods.setIsDelete("1");

        //跟据查询条件删除数据
        goodsMapper.updateByExampleSelective(tbGoods,example);
    }

    @Override
    public void updateStatus(Long[] ids,String auditStatus) {
        Example example = new Example(TbGoods.class);
        Example.Criteria criteria = example.createCriteria();
        List longs = Arrays.asList(ids);
        criteria.andIn("id",longs);
        TbGoods tbGoods = new TbGoods();
        tbGoods.setAuditStatus(auditStatus);
        goodsMapper.updateByExampleSelective(tbGoods,example);
    }

    @Override
    public void isMarketableStatus(Long id) {
        TbGoods tbGoods = goodsMapper.selectByPrimaryKey(id);
        if ("0".equals(tbGoods.getIsMarketable())){
            tbGoods.setIsMarketable("1");
        }else {
            tbGoods.setIsMarketable("0");
        }
        goodsMapper.updateByPrimaryKeySelective(tbGoods);
    }

    @Override
    public List<TbItem> findItemListByGoodsIdsAndStatus(Long[] goodsIds, String status) {
        Example example = new Example(TbItem.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("status",status);
        List longs = Arrays.asList(goodsIds);
        criteria.andIn("goodsId",longs);

        List<TbItem> tbItems = tbItemMapper.selectByExample(example);
        return tbItems;
    }


}
