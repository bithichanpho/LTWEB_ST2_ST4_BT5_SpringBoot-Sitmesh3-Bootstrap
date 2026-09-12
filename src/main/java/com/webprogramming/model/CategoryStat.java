package com.webprogramming.model;

import com.webprogramming.entity.Category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
 
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryStat {
	private Category category;
	private long productCount;
	private long totalSold;
	private double totalRevenue;
	private double revenuePercent;
}