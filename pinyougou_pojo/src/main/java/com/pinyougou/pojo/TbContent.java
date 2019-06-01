package com.pinyougou.pojo;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "tb_content")
public class TbContent implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 内容类目ID
     */
    @Column(name = "category_id")
    private Integer categoryId;

    /**
     * 内容标题
     */
    private String title;

    /**
     * 链接
     */
    private String url;

    /**
     * 图片绝对路径
     */
    private String pic;

    /**
     * 状态
     */
    private String status;

    /**
     * 排序
     */
    @Column(name = "sort_order")
    private Integer sortOrder;

    private static final long serialVersionUID = 1L;

    /**
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * 获取内容标题
     *
     * @return title - 内容标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置内容标题
     *
     * @param title 内容标题
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 获取链接
     *
     * @return url - 链接
     */
    public String getUrl() {
        return url;
    }

    /**
     * 设置链接
     *
     * @param url 链接
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 获取图片绝对路径
     *
     * @return pic - 图片绝对路径
     */
    public String getPic() {
        return pic;
    }

    /**
     * 设置图片绝对路径
     *
     * @param pic 图片绝对路径
     */
    public void setPic(String pic) {
        this.pic = pic;
    }

    /**
     * 获取状态
     *
     * @return status - 状态
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置状态
     *
     * @param status 状态
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 获取排序
     *
     * @return sort_order - 排序
     */
    public Integer getSortOrder() {
        return sortOrder;
    }

    /**
     * 设置排序
     *
     * @param sortOrder 排序
     */
    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }


}