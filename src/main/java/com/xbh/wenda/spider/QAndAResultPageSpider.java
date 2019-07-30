package com.xbh.wenda.spider;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.ram.RamCrawler;
import com.xbh.wenda.Main;
import com.xbh.wenda.assemble.Assemble;
import com.xbh.wenda.bean.WdBean;

/**
 * @author xbh
 * @date 2019年7月28日15:03:37
 * @Description 获取问答最终页面
 */
public class QAndAResultPageSpider extends RamCrawler {
	static long i = 0;
	static Assemble assemble = new Assemble();

	public QAndAResultPageSpider() {
		setThreads(100);
		setResumable(false);
		getConf().setExecuteInterval(1000);
		getConf().setConnectTimeout(10000);
		conf.setReadTimeout(10000);
	}

	@MatchType(types = "ksPage")
	public void ksPage(Page page, CrawlDatums next) {
		if (page.code() == 301 || page.code() == 302) {
			next.addAndReturn(page.location()).meta(page.meta());
			return;
		}

		final WdBean bean = Main.getBeanList().get(Integer.valueOf(page.meta("id")));

		String issue = bean.getIssue();
		String result = bean.getResult();
		if (issue == null || issue.length() < 50 || issue.length() > 80) {
			assemble.assembleOne(bean, page);
		}

		i++;
		System.out.println(i);
	}

	@Override
	public void visit(Page page, CrawlDatums next) {

	}
}
