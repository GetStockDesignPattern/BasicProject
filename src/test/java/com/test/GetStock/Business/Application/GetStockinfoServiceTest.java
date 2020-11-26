package com.test.GetStock.Business.Application;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.client.MockRestServiceServer;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.GetStock.Business.Domain.Profit;
import com.test.GetStock.Business.Domain.StockResponse;

import org.junit.jupiter.api.Assertions;

import org.springframework.boot.test.autoconfigure.web.client.*;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@AutoConfigureMockRestServiceServer
@SpringBootTest
class GetStockinfoServiceTest {

	private static StockResponse[] testDataarr;

	@Autowired
	private MockRestServiceServer mockRestServiceServer;

	@Autowired
	private GetStockinfoService getStockinfoService;

	@Autowired
	private ObjectMapper objectMapper;

	@BeforeAll
	static void setting() {
		testDataarr = new StockResponse[10];
		testDataarr[0] = new StockResponse("2020-09-01", 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		testDataarr[1] = new StockResponse("2020-09-02", 11, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		testDataarr[2] = new StockResponse("2020-09-03", 12, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		testDataarr[3] = new StockResponse("2020-09-04", 9, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		testDataarr[4] = new StockResponse("2020-09-05", 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		testDataarr[5] = new StockResponse("2020-09-06", 7, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		testDataarr[6] = new StockResponse("2020-09-07", 13, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		testDataarr[7] = new StockResponse("2020-09-08", 15, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		testDataarr[8] = new StockResponse("2020-09-09", 15, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		testDataarr[9] = new StockResponse("2020-09-10", 15, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
	}

	@Test
	@DisplayName("올바른 ticker가 들어온 경우 정보를 가져옴")
	public void GetStockInfo_WhengivenCorrectTicker() throws JsonProcessingException {
		String ticker = "CorrectTicker";
		StockResponse[] stockResponse = { new StockResponse(ticker, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0) };
		Date date = new Date();
		Calendar before180 = Calendar.getInstance();
		before180.add(Calendar.DATE, -180);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String enddate = format.format(date);
		String startdate = format.format(before180.getTime());
		String token = "de6162a413844946ee8c3535879d862ad97187fe";
		String url = String.format(
				"https://api.tiingo.com/tiingo/daily/" + ticker + "/prices?startDate=%s&endDate=%s&token=%s", startdate,
				enddate, token);
		mockRestServiceServer.expect(requestTo(url)).andExpect(method(HttpMethod.GET))
				.andRespond(withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
						.body(objectMapper.writeValueAsString(stockResponse)));

		// when
		ResponseEntity<StockResponse[]> responseEntity = getStockinfoService.GetStockInfoEntity(ticker);

		// then
		mockRestServiceServer.verify();
		Assertions.assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
		Assertions.assertEquals(stockResponse[0], responseEntity.getBody()[0]);
	}

	@Test
	@DisplayName("존재하지않는 ticker가 들어온 경우 정보를 가져옴")
	public void GetStockInfo_WhengivenWrongTicker() throws JsonProcessingException {
		String ticker = "WrongTicker";
		StockResponse[] stockResponse = null;
		Date date = new Date();
		Calendar before180 = Calendar.getInstance();
		before180.add(Calendar.DATE, -180);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String enddate = format.format(date);
		String startdate = format.format(before180.getTime());
		String token = "de6162a413844946ee8c3535879d862ad97187fe";
		String url = String.format(
				"https://api.tiingo.com/tiingo/daily/" + ticker + "/prices?startDate=%s&endDate=%s&token=%s", startdate,
				enddate, token);
		mockRestServiceServer.expect(requestTo(url)).andExpect(method(HttpMethod.GET))
				.andRespond(withStatus(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON)
						.body(objectMapper.writeValueAsString(stockResponse)));

		// when
		ResponseEntity<StockResponse[]> responseEntity = getStockinfoService.GetStockInfoEntity(ticker);

		// then
		mockRestServiceServer.verify();
		Assertions.assertTrue(responseEntity.getStatusCode().is4xxClientError());
		Assertions.assertEquals(stockResponse, responseEntity.getBody());
	}

	@Test
	@DisplayName("연속 상승경우")
	public void alwaysUpword() {
		StockResponse[] test = new StockResponse[3];
		for (int i = 0; i < test.length; i++) {
			test[i] = testDataarr[i];
		}
		Profit result = getStockinfoService.getprofit("testticker", test);
		assertEquals(2f, result.getMaxprofit());
		assertEquals("2020-09-01", result.getLowdate());
		assertEquals("2020-09-03", result.getHighdate());
	}

	@Test
	@DisplayName("연속 하강경우")
	public void alwaysDownword() {
		StockResponse[] test = new StockResponse[3];
		for (int i = 0; i < test.length; i++) {
			test[i] = testDataarr[i + 3];
		}
		Profit result = getStockinfoService.getprofit("testticker", test);
		assertEquals(0f, result.getMaxprofit());
		assertEquals("2020-09-06", result.getLowdate());
		assertEquals("2020-09-04", result.getHighdate());
	}

	@Test
	@DisplayName("변화가 없는경우")
	public void AlwaysEquals() {
		StockResponse[] test = new StockResponse[3];
		for (int i = 0; i < test.length; i++) {
			test[i] = testDataarr[i + 7];
		}
		Profit result = getStockinfoService.getprofit("testticker", test);
		assertEquals(0f, result.getMaxprofit());
		assertEquals("2020-09-08", result.getLowdate());
		assertEquals("2020-09-08", result.getHighdate());
	}

	@Test
	@DisplayName("최저가가 갱신되었지만 그 뒤에 최대 이익이 나지 않은 경우")
	public void renewmin() {
		StockResponse[] test = new StockResponse[6];

		String mostLowDate = testDataarr[0].getDate();
		float MostLowValue = testDataarr[0].getClose();
		for (int i = 0; i < test.length; i++) {
			test[i] = testDataarr[i];
			if (MostLowValue > test[i].getClose()) {
				MostLowValue = test[i].getClose();
				mostLowDate = test[i].getDate();
			}
		}
		Profit result = getStockinfoService.getprofit("testticker", test);
		assertEquals("2020-09-06", mostLowDate);
		assertEquals(2f, result.getMaxprofit());
		assertEquals("2020-09-01", result.getLowdate());
		assertEquals("2020-09-03", result.getHighdate());
	}

	@Test
	@DisplayName("최저가와 최고가가 갱신된 경우")
	public void renewMinandMax() {
		StockResponse[] test = new StockResponse[8];
		for (int i = 0; i < test.length; i++) {
			test[i] = testDataarr[i];
		}
		Profit result = getStockinfoService.getprofit("testticker", test);
		assertEquals(8f, result.getMaxprofit());
		assertEquals("2020-09-06", result.getLowdate());
		assertEquals("2020-09-08", result.getHighdate());
	}

	@Test
	@DisplayName("전체 테스트")
	public void allarr() {
		Profit result = getStockinfoService.getprofit("testticker", testDataarr);
		assertEquals(8f, result.getMaxprofit());
		assertEquals("2020-09-06", result.getLowdate());
		assertEquals("2020-09-08", result.getHighdate());
	}
}
