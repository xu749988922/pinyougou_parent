package com.pinyougou.sellergoods.service;
import java.util.List;
import com.pinyougou.pojo.TbAddress;

import entity.PageResult;
/**
 * 业务逻辑接口
 * @author Steven
 *
 */
public interface AddressService {

	/**
	 * 返回全部列表
	 * @return
	 */
	public List<TbAddress> findAll();
	
	
	/**
     * 分页查询列表
     * @return
     */
    public PageResult<TbAddress> findPage(int pageNum, int pageSize,TbAddress address);
	
	
	/**
	 * 增加
	*/
	public void add(TbAddress address);
	
	
	/**
	 * 修改
	 */
	public void update(TbAddress address);
	

	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	public TbAddress getById(Long id);
	
	
	/**
	 * 批量删除
	 * @param ids
	 */
	public void delete(Long [] ids);

	
}
