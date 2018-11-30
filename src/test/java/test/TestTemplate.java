package test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.SolrDataQuery;
import org.springframework.data.solr.core.query.result.ScoredPage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SimpleAliasRegistry;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import pojo.TbItem;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext-solr.xml")

public class TestTemplate {
	@Autowired
	private SolrTemplate solrTemplate;
	
	@Test
	public void testAdd(){
		TbItem item=new TbItem();
		item.setId(1L);
		item.setBrand("华为");
		item.setCategory("手机");
		item.setGoodsId(1L);
		item.setSeller("华为2号专卖店");
		item.setTitle("华为Mate9");
		item.setPrice(new BigDecimal(2000));		
		solrTemplate.saveBean(item);
		solrTemplate.commit();
	}
	@Test
	public void testFindOne(){
		TbItem item = solrTemplate.getById(1L, TbItem.class);
		System.out.println(item.getTitle());
	}
	@Test
	public void detelebyid() {
		solrTemplate.deleteById("1");
		solrTemplate.commit();
	}
	@Test
	public void testAddlist() {
		List list=new ArrayList();
		for (int i = 0; i < 100; i++) {
			TbItem item=new TbItem();
			item.setId(i+1L);
			item.setBrand("华为"+i);
			item.setCategory("手机");
			item.setGoodsId(1L); 
			item.setSeller("华为2号专卖店");
			item.setTitle("华为Mate9");
			item.setPrice(new BigDecimal(2000+i));	
			list.add(item);
		}
		solrTemplate.saveBeans(list);
		solrTemplate.commit();
	}
	@Test
	public void testQuery() {
		
		Query query=new SimpleQuery("*:*");
		Criteria create=new Criteria("item_category");
		create.contains("手机");
		create.and("item_brand").contains("2");
		query.addCriteria(create);
		query.setOffset(1); 
		query.setRows(2);
		ScoredPage<TbItem> page = solrTemplate.queryForPage(query,TbItem.class);
		for (TbItem tbItem : page) {
			System.out.println(tbItem);
		}
	}
	@Test
	public void delete() { 
		Query query=new SimpleQuery("*:*");
		solrTemplate.delete(query);
		solrTemplate.commit();
	}
}
