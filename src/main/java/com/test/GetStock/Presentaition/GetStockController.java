package com.test.GetStock.Presentaition;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.test.GetStock.Business.Application.GetStockinfoService;

@RestController
public class GetStockController {
	
	@Autowired
	private GetStockinfoService getStockinfoService;
	
	@GetMapping(path = "/getMaxProfit", produces="text/plain;charset=UTF-8")
	public String GetHello(@RequestParam String ticker) throws Exception {
		return getStockinfoService.GetmaxProfit(ticker);
	}

}
