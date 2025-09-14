package com.qraft.news.domain.customer.service;

import com.qraft.news.domain.customer.Customer;

public interface CustomerService {
    /**
     * 토큰 유효성 검증 및 중복 연결 체크
     *
     * 토큰 존재, 중복 연결
     *
     * @param token 고객사 전용 토큰
     * @return Customer
     */
    public boolean validateTokenAndCheckConnection(String token);

    /**
     * 고객 연결 활성화
     *
     * @param token 고객사 전용 토큰
     */
    public void activateConnection(String token);

    /**
     * 고객 연결 비활성화
     *
     * @param token 고객사 전용 토큰
     */
    public void deactivateConnection(String token);
}
