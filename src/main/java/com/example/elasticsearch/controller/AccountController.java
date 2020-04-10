package com.example.elasticsearch.controller;

import java.io.IOException;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
public class AccountController {

	@Autowired
	private RestHighLevelClient restHighLevelClient;

	@RequestMapping("/allAccounts")
	public String getAllAccounts() {
		String allAccounts;
		SearchHits results;

		SearchRequest searchRequest = new SearchRequest();
		searchRequest.indices("bank");

		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.matchAllQuery());

		searchRequest.source(searchSourceBuilder);

		try {
			SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
			// allAccounts = searchResponse.toString();
			results = searchResponse.getHits();

			JSONObject jo = new JSONObject();
			JSONArray ja = new JSONArray();

			results.forEach((hit) -> {
				System.out.println(hit.getSourceAsString());
				ja.put(hit.getSourceAsString());
			});

			jo.put("results", ja);

			allAccounts = jo.toString();
		} catch (IOException e) {
			System.out.println("****error getting all accounts: " + e.getMessage());
			return allAccounts = "ERROR";
		}

		return allAccounts;
	}

	@RequestMapping(value = "/match/{stateName}", produces = "application/json")
	public String match(@PathVariable String stateName) {

		String hits = "";
		String allAccounts;
		SearchHits results;

		SearchRequest searchRequest = new SearchRequest();
		searchRequest.indices("bank");

		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(new MatchQueryBuilder("state", stateName));
		// number of search hits to return, default is 10
		searchSourceBuilder.size(1000);
		searchRequest.source(searchSourceBuilder);

		try {
			SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
			allAccounts = searchResponse.toString();
			hits = String.valueOf(searchResponse.getHits().getHits().length);
			System.out.println("Accounts with state = " + stateName + " = " + hits);

			results = searchResponse.getHits();

			JSONObject jo = new JSONObject();
			JSONArray ja = new JSONArray();

			results.forEach((hit) -> {
				System.out.println(hit.getSourceAsString());
				ja.put(hit.getSourceAsString());
			});

			jo.put("results", ja);

			allAccounts = jo.toString();

		} catch (IOException e) {
			System.out.println("****error getting all customers: " + e.getMessage());
			allAccounts = "ERROR";
		}

		// return new ResponseEntity<String> (allAcounts, HttpStatus.OK);

		// return hits + " customers in " + stateName + " : " + allAcounts;
		return allAccounts;
	}

	@RequestMapping(value = { "/accountList" }, method = RequestMethod.GET)
	public String viewAccountList(Model model) {
		String allAccounts;

		SearchRequest searchRequest = new SearchRequest();
		searchRequest.indices("bank");
		// or
		// SearchRequest searchRequest new SearchRequest("bank");

		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.matchAllQuery());

		searchRequest.source(searchSourceBuilder);

		try {
			SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
			allAccounts = searchResponse.toString();
		} catch (IOException e) {
			System.out.println("****error getting all accounts: " + e.getMessage());
			allAccounts = "ERROR";
		}

		model.addAttribute("accounts", allAccounts);

		return "accountList";
	}
}
