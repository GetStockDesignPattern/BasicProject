package com.test.GetStock.Business.Application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.GetStock.Business.Domain.Profit;
import com.test.GetStock.Business.Domain.StockResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetStockinfoService {
	private final RestTemplate restTemplate;
	private final HttpEntity<String> httpEntity;

	@Autowired
	public GetStockinfoService(RestTemplateBuilder restTemplateBuilder) {
		// TODO:
		this.restTemplate = restTemplateBuilder.build();
		final HttpHeaders headers = new HttpHeaders();
		this.httpEntity = new HttpEntity<>(headers);
	}

	public String GetmaxProfit(String ticker) throws JsonProcessingException {
		log.info("ticker: {}", ticker);

		List<Profit> list = new LinkedList<Profit>();
		String[] tickerArr = ticker.split(",");

		for (int i = 0; i < tickerArr.length; i++) {
			String curTicker = tickerArr[i];
			log.info("ticker: {}", curTicker);
			ResponseEntity<StockResponse[]> entity = GetStockInfoEntity(curTicker);
			StockResponse[] arr = entity.getBody();
			if (arr != null) {
				list.add(getprofit(curTicker, arr));
			} else {
				list.add(new Profit(curTicker, "There is no result about " + curTicker));
			}
		}
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(list);
	}

	public ResponseEntity<StockResponse[]> GetStockInfoEntity(String curTicker) {
		Date date = new Date();
		Calendar before180 = Calendar.getInstance();
		before180.add(Calendar.DATE, -180);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String enddate = format.format(date);
		String startdate = format.format(before180.getTime());
		String token = "de6162a413844946ee8c3535879d862ad97187fe";
		String url = String.format("https://api.tiingo.com/tiingo/daily/%s/prices?startDate=%s&endDate=%s&token=%s",
				curTicker, startdate, enddate, token);
		try {
			return restTemplate.exchange(url, HttpMethod.GET, httpEntity, StockResponse[].class, curTicker);
		} catch (HttpStatusCodeException exception) {
			log.info("No result about {}", curTicker);
			return ResponseEntity.status(exception.getStatusCode()).body(null);
		}
	}

	public Profit getprofit(String ticker, StockResponse[] arr) {
		String lowdate = arr[0].getDate();
		String highdate = arr[0].getDate();
		String curlowdate = lowdate;
		float low = arr[0].getClose();
		float high = arr[0].getClose();
		float result = high - low;
		for (int i = 1; i < arr.length; i++) {
			if (high < arr[i].getClose()) {
				high = arr[i].getClose();
				if (result < high - low) {
					result = high - low;
					lowdate = curlowdate;
					highdate = arr[i].getDate();
				}
			}
			if (low > arr[i].getClose()) {
				low = arr[i].getClose();
				curlowdate = arr[i].getDate();
				high = arr[i].getClose();
			}
		}
		// 이익이 나지 않는다는 것은 시작일부터 끝일까지 동일하거나 중간에 올라가는 것 없는 하향세일경우임
		String etc = "";
		if (result == 0f) {
			etc = "You can never get profit in 180 days.";
			if (lowdate.equals(curlowdate)) {
				lowdate = highdate;
			} else
				lowdate = curlowdate;
		}
		return new Profit(ticker, lowdate.split("T")[0], highdate.split("T")[0], result, etc);
	}

}
