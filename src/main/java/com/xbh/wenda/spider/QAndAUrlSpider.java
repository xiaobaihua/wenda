package com.xbh.wenda.spider;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.ram.RamCrawler;
import com.xbh.wenda.Main;
import com.xbh.wenda.bean.WdBean;

import java.util.regex.Pattern;

/**
 * @author xbh
 * @date 2019年7月28日14:48:23
 * @Description 问答页面爬去
 */
public class QAndAUrlSpider extends RamCrawler {

	static int l = 0;
	public QAndAUrlSpider() {
		setResumable(false);
		setThreads(500);
		getConf().setTopN(50000);
		conf.setExecuteInterval(500);
		setMaxExecuteCount(3);
	}

	@MatchType(types = "ksQA")
	public void ksQA(Page page, CrawlDatums next) {
		// 如果301 , 302就复制meta数据并且添加到下一个任务
		if (page.code() == 301 || page.code() == 302) {
			WdBean bean = Main.getBeanList().get(Integer.valueOf(page.meta("id")));
			final String s = page.location();
			if (Pattern.matches(".*120ask.com/question/[0-9].*", s)) {
				if (bean.getThreeUrl().size() < 10) {
					bean.getThreeUrl().add(page.location());
				}
			}
			l++;
			System.out.println(l);
		}
	}

	@Override
	public void visit(Page page, CrawlDatums next) {

	}
}
