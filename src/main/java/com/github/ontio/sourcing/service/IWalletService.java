package com.github.ontio.sourcing.service;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "feignWalletController", fallback = WalletService.class)
public interface IWalletService {

    void testFeign();
}
