package org.mule.extension.customcustom.internal;

import static org.mule.runtime.extension.api.annotation.param.MediaType.ANY;


import org.mule.runtime.extension.api.annotation.param.MediaType;
//import org.mule.runtime.extension.api.annotation.param.Config;
//import org.mule.runtime.extension.api.annotation.param.Connection;

//import java.util.HashMap;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;



/**
 * This class is a container for operations, every public method in this class will be taken as an extension operation.
 */
public class CurrencyconversioncustomOperations {

  /**
   * Example of an operation that uses the configuration and a connection instance to perform some action.
   */
//  @MediaType(value = ANY, strict = false)
//  public String retrieveInfo(@Config CurrencyconversioncustomConfiguration configuration, @Connection CurrencyconversioncustomConnection connection){
//    return "Using Configuration [" + configuration.getConfigId() + "] with Connection id [" + connection.getId() + "]";
//  }

  /**
   * Example of a simple operation that receives a string parameter and returns a new string message that will be set on the payload.
   */



	@MediaType(value = ANY, strict = false)
	 public double convertCurrency(String sourceCurrencyCode, String targetCurrencyCode, double amount) {
        HashMap<String, Double> exchangeRates = getExchangeRatesFromAPI();

        if (exchangeRates.containsKey(sourceCurrencyCode) && exchangeRates.containsKey(targetCurrencyCode)) {
            double sourceRate = exchangeRates.get(sourceCurrencyCode);
            double targetRate = exchangeRates.get(targetCurrencyCode);

            double convertedAmount = (amount / sourceRate) * targetRate;
            return convertedAmount;
        } else {
            return 0.0; // or any other error handling
        }
    }
	private static final String EXCHANGE_RATE_API_URL = "https://v6.exchangerate-api.com/v6/b215534306962b44e019248c/latest/USD";

    private HashMap<String, Double> getExchangeRatesFromAPI() {
        try {
            URL url = new URL(EXCHANGE_RATE_API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                String responseBody = "";
                Scanner scanner = new Scanner(connection.getInputStream());
                while (scanner.hasNextLine()) {
                    responseBody += scanner.nextLine();
                }
                scanner.close();

                // Parse the response body to extract exchange rates
                HashMap<String, Double> exchangeRates = parseExchangeRates(responseBody);
                return exchangeRates;
            } else {
                // Handle API error response
                System.out.println("API request failed with response code: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new HashMap<String, Double>();
    }

    private HashMap<String, Double> parseExchangeRates(String responseBody) {
        HashMap<String, Double> exchangeRates = new HashMap<>();

        com.google.gson.JsonParser parser = new com.google.gson.JsonParser();
        com.google.gson.JsonElement jsonElement = parser.parse(responseBody);
        com.google.gson.JsonObject jsonObject = jsonElement.getAsJsonObject();
        com.google.gson.JsonObject ratesObject = jsonObject.getAsJsonObject("conversion_rates");

        for (String currencyCode : ratesObject.keySet()) {
            double exchangeRate = ratesObject.get(currencyCode).getAsDouble();
            exchangeRates.put(currencyCode, exchangeRate);
        }

        return exchangeRates;
    }
}
