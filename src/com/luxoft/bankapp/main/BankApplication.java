package com.luxoft.bankapp.main;

import com.luxoft.bankapp.domain.Account;
import com.luxoft.bankapp.domain.Bank;
import com.luxoft.bankapp.domain.CheckingAccount;
import com.luxoft.bankapp.domain.Client;
import com.luxoft.bankapp.domain.Gender;
import com.luxoft.bankapp.domain.SavingAccount;
import com.luxoft.bankapp.exceptions.ClientExistsException;
import com.luxoft.bankapp.exceptions.NotEnoughFundsException;
import com.luxoft.bankapp.exceptions.OverdraftLimitExceededException;
import com.luxoft.bankapp.service.BankReport;
import com.luxoft.bankapp.service.BankService;

import java.util.Scanner;

public class BankApplication {
	
	private static Bank bank;
	
	public static void main(String[] args) {
		bank = new Bank();
		modifyBank();
		if (args.length == 0) {
			printBalance();
			BankService.printMaximumAmountToWithdraw(bank);
		} else {
			if ("-statistics".equals(args[0])) {
				Scanner scanner = new Scanner(System.in);
				while (scanner.hasNext()) {
					String command = scanner.nextLine();
					if ("exit".equals(command)) {
						return;
					} else if ("display statistic".equals(command)) {
						printStatistics();
					}
				}
			}
		}
	}
	
	private static void modifyBank() {
		Client client1 = new Client("John", Gender.MALE, "Bucuresti");
		Account account1 = new SavingAccount(1, 150);
		Account account2 = new CheckingAccount(2, 100, 20);
		client1.addAccount(account1);
		client1.addAccount(account2);

		Client client2 = new Client("Andrei", Gender.MALE, "Ploiesti");
		Account account3 = new CheckingAccount(3, 250, 25);
		Account account4 = new SavingAccount(4, 225);
		client2.addAccount(account3);
		client2.addAccount(account4);

		Client client3 = new Client("Raluca", Gender.FEMALE, "Ploiesti");
		Client client4 = new Client("Lucian", Gender.MALE, "Cluj");

		try {
		   BankService.addClient(bank, client1);
		   BankService.addClient(bank, client2);
		   BankService.addClient(bank, client3);
		   BankService.addClient(bank, client4);
		} catch(ClientExistsException e) {
			System.out.format("Cannot add an already existing client: %s%n", client1.getName());
		}

		account1.deposit(100);
		try {
		  account1.withdraw(10);
		  account3.withdraw(274);
		} catch (OverdraftLimitExceededException e) {
	    	System.out.format("Not enough funds for account %d, balance: %.2f, overdraft: %.2f, tried to extract amount: %.2f%n", e.getId(), e.getBalance(), e.getOverdraft(), e.getAmount());
	    } catch (NotEnoughFundsException e) {
	    	System.out.format("Not enough funds for account %d, balance: %.2f, tried to extract amount: %.2f%n", e.getId(), e.getBalance(), e.getAmount());
	    }
		
		try {
		  account2.withdraw(90);
		  account2.withdraw(20);
		} catch (OverdraftLimitExceededException e) {
	      System.out.format("Not enough funds for account %d, balance: %.2f, overdraft: %.2f, tried to extract amount: %.2f%n", e.getId(), e.getBalance(), e.getOverdraft(), e.getAmount());
	    } catch (NotEnoughFundsException e) {
	      System.out.format("Not enough funds for account %d, balance: %.2f, tried to extract amount: %.2f%n", e.getId(), e.getBalance(), e.getAmount());
	    }

		try {
		  account2.withdraw(100);
		} catch (OverdraftLimitExceededException e) {
	      System.out.format("Not enough funds for account %d, balance: %.2f, overdraft: %.2f, tried to extract amount: %.2f%n", e.getId(), e.getBalance(), e.getOverdraft(), e.getAmount());
	    } catch (NotEnoughFundsException e) {
	      System.out.format("Not enough funds for account %d, balance: %.2f, tried to extract amount: %.2f%n", e.getId(), e.getBalance(), e.getAmount());
	    }
		
		try {
		  BankService.addClient(bank, client1);
		} catch(ClientExistsException e) {
		  System.out.format("Cannot add an already existing client: %s%n", client1);
	    }
	}
	
	private static void printBalance() {
		System.out.format("%nPrint balance for all clients%n");
		for(Client client : bank.getClients()) {
			System.out.println("Client: " + client);
			for (Account account : client.getAccounts()) {
				System.out.format("Account %d : %.2f%n", account.getId(), account.getBalance());
			}
		}
	}

	private static void printStatistics() {
		System.out.format("%nPrint bank statistics%n");
		BankReport bankReport = new BankReport(bank);
		System.out.format("Number of clients in the bank is: %d%n", bankReport.getNumberOfClients());
		System.out.format("Number of accounts in the bank is: %d%n", bankReport.getNumberOfAccounts());
		System.out.format("Sorted list of clients is: %s%n", bankReport.getClientsSorted().toString());
		System.out.format("The total sum in accounts is: %.2f%n", bankReport.getTotalSumInAccounts());
		System.out.format("Sorted list of accounts is: %s%n", bankReport.getAccountsSortedBySum());
		System.out.format("Bank credit sum is: %.2f%n", bankReport.getBankCreditSum());
		System.out.format("Customer accounts are: %s%n", bankReport.getCustomerAccounts());
		System.out.format("Clients by cities: %s%n", bankReport.getClientsByCity());
	}
}
