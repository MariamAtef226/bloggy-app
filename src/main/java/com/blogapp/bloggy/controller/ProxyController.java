package com.blogapp.bloggy.controller;

import com.blogapp.bloggy.utils.InvoiceData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/v1/payment")
public class ProxyController {
    private final String authorization = "bearer rLtt6JWvbUHDDhsZnfpAhpYk4dxYDQkbcPTyGaKp2TYqQgG7FGZ5Th_WD53Oq8Ebz6A53njUoo1w3pjU1D4vs_ZMqFiz_j0urb_BH9Oq9VZoKFoJEDAbRZepGcQanImyYrry7Kt6MnMdgfG5jn4HngWoRdKduNNyP4kzcp3mRv7x00ahkm9LAK7ZRieg7k1PDAnBIOG3EyVSJ5kK4WLMvYr7sCwHbHcu4A5WwelxYK0GMJy37bNAarSJDFQsJ2ZvJjvMDmfWwDVFEVe_5tOomfVNt6bOg9mexbGjMrnHBnKnZR1vQbBtQieDlQepzTZMuQrSuKn-t5XZM7V6fCW7oP-uXGX-sMOajeX65JOf6XVpk29DP6ro8WTAflCDANC193yof8-f5_EYY-3hXhJj7RBXmizDpneEQDSaSz5sFk0sV5qPcARJ9zGG73vuGFyenjPPmtDtXtpx35A-BVcOSBYVIWe9kndG3nclfefjKEuZ3m4jL9Gg1h2JBvmXSMYiZtp9MR5I6pvbvylU_PP5xJFSjVTIz7IQSjcVGO41npnwIxRXNRxFOdIUHn0tjQ-7LwvEcTXyPsHXcMD8WtgBh-wxR8aKX7WPSsT1O8d8reb2aR7K3rkV3K82K_0OgawImEpwSvp9MNKynEAJQS6ZHe_J_l77652xwPNxMRTMASk1ZsJL";

    @PostMapping("/initiate-payment")
    public ResponseEntity<?> initiatePayment(@RequestBody String requestBody) {
        // Set the URL of the MyFatoorah API endpoint
        String apiUrl = "https://apitest.myfatoorah.com/v2/InitiateSession";

        // Create a RestTemplate instance
        RestTemplate restTemplate = new RestTemplate();

        // Set the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization",authorization);

        // Create an HttpEntity with the request body and headers
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        // Forward the request to the MyFatoorah API
        ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, requestEntity, String.class);

        // Add CORS headers to the response
        HttpHeaders responseHeaders = new HttpHeaders();
//        responseHeaders.add("Access-Control-Allow-Origin", "*"); // Allow requests from any origin
        // Add other CORS headers as needed

        // Return the response with CORS headers
        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(response.getBody());
    }

    @PostMapping("/execute-payment")
    public ResponseEntity<?> executePayment(@RequestBody InvoiceData requestBody) {
        // Set the URL of the MyFatoorah API endpoint

            String apiUrl = "https://apitest.myfatoorah.com/v2/ExecutePayment";

            // Create a RestTemplate instance
            RestTemplate restTemplate = new RestTemplate();

            // Set the request headers
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization",authorization);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Object> requestEntity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, requestEntity, String.class);
            return ResponseEntity.ok()
                    .body(response.getBody());

    }


}

