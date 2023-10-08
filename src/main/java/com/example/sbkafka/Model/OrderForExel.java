package com.example.sbkafka.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderForExel {

	private int ordernumber;//номер заказа
	private String bsnumber;//номер БС
	private String datestart;//дата начала работ
	private String dateend;//дата окончания работ
	private double calc;//стоимость без НДС
	private double calcnds;//стоимость с НДС
	private String comm;//комментарий
	private String bsaddress;//адрес БС
	
}
