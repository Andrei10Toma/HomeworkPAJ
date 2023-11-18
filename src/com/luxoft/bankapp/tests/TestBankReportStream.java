package com.luxoft.bankapp.tests;

import com.luxoft.bankapp.domain.*;
import com.luxoft.bankapp.email.EmailService;
import com.luxoft.bankapp.exceptions.ClientExistsException;
import com.luxoft.bankapp.service.BankReportStreams;
import com.luxoft.bankapp.service.Reporter;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

public class TestBankReportStream {
    private final List<Client> sortedClientsRef = new ArrayList<>();
    private final List<Account> sortedAccountsRef = new ArrayList<>();
    private final Map<Client, Collection<Account>> customerAccountsRef = new HashMap<>();
    private final Map<String, List<Client>> clientsByCityRef = new TreeMap<>();

    private Bank computeBank() {
        Bank bank = new Bank();
        EmailService emailService = new EmailService();
        bank.setEmailService(emailService);

        Client client1 = new Client("Andrei", Gender.MALE, "Ploiesti");
        Client client2 = new Client("Raluca", Gender.FEMALE, "Ploiesti");
        Client client3 = new Client("Lucian", Gender.MALE, "Bucuresti");
        Client client4 = new Client("Vlad", Gender.MALE, "Cluj");
        Client client5 = new Client("Ioana", Gender.FEMALE, "Bucuresti");
        sortedClientsRef.addAll(Arrays.asList(
                client1,
                client5,
                client3,
                client2,
                client4
        ));

        Account account1 = new SavingAccount(1, 160);
        Account account2 = new CheckingAccount(2, 100, 20);
        client1.addAccount(account1);
        client1.addAccount(account2);

        Account account3 = new CheckingAccount(3, 250, 25);
        Account account4 = new SavingAccount(4, 225);
        client2.addAccount(account3);
        client2.addAccount(account4);

        Account account5 = new CheckingAccount(5, -10, 20);
        client3.addAccount(account5);

        Account account6 = new SavingAccount(6, 150);
        client4.addAccount(account6);

        Account account7 = new SavingAccount(7, 450);
        Account account8 = new CheckingAccount(8, -25, 50);
        client5.addAccount(account7);
        client5.addAccount(account8);

        sortedAccountsRef.addAll(Arrays.asList(
                account8,
                account5,
                account2,
                account6,
                account1,
                account4,
                account3,
                account7
        ));

        customerAccountsRef.put(client1, client1.getAccounts());
        customerAccountsRef.put(client2, client2.getAccounts());
        customerAccountsRef.put(client3, client3.getAccounts());
        customerAccountsRef.put(client4, client4.getAccounts());
        customerAccountsRef.put(client5, client5.getAccounts());

        clientsByCityRef.put("Ploiesti", Arrays.asList(client1, client2));
        clientsByCityRef.put("Bucuresti", Arrays.asList(client3, client5));
        clientsByCityRef.put("Cluj", Collections.singletonList(client4));

        try {
            bank.addClient(client1);
            bank.addClient(client2);
            bank.addClient(client3);
            bank.addClient(client4);
            bank.addClient(client5);
        } catch (ClientExistsException e) {
            e.printStackTrace();
        }

        return bank;
    }

    @Test
    public void testGetNumberOfClients() {
        Bank bank = computeBank();
        Reporter reporter = new BankReportStreams(bank);

        assertEquals(5, reporter.getNumberOfClients());
    }

    @Test
    public void testGetNumberOfAccounts() {
        Bank bank = computeBank();
        Reporter reporter = new BankReportStreams(bank);

        assertEquals(8, reporter.getNumberOfAccounts());
    }

    @Test
    public void testGetClientsSorted() {
        Bank bank = computeBank();
        Reporter reporter = new BankReportStreams(bank);

        Set<Client> clientsSorted = reporter.getClientsSorted();
        assertEquals(sortedClientsRef, clientsSorted.stream().toList());
    }

    @Test
    public void testGetTotalSumInAccounts() {
        Bank bank = computeBank();
        Reporter reporter = new BankReportStreams(bank);

        assertEquals(1300, reporter.getTotalSumInAccounts(), 1e-3);
    }

    @Test
    public void testGetAccountsSortedBySum() {
        Bank bank = computeBank();
        Reporter reporter = new BankReportStreams(bank);

        Set<Account> accountsSortedBySum = reporter.getAccountsSortedBySum();
        assertEquals(sortedAccountsRef, accountsSortedBySum.stream().toList());
    }

    @Test
    public void testGetBankCreditSum() {
        Bank bank = computeBank();
        Reporter reporter = new BankReportStreams(bank);

        assertEquals(35, reporter.getBankCreditSum(), 1e-3);
    }

    @Test
    public void testGetCustomerAccounts() {
        Bank bank = computeBank();
        Reporter reporter = new BankReportStreams(bank);

        assertEquals(customerAccountsRef, reporter.getCustomerAccounts());
    }

    @Test
    public void testGetClientsByCity() {
        Bank bank = computeBank();
        Reporter reporter = new BankReportStreams(bank);

        Map<String, List<Client>> clientsByCity = reporter.getClientsByCity();
        assertEquals(clientsByCityRef.keySet(), clientsByCity.keySet());
        assertEquals(clientsByCityRef.get("Ploiesti").size(), 2);
        assertEquals(clientsByCity.get("Bucuresti").size(), 2);
        assertEquals(clientsByCity.get("Cluj").size(), 1);
    }
}
