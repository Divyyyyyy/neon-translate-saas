package com.example.smartexpense.controller;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin(origins = "*") 
public class PaymentController {

    // ⚠️ Inhe dashboard se badalna mat bhulna baad mein
    private final String KEY_ID = "rzp_test_YOUR_KEY_ID";
    private final String KEY_SECRET = "YOUR_KEY_SECRET";

    @PostMapping("/create-order")
    public String createOrder(@RequestBody Map<String, Object> data) throws RazorpayException {
        
        // Amount ko handle karne ke liye safe tarika
        int amount = Integer.parseInt(data.get("amount").toString());

        RazorpayClient client = new RazorpayClient(KEY_ID, KEY_SECRET);

        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", amount * 100); // INR to Paisa
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", "txn_languix_123");

        Order order = client.orders.create(orderRequest);

        return order.toString();
    }
}