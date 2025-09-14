package com.qraft.news.domain.customer.service;

import com.qraft.news.domain.customer.Customer;
import com.qraft.news.domain.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    public boolean validateTokenAndCheckConnection(String token) {
        if (token == null) {
            log.warn("빈 토큰으로 연결 시도");
            return false;
        }

        // 1. 토큰으로 고객 조회
        Customer customer = customerRepository.findByToken(token);

        if (customer==null) {
            log.warn("토큰 검증 실패 - 존재하지 않는 토큰: [{}]", token);
            return false;
        }

        // 2. 이미 연결되어 있는지 확인 (중복 연결 방지)
        if (customer.getIsActive()) {
            log.warn("중복 연결 시도 - 고객 [{}]은 이미 연결 중", customer.getCustomerName());
            return false;
        }

        log.info("토큰 검증 및 연결 가능 확인 성공 - Customer: [{}], Token: [{}]",
                customer.getCustomerName(), token);

        return true;
    }

    public void activateConnection(String token) {
        Customer customer = customerRepository.findByToken(token);

        if(customer != null) {
            customer.setIsActive(true);
            customerRepository.save(customer);
            log.info("고객 연결 활성화 - Customer: [{}], Token: [{}]",
                    customer.getCustomerName(), token);
        }
    }

    public void deactivateConnection(String token) {
        Customer customer = customerRepository.findByToken(token);

        if(customer != null) {
            customer.setIsActive(false);
            customerRepository.save(customer);
            log.info("고객 연결 비활성화 - Customer: [{}], Token: [{}]",
                    customer.getCustomerName(), token);
        }
    }

}
