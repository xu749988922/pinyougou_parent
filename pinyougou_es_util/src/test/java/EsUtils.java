import com.alibaba.fastjson.JSON;
import com.pinyougou.es.dao.ItemDao;
import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.pojo.TbItem;
import entity.EsItem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * es数据导入
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:spring/applicationContext-*.xml"})
public class EsUtils {
    @Autowired
    private TbItemMapper tbItemMapper;
    @Autowired
    private ItemDao itemDao;

    //把数据库中(上架的)的元素导入到es索引库
    @Test
    public void test(){
        TbItem where = new TbItem();
        where.setStatus("1");
        List<TbItem> tbItems = tbItemMapper.select(where);
        ArrayList<EsItem> esItems = new ArrayList();
        EsItem esItem = null;
        for (TbItem tbItem : tbItems) {
            esItem =new EsItem();
            BigDecimal tbItemPrice = tbItem.getPrice();
            esItem.setPrice(tbItemPrice.doubleValue());
            BeanUtils.copyProperties(tbItem,esItem);
            //嵌套域需要转换
            Map specMap = JSON.parseObject(tbItem.getSpec(), Map.class);
            esItem.setSpec(specMap);
            esItems.add(esItem);
        }
        itemDao.saveAll(esItems);
    }
}
