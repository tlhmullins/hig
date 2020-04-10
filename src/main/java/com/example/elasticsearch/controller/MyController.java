package com.example.elasticsearch.controller;

import java.io.IOException;

import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

//import com.springboot.elasticsearch.Springbootelasticsearchtutorial.model.Customer;
//import com.springboot.elasticsearch.Springbootelasticsearchtutorial.service.Customerserv;

@RestController
//@RequestMapping(value="/customer")
public class MyController {
	@Autowired
	private RestHighLevelClient restHighLevelClient;

	@RequestMapping(value = { "/customerList" }, method = RequestMethod.GET)
	public String viewCustomerList(Model model) {
		String allCustomers;

		SearchRequest searchRequest = new SearchRequest();
		searchRequest.indices("tlm_customers");
		// or
		// SearchRequest searchRequest new SearchRequest("tlm_customers");

		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.matchAllQuery());

		searchRequest.source(searchSourceBuilder);

		try {
			SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
			allCustomers = searchResponse.toString();
		} catch (IOException e) {
			System.out.println("****error getting all customers: " + e.getMessage());
			allCustomers = "ERROR";
		}

		model.addAttribute("customers", allCustomers);

		return "customerList";
	}

	// @GetMapping(value= "/getall")
	@RequestMapping("/allCustomers")
	public String getAllCustomers() {
		String allCustomers;

		SearchRequest searchRequest = new SearchRequest();
		searchRequest.indices("tlm_customers");
		// or
		// SearchRequest searchRequest new SearchRequest("tlm_customers");

		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.matchAllQuery());

		searchRequest.source(searchSourceBuilder);

		try {
			SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
			allCustomers = searchResponse.toString();
		} catch (IOException e) {
			System.out.println("****error getting all customers: " + e.getMessage());
			allCustomers = "ERROR";
		}

		return allCustomers;
	}

	@RequestMapping("/test")
	public String test() {

		String hits = "";

		SearchRequest searchRequest = new SearchRequest();
		searchRequest.indices("tlm_customers");

		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.matchAllQuery());
		// number of search hits to return, default is 10
		searchSourceBuilder.size(1000);
		searchRequest.source(searchSourceBuilder);

		try {
			SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
			hits = String.valueOf(searchResponse.getHits().getHits().length);
		} catch (IOException e) {
			System.out.println("****error getting all customers: " + e.getMessage());

		}

		return hits;
	}

	@RequestMapping("/test2")
	public String test2() {

		String hits = "";

		SearchRequest searchRequest = new SearchRequest();
		searchRequest.indices("bank");

		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.matchAllQuery());
		// number of search hits to return, default is 10
		searchSourceBuilder.size(1000);
		searchRequest.source(searchSourceBuilder);

		try {
			SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
			hits = String.valueOf(searchResponse.getHits().getHits().length);
		} catch (IOException e) {
			System.out.println("****error getting all accounts: " + e.getMessage());

		}

		return hits;
	}

	@RequestMapping(value = "/match/{stateName}", produces = "application/json")
	public String match(@PathVariable String stateName) {
		String allCustomers;
		String hits = "";

		SearchRequest searchRequest = new SearchRequest();
		searchRequest.indices("tlm_customers");

		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(new MatchQueryBuilder("state", stateName));
		// number of search hits to return, default is 10
		searchSourceBuilder.size(1000);
		searchRequest.source(searchSourceBuilder);

		try {
			SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
			allCustomers = searchResponse.toString();
			hits = String.valueOf(searchResponse.getHits().getHits().length);
		} catch (IOException e) {
			System.out.println("****error getting all customers: " + e.getMessage());
			allCustomers = "ERROR";
		}

		// return new ResponseEntity<String> (allCustomers, HttpStatus.OK);

		// return hits + " customers in " + stateName + " : " + allCustomers;
		return allCustomers;
	}

	@RequestMapping(value = "/femaleCustomers", produces = "application/json")
	public String getFemaleCustomers() {
		String customersFound;
		RestHighLevelClient client = new RestHighLevelClient(
				RestClient.builder(new HttpHost("127.0.0.1", 9200, "http")));

		// SearchRequest searchRequest = new SearchRequest("tlm_customers");
		SearchRequest searchRequest = new SearchRequest();
		searchRequest.indices("tlm_customers");

		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

		BoolQueryBuilder qb = QueryBuilders.boolQuery();
		qb.must(QueryBuilders.matchQuery("gender", "female"));

		searchSourceBuilder.query(qb);

		searchRequest.source(searchSourceBuilder);

		try {
			SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
			customersFound = searchResponse.toString();
			client.close();
		} catch (IOException e) {
			System.out.println("****error getting all customers: " + e.getMessage());
			customersFound = "ERROR";
		}

		return customersFound;
	}

	@GetMapping(value = "/maleCustomers", produces = "application/json")
	public ResponseEntity<String> getMaleCustomers() {
		String customersFound;
		RestHighLevelClient client = new RestHighLevelClient(
				RestClient.builder(new HttpHost("127.0.0.1", 9200, "http")));

		// SearchRequest searchRequest = new SearchRequest("tlm_customers");
		SearchRequest searchRequest = new SearchRequest();
		searchRequest.indices("tlm_customers");

		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

		BoolQueryBuilder qb = QueryBuilders.boolQuery();
		qb.must(QueryBuilders.matchQuery("gender", "male"));

		searchSourceBuilder.query(qb);

		searchRequest.source(searchSourceBuilder);

		try {
			SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
			customersFound = searchResponse.toString();
			client.close();
		} catch (IOException e) {
			System.out.println("****error getting all customers: " + e.getMessage());
			customersFound = "ERROR";
		}

		return new ResponseEntity<String>(customersFound, HttpStatus.OK);
	}

//	@Autowired 
//	Customerserv cserv;
//	
//	/**
//     * Method to save the customers in the database.
//     * @param myemployees
//     * @return
//     */
//    @PostMapping(value= "/savecustomers")
//    public String saveCustomers(@RequestBody List<Customer> myCustomers) {
//        cserv.saveCustomer(myCustomers);
//        return "Records saved in the db.";
//    }
//    /**
//     * Method to fetch all customers from the database.
//     * @return
//     */
//    @GetMapping(value= "/getall")
//    public Iterable<Customer> getAllCustomers() {
//        return cserv.findAllCustomers();
//    }
//    
//    /**
//     * Method to fetch the customer details on the basis of designation.
//     * @param designation
//     * @return
//     */
//    @GetMapping(value= "/findbydesignation/{customer-designation}")
//    public Iterable<Customer> getByDesignation(@PathVariable(name= "customer-designation") String designation) {
//        return cserv.findByDesignation(designation);
//    }

}