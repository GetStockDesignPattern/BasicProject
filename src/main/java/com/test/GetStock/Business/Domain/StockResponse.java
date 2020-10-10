package com.test.GetStock.Business.Domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StockResponse {
	// :"2019-01-02T00:00:00.000Z",
	@JsonProperty(value = "date")
	String date;
	@JsonProperty(value = "close")
	float close;
	@JsonProperty(value = "high")
	float high;
	@JsonProperty(value = "low")
	float low;
	@JsonProperty(value = "open")
	float open;
	@JsonProperty(value = "volume")
	int volume;
	@JsonProperty(value = "adjClose")
	float adjClose;
	@JsonProperty(value = "adjHigh")
	float adjHigh;
	@JsonProperty(value = "adjLow")
	float adjLow;
	@JsonProperty(value = "adjOpen")
	float adjOpen;
	@JsonProperty(value = "adjVolume")
	int adjVolume;
	@JsonProperty(value = "divCash")
	float divCash;
	@JsonProperty(value = "splitFactor")
	float splitFactor;
	@Override
	public String toString() {
		return "Stock [date=" + date + ", close=" + close + ", high=" + high + ", low=" + low + ", open=" + open
				+ ", volume=" + volume + ", adjClose=" + adjClose + ", adjHigh=" + adjHigh + ", adjLow=" + adjLow
				+ ", adjOpen=" + adjOpen + ", adjVolume=" + adjVolume + ", divCash=" + divCash + ", splitFactor="
				+ splitFactor + "]";
	}
	
	
}
