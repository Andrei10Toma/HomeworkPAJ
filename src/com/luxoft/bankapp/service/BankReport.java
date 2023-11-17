package com.luxoft.bankapp.service;

import com.luxoft.bankapp.domain.Account;
import com.luxoft.bankapp.domain.Bank;
import com.luxoft.bankapp.domain.CheckingAccount;
import com.luxoft.bankapp.domain.Client;

import java.util.*;

public class BankReport implements Reporter {
    private final Bank bank;

    public BankReport(Bank bank) {
        this.bank = bank;
    }

    public int getNumberOfClients() {
        return bank.getClients() != null ? bank.getClients().size() : 0;
    }

    public int getNumberOfAccounts() {
        Set<Client> clients = bank.getClients();
        if (clients == null)
            return 0;

        int accountsCounter = 0;
        for (Client client : clients) {
            Set<Account> clientAccounts = client.getAccounts();
            if (clientAccounts == null)
                continue;

            accountsCounter += clientAccounts.size();
        }

        return accountsCounter;
    }

    public Set<Client> getClientsSorted() {
        Set<Client> clients = bank.getClients();
        if (clients == null)
            return null;

        Set<Client> sortedClients = new TreeSet<>(new Comparator<Client>() {
            @Override
            public int compare(Client o1, Client o2) {
                if (o1.getName() == null)
                    return 1;

                return o1.getName().compareTo(o2.getName());
            }
        });

        sortedClients.addAll(clients);
        return sortedClients;
    }

    public double getTotalSumInAccounts() {
        Set<Client> clients = bank.getClients();
        if (clients == null)
            return 0;

        double sum = 0;
        for (Client client : clients) {
            Set<Account> accounts = client.getAccounts();
            if (accounts == null)
                continue;

            for (Account account : accounts)
                sum += account.getBalance();
        }

        return sum;
    }

    public Set<Account> getAccountsSortedBySum() {
        Set<Client> clients = bank.getClients();
        if (clients == null)
            return null;

        Set<Account> sortedAccounts = new TreeSet<>(new Comparator<Account>() {
            @Override
            public int compare(Account o1, Account o2) {
                return (int) (o1.getBalance() - o2.getBalance());
            }
        });

        for (Client client : clients) {
            Set<Account> accounts = client.getAccounts();
            if (accounts == null)
                continue;

            sortedAccounts.addAll(accounts);
        }

        return sortedAccounts;
    }

    public double getBankCreditSum() {
        Set<Client> clients = bank.getClients();
        if (clients == null)
            return 0;

        double bankCredit = 0;
        for (Client client : clients) {
            Set<Account> accounts = client.getAccounts();
            if (accounts == null)
                continue;

            for (Account account : accounts) {
                if (account instanceof CheckingAccount) {
                    if (account.getBalance() < 0) {
                        bankCredit -= account.getBalance();
                    }
                }
            }
        }

        return bankCredit;
    }

    public Map<Client, Collection<Account>> getCustomerAccounts() {
        Set<Client> clients = bank.getClients();
        if (clients == null)
            return null;

        Map<Client, Collection<Account>> clientAccounts = new HashMap<>();
        for (Client client : clients) {
            Set<Account> accounts = client.getAccounts();
            if (accounts == null)
                clientAccounts.put(client, Collections.emptySet());

            clientAccounts.put(client, accounts);
        }

        return clientAccounts;
    }

    public Map<String, List<Client>> getClientsByCity() {
        Set<Client> clients = bank.getClients();
        if (clients == null)
            return Collections.emptyMap();

        Map<String, List<Client>> cityClients = new TreeMap<>();

        for (Client client : clients) {
            String clientCity = client.getCity();
            if (clientCity == null || clientCity.isEmpty())
                continue;
            if (cityClients.containsKey(clientCity)) {
                cityClients.get(clientCity).add(client);
            } else {
                List<Client> newClients = new ArrayList<>();
                newClients.add(client);
                cityClients.put(clientCity, newClients);
            }
        }

        return cityClients;
    }
}
