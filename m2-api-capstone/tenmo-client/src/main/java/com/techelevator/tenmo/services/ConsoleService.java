package com.techelevator.tenmo.services;


import com.techelevator.tenmo.auth.models.AuthenticatedUser;
import com.techelevator.tenmo.auth.models.User;
import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.Account;

import javax.validation.constraints.NegativeOrZero;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Scanner;

public class ConsoleService {

	private PrintWriter out;
	private Scanner in;

	public ConsoleService(InputStream input, OutputStream output) {
		this.out = new PrintWriter(output, true);
		this.in = new Scanner(input);
	}

	public void showWelcomeBanner() {
		out.println("*********************");
		out.println("* Welcome to TEnmo! *");
		out.println("*********************");
		out.flush();
	}

	public void displayMessage(String message) {
		out.println(message);
		out.flush();
	}

	public void showRegistrationFailed(String message) {
		out.println("REGISTRATION ERROR: " + message);
		out.println("Please attempt to register again.");
		out.flush();
	}

	public void showLoginFailed(String message) {
		out.println("LOGIN ERROR: " + message);
		out.println("Please attempt to login again.");
		out.flush();
	}

	public Object getChoiceFromOptions(Object[] options) {
		Object choice = null;
		while (choice == null) {
			displayMenuOptions(options);
			choice = getChoiceFromUserInput(options);
		}
		out.println();
		return choice;
	}

	private Object getChoiceFromUserInput(Object[] options) {
		Object choice = null;
		String userInput = in.nextLine();
		try {
			int selectedOption = Integer.valueOf(userInput);
			if (selectedOption > 0 && selectedOption <= options.length) {
				choice = options[selectedOption - 1];
			}
		} catch (NumberFormatException e) {
			// eat the exception, an error message will be displayed below since choice will be null
		}
		if (choice == null) {
			out.println(System.lineSeparator() + "*** " + userInput + " is not a valid option ***" + System.lineSeparator());
		}
		return choice;
	}

	private void displayMenuOptions(Object[] options) {
		out.println();
		for (int i = 0; i < options.length; i++) {
			int optionNum = i + 1;
			out.println(optionNum + ") " + options[i]);
		}
		out.print(System.lineSeparator() + "Please choose an option >>> ");
		out.flush();
	}

	public String getUserInput(String prompt) {
		out.print(prompt + ": ");
		out.flush();
		return in.nextLine();
	}

	public Integer getUserInputInteger(String prompt) {
		Integer result = null;
		do {
			out.print(prompt + ": ");
			out.flush();
			String userInput = in.nextLine();
			try {
				result = Integer.parseInt(userInput);
			} catch (NumberFormatException e) {
				out.println(System.lineSeparator() + "*** " + userInput + " is not valid ***" + System.lineSeparator());
			}
		} while (result == null);
		return result;
	}

	public Double getUserInputDouble(String prompt){
		Double result = null;
		do {
			out.print(prompt + ": ");
			out.flush();
			String userInput = in.nextLine();

			try {
				result = Double.parseDouble(userInput);
				if(BigDecimal.valueOf(result).scale() > 2) {
					out.println(System.lineSeparator() + "*** " + userInput + " is not valid ***" + System.lineSeparator());
					result = null;
				}
			} catch (NumberFormatException e) {
				out.println(System.lineSeparator() + "*** " + userInput + " is not valid ***" + System.lineSeparator());
			}
		} while (result == null);
		return result;
	}


	public Transfer getTransferInfoFromUserPrompt(int toUserId, int fromUserId, double moneyToTransfer) {
		Transfer transfer = new Transfer();

		transfer.setAccountFrom(fromUserId);
		transfer.setAccountTo(toUserId);
		transfer.setAmount(moneyToTransfer);
		transfer.setTransferTypeId(2);
		transfer.setTransferStatusId(2);

		return transfer;

	}

	public void printTransferDetails(Transfer transfer) {
		System.out.println("-----------------------------");
		System.out.println("Transfer Details");
		System.out.println("-----------------------------");

		System.out.println("Id: " + transfer.getTransferId());
		System.out.println("From: " + transfer.getAccountFromName());
		System.out.println("To: " + transfer.getAccountToName());
		System.out.println("Type: Send");
		System.out.println("Status: Approved");
		System.out.printf("%s %.2f","Amount: $", transfer.getAmount());
		System.out.println("");

	}

	public void printListOfUsers(User[] users) {
		System.out.println("-----------------------------");
		System.out.println("Users");
		System.out.printf("%-12s %s", "ID", "Name");
		System.out.println("");
		System.out.println("-----------------------------");
		for (User user : users) {
			System.out.printf("%-12d %s", user.getId(), user.getUsername());
			System.out.println("");
		}
	}

	public void printListOfTransfersForUser(Transfer[] transfers) {

		System.out.println("-----------------------------");
		System.out.println("Transfers");
		System.out.printf("%-12s %-17s %s", "ID", "From/To", "Amount");
		System.out.println("");
		System.out.println("-----------------------------");
		for (Transfer transfer : transfers) {
			if (transfer.getAccountFromName() == null) {
				System.out.printf("%-12s %-17s %.2f", transfer.getTransferId(), "To " + transfer.getAccountToName(), transfer.getAmount());
				System.out.println("");
			} else if (transfer.getAccountToName() == null) {
				System.out.printf("%-12s %-17s %.2f", transfer.getTransferId(), "From " + transfer.getAccountFromName(), transfer.getAmount());
				System.out.println("");
			}
		}

	}
}
