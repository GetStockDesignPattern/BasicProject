package com.test.GetStock.Business.Domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Profit {
	
	String ticker;
	String lowdate;
	String highdate;
	float maxprofit;
	String etc;
	
	public Profit(String ticker, String etc) {
		super();
		this.ticker = ticker;
		this.etc = etc;
	}
}
