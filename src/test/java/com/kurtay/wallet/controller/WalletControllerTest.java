package com.kurtay.wallet.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.kurtay.wallet.domain.request.CreateWalletRequest;
import com.kurtay.wallet.domain.request.SearchWalletRequest;
import com.kurtay.wallet.domain.response.CreateWalletResponse;
import com.kurtay.wallet.domain.response.SearchWalletResponse;
import com.kurtay.wallet.service.WalletService;

@ExtendWith(MockitoExtension.class)
class WalletControllerTest {

    @InjectMocks
    private WalletController walletController;

    @Mock
    private WalletService walletService;

    @Test
    void createWallet_ShouldReturnCreatedResponse_WhenRequestIsValid() {
        CreateWalletRequest request = new CreateWalletRequest();
        CreateWalletResponse response = new CreateWalletResponse();

        when(walletService.createWallet(request)).thenReturn(response);

        ResponseEntity<CreateWalletResponse> result = walletController.createWallet(request);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(response, result.getBody());
        verify(walletService).createWallet(request);
    }

    @Test
    void searchWallets_ShouldReturnOkResponse_WhenRequestIsValid() {
        SearchWalletRequest request = new SearchWalletRequest();
        List<SearchWalletResponse> response = List.of(new SearchWalletResponse());

        when(walletService.searchWallets(request)).thenReturn(response);

        ResponseEntity<List<SearchWalletResponse>> result = walletController.searchWallets(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
        verify(walletService).searchWallets(request);
    }
}