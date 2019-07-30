package com.xbh.wenda.spider;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.ram.RamCrawler;
import com.xbh.wenda.Main;
import com.xbh.wenda.bean.WdBean;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author xbh
 * @date 2019年7月28日10:28:20
 * @Description 百度相关爬虫
 */
public class BaiduResultUrlSpider extends RamCrawler {
	private static Integer i = 0;

	public BaiduResultUrlSpider() {
		setResumable(false);
		setThreads(500);
		getConf().setTopN(50000);
		conf.setExecuteInterval(500);
		conf.setMaxExecuteCount(10000);
		conf.setConnectTimeout(1000);
		setMaxExecuteCount(5);
	}

	@MatchType(types = "ksBaidu")
	public void visitOne(Page page, CrawlDatums next) {
		final Integer id = Integer.valueOf(page.meta("id"));
		final WdBean bean = Main.getBeanList().get(id);

		i++;
		Elements s = page.select("div.result");
		for (Element element : s.select("a.c-showurl")) {
			bean.getTowUrl().add(element.attr("href"));
		}
	}

	@Override
	public void visit(Page page, CrawlDatums next) {

	}
}
