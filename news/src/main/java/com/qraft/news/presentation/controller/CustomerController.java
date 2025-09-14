package com.qraft.news.presentation.controller;

import com.qraft.news.domain.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class CustomerController {
    public final CustomerService customerService;
}
