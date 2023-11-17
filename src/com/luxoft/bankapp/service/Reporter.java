package com.luxoft.bankapp.service;

import com.luxoft.bankapp.domain.Account;
import com.luxoft.bankapp.domain.Client;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface Reporter {
    int getNumberOfClients();
    int getNumberOfAccounts();
    Set<Client> getClientsSorted();
    double getTotalSumInAccounts();
    Set<Account> getAccountsSortedBySum();
    double getBankCreditSum();
    Map<Client, Collection<Account>> getCustomerAccounts();
    Map<String, List<Client>> getClientsByCity();
}
