package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.abel533.entity.Example;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.mapper.TbItemCatMapper;
import com.pinyougou.pojo.TbItemCat;
import com.pinyougou.sellergoods.service.ItemCatService;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 业务逻辑实现
 * @author Steven
 *
 */
@Service(interfaceClass = ItemCatService.class)
@Transactional
public class ItemCatServiceImpl implements ItemCatService {

	@Autowired
	private TbItemCatMapper itemCatMapper;
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbItemCat> findAll() {
		return itemCatMapper.select(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize,TbItemCat itemCat) {
		PageResult<TbItemCat> result = new PageResult<TbItemCat>();
        //设置分页条件
        PageHelper.startPage(pageNum, pageSize);

        //构建查询条件
        Example example = new Example(TbItemCat.class);
        Example.Criteria criteria = example.createCriteria();
		
		if(itemCat!=null){			
						//如果字段不为空
			if (itemCat.getName()!=null && itemCat.getName().length()>0) {
				criteria.andLike("name", "%" + itemCat.getName() + "%");
			}
	
		}

        //查询数据
        List<TbItemCat> list = itemCatMapper.selectByExample(example);
        //返回数据列表
        result.setRows(list);

        //获取总页数
        PageInfo<TbItemCat> info = new PageInfo<TbItemCat>(list);
        result.setPages((long)info.getPages());
		
		return result;
	}

	/**
	 * 增加
	 */
	@Override
	public void add(TbItemCat itemCat) {
		itemCatMapper.insertSelective(itemCat);		
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(TbItemCat itemCat){
		itemCatMapper.updateByPrimaryKeySelective(itemCat);
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public TbItemCat getById(Long id){
		return itemCatMapper.selectByPrimaryKey(id);
	}

//  递归要删除的所有id
	private List list ;
	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		//数组转list
		list = new ArrayList<>();
		for (Long id : ids) {
			recursionDele(id);
		}
		Example example = new Example(TbItemCat.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id",list);

        //跟据查询条件删除数据
        itemCatMapper.deleteByExample(example);
	}

	private void recursionDele(Long id){
			list.add(id);
   //构建查询条件
			TbItemCat tbItemCat1 = new TbItemCat();
			tbItemCat1.setParentId(id);
			List<TbItemCat> catList = itemCatMapper.select(tbItemCat1);
			if (catList!=null && catList.size()>0){
				for (TbItemCat tbItemCat : catList) {
					recursionDele(tbItemCat.getId());
				}
			}
	}

    @Override
    public List<TbItemCat> findByParentId(Integer parentId) {
		TbItemCat tbItemCat = new TbItemCat();
		tbItemCat.setParentId((long)parentId);
		List<TbItemCat> list = itemCatMapper.select(tbItemCat);
		return list;
    }


}
