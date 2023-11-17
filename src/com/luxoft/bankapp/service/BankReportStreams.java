package com.luxoft.bankapp.service;

import com.luxoft.bankapp.domain.Account;
import com.luxoft.bankapp.domain.Bank;
import com.luxoft.bankapp.domain.CheckingAccount;
import com.luxoft.bankapp.domain.Client;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BankReportStreams implements Reporter {
    private final Bank bank;

    public BankReportStreams(Bank bank) {
        this.bank = bank;
    }

    @Override
    public int getNumberOfClients() {
        if (bank.getClients() == null)
            return 0;
        return (int) bank.getClients().stream().count();
    }

    @Override
    public int getNumberOfAccounts() {
        if (bank.getClients() == null)
            return 0;

        return (int) bank.getClients().stream()
                .flatMap(client -> client.getAccounts().stream())
                .filter(Objects::nonNull)
                .count();
    }

    @Override
    public Set<Client> getClientsSorted() {
        if (bank.getClients() == null)
            return Collections.emptySet();

        return bank.getClients().stream()
                .sorted((c1, c2) -> {
                    if (c1.getName() == null)
                        return 1;
                    return c1.getName().compareTo(c2.getName());
                })
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public double getTotalSumInAccounts() {
        if (bank.getClients() == null)
            return 0;

        return bank.getClients().stream()
                .flatMap(client -> client.getAccounts().stream())
                .filter(Objects::nonNull)
                .mapToDouble(Account::getBalance)
                .sum();
    }

    @Override
    public Set<Account> getAccountsSortedBySum() {
        if (bank.getClients() == null)
            return Collections.emptySet();

        return bank.getClients().stream()
                .flatMap(client -> client.getAccounts().stream())
                .filter(Objects::nonNull)
                .sorted(Comparator.comparingDouble(Account::getBalance))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public double getBankCreditSum() {
        if (bank.getClients() == null)
            return 0;

        return -bank.getClients().stream()
                .flatMap(client -> client.getAccounts().stream())
                .filter(Objects::nonNull)
                .filter(acc -> acc instanceof CheckingAccount && acc.getBalance() < 0)
                .mapToDouble(Account::getBalance)
                .sum();
    }

    @Override
    public Map<Client, Collection<Account>> getCustomerAccounts() {
        if (bank.getClients() == null)
            return Collections.emptyMap();

        return bank.getClients().stream()
                .collect(Collectors.toMap(Function.identity(), Client::getAccounts));
    }

    @Override
    public Map<String, List<Client>> getClientsByCity() {
        if (bank.getClients() == null)
            return Collections.emptyMap();

        return bank.getClients().stream()
                .filter(client -> client.getCity() != null && !client.getCity().isEmpty())
                .collect(Collectors.groupingBy(
                        Client::getCity,
                        TreeMap::new,
                        Collectors.mapping(
                                Function.identity(),
                                Collectors.toList()
                        )
                ));
    }
}
