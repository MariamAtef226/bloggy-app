package com.blogapp.bloggy.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class MyFatoorahInvoice {
        private String sessionId;
        private int invoiceValue;
        private String customerName;
        private String displayCurrencyIso;
        private String mobileCountryCode;
        private String customerMobile;
        private String customerEmail;
        private String callBackUrl;
        private String errorUrl;
        private String language;
        private String customerReference;
        private CustomerAddress customerAddress;
        private List<InvoiceItem> invoiceItems;



    }



