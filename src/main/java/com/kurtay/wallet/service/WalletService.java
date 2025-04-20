package com.kurtay.wallet.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kurtay.wallet.domain.entity.Transaction;
import com.kurtay.wallet.domain.entity.Wallet;
import com.kurtay.wallet.domain.request.CreateWalletRequest;
import com.kurtay.wallet.domain.request.SearchWalletRequest;
import com.kurtay.wallet.domain.request.Transact;
import com.kurtay.wallet.domain.response.CreateWalletResponse;
import com.kurtay.wallet.domain.response.SearchWalletResponse;
import com.kurtay.wallet.mapper.WalletMapper;
import com.kurtay.wallet.repository.WalletRepository;
import com.kurtay.wallet.repository.WalletSpecifications;
import com.kurtay.wallet.util.SecurityUtility;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;
    private final WalletMapper walletMapper;

    @Transactional
    public CreateWalletResponse createWallet(CreateWalletRequest request) {
        validateWalletCreation(request);
        Wallet wallet = walletMapper.mapToWallet(request);

        Wallet savedWallet = walletRepository.save(wallet);

        return walletMapper.mapToCreateResponse(savedWallet);
    }

    protected void validateWalletCreation(CreateWalletRequest request) {
        Optional<Wallet> existingWallet = walletRepository.findByName(request.getWalletName());
        if (existingWallet.isPresent()) {
            throw new IllegalArgumentException("Wallet with name " + request.getWalletName() + " already exists.");
        }
    }

    @Transactional(readOnly = true)
    public List<SearchWalletResponse> searchWallets(SearchWalletRequest searchWalletRequest) {
        SecurityUtility.checkUserHasAccessToWallets(searchWalletRequest.getTckn());

        Specification<Wallet> spec = Specification.where(WalletSpecifications.tcknEquals(searchWalletRequest.getTckn()))
            .and(WalletSpecifications.balanceEquals(searchWalletRequest.getBalance()))
            .and(WalletSpecifications.currencyEquals(searchWalletRequest.getCurrency()));

        List<Wallet> wallets = walletRepository.findAll(spec);

        return walletMapper.mapToSearchResponses(wallets);
    }

    public Wallet findWallet(Long walletId) {
        return walletRepository.findById(walletId)
            .orElseThrow(() -> new IllegalArgumentException("Wallet not found with id: " + walletId));
    }

    @Transactional
    public void depositApprovedAmount(Transact transact) {
        Wallet wallet = findWallet(transact.getWalletId());
        wallet.setBalance(wallet.getBalance().add(transact.getAmount()));
        wallet.setUsableBalance(wallet.getUsableBalance().add(transact.getAmount()));
    }

    @Transactional
    public void approveDeposit(Transaction transaction) {
        Wallet wallet = findWallet(transaction.getWallet().getId());
        wallet.setUsableBalance(wallet.getUsableBalance().add(transaction.getAmount()));
    }

    @Transactional
    public void denyDeposit(Transaction transaction) {
        Wallet wallet = findWallet(transaction.getWallet().getId());
        wallet.setBalance(wallet.getBalance().subtract(transaction.getAmount()));
    }

    @Transactional
    public void depositPendingAmount(Transact transact) {
        Wallet wallet = findWallet(transact.getWalletId());
        wallet.setBalance(wallet.getBalance().add(transact.getAmount()));
    }

    @Transactional
    public void withdrawApprovedAmount(Transact transact) {
        Wallet wallet = findWallet(transact.getWalletId());
        wallet.setBalance(wallet.getBalance().subtract(transact.getAmount()));
        wallet.setUsableBalance(wallet.getUsableBalance().subtract(transact.getAmount()));
    }

    @Transactional
    public void approveWithdraw(Transaction transaction) {
        Wallet wallet = findWallet(transaction.getWallet().getId());
        wallet.setBalance(wallet.getBalance().subtract(transaction.getAmount()));
    }

    @Transactional
    public void denyWithdraw(Transaction transaction) {
        Wallet wallet = findWallet(transaction.getWallet().getId());
        wallet.setUsableBalance(wallet.getUsableBalance().add(transaction.getAmount()));
    }

    @Transactional
    public void withdrawPendingAmount(Transact transact) {
        Wallet wallet = findWallet(transact.getWalletId());
        wallet.setUsableBalance(wallet.getBalance().subtract(transact.getAmount()));
    }
}
