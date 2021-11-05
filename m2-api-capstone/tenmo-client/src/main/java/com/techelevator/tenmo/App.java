package com.techelevator.tenmo;

import com.techelevator.tenmo.auth.models.AuthenticatedUser;
import com.techelevator.tenmo.auth.models.User;
import com.techelevator.tenmo.auth.models.UserCredentials;
import com.techelevator.tenmo.auth.services.AuthenticationService;
import com.techelevator.tenmo.auth.services.AuthenticationServiceException;
import com.techelevator.tenmo.models.Account;
import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.services.ConsoleService;
import com.techelevator.tenmo.services.TenmoService;
import io.cucumber.java.bs.A;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {

private static final String API_BASE_URL = "http://localhost:8080/";
    
    private static final String MENU_OPTION_EXIT = "Exit";
    private static final String LOGIN_MENU_OPTION_REGISTER = "Register";
	private static final String LOGIN_MENU_OPTION_LOGIN = "Login";
	private static final String[] LOGIN_MENU_OPTIONS = { LOGIN_MENU_OPTION_REGISTER, LOGIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };
	private static final String MAIN_MENU_OPTION_VIEW_BALANCE = "View your current balance";
	private static final String MAIN_MENU_OPTION_SEND_BUCKS = "Send TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS = "View your past transfers";
	private static final String MAIN_MENU_OPTION_REQUEST_BUCKS = "Request TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS = "View your pending requests";
	private static final String MAIN_MENU_OPTION_LOGIN = "Login as different user";
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_VIEW_BALANCE, MAIN_MENU_OPTION_SEND_BUCKS, MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS, MAIN_MENU_OPTION_REQUEST_BUCKS, MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS, MAIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };
	
    private AuthenticatedUser currentUser;
    private ConsoleService console;
    private AuthenticationService authenticationService;
    private TenmoService tenmoService;

    public static void main(String[] args) {
    	App app = new App(new ConsoleService(System.in, System.out), new AuthenticationService(API_BASE_URL), new TenmoService(API_BASE_URL));
    	app.run();
    }

    public App(ConsoleService console, AuthenticationService authenticationService, TenmoService tenmoService) {
		this.console = console;
		this.authenticationService = authenticationService;
		this.tenmoService = tenmoService;
	}

	public void run() {
		console.showWelcomeBanner();
		registerAndLogin();
		mainMenu();
	}

	private void mainMenu() {
		while(true) {
			String choice = (String)console.getChoiceFromOptions(MAIN_MENU_OPTIONS);
			if(MAIN_MENU_OPTION_VIEW_BALANCE.equals(choice)) {
				viewCurrentBalance();
			} else if(MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS.equals(choice)) {
				viewTransferHistory();
			} else if(MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS.equals(choice)) {
				viewPendingRequests();
			} else if(MAIN_MENU_OPTION_SEND_BUCKS.equals(choice)) {
				sendBucks();
			} else if(MAIN_MENU_OPTION_REQUEST_BUCKS.equals(choice)) {
				requestBucks();
			} else if(MAIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else {
				// the only other option on the main menu is to exit
				exitProgram();
			}
		}
	}

	private void viewCurrentBalance() {
		// TODO Auto-generated method stub
		Account account = tenmoService.retrieveAccountDetails(currentUser.getUser().getId());
		System.out.println("Current Balance: " + account.getAccountBalance());
		
	}

	private void viewTransferHistory() {
		// TODO Auto-generated method stub
		Account account = tenmoService.retrieveAccountDetails(currentUser.getUser().getId());

		Transfer[] transfers = tenmoService.retrieveTransfersForUser(account.getAccountId());

		while (true) {

			console.printListOfTransfersForUser(transfers);
			System.out.println("");
			int transferId = console.getUserInputInteger("Please enter transfer ID to view details (0 to cancel)");
			System.out.println("");
			Transfer chosenTransfer = tenmoService.retrieveTransferDetails(transferId);

			if(transferId == 0) {
				break;

			}
			List<Integer> transferIds = new ArrayList<>();
			for (Transfer transfer : transfers) {
				transferIds.add(transfer.getTransferId());
			}

			if (transferIds.contains(transferId)) {
				console.printTransferDetails(chosenTransfer);
				break;
			}
			else {
				System.out.println("Invalid Transfer ID. Please enter a valid option from above.");

			}

//			for (Transfer transfer : transfers) {
//				if (transfer.getTransferId() == transferId){
//					console.printTransferDetails(chosenTransfer);
//					break;
//				}
//			}
			//System.out.println("Invalid Transfer ID. Please enter a valid option from above.");
		}



		
	}

	private void viewPendingRequests() {
		// TODO Auto-generated method stub
		
	}

	private void sendBucks() {
		User[] users = tenmoService.retrieveAllUsers();
		Account account = tenmoService.retrieveAccountDetails(currentUser.getUser().getId());

		while (true) {
			console.printListOfUsers(users);

			int toUserId = console.getUserInputInteger("Enter ID of user you are sending to(0 to cancel)");
			System.out.println("");


			List<Integer> userIds = new ArrayList<>();
			for (User user : users) {
				userIds.add(user.getId());
			}

			if (userIds.contains(toUserId)) {
				double amount = console.getUserInputDouble("Enter amount");

				if(amount <= account.getAccountBalance()) {
					Transfer transfer = console.getTransferInfoFromUserPrompt(toUserId, currentUser.getUser().getId(), amount);
					tenmoService.createTransfer(transfer);
					break;
				} else {
					System.out.println("Insufficient funds for transfer!");
				}
			}
			else {
				System.out.println("Invalid User ID. Please enter a valid option from above.");
			}
		}
	}

	private void requestBucks() {
		// TODO Auto-generated method stub
		
	}
	
	private void exitProgram() {
		System.exit(0);
	}

	private void registerAndLogin() {
		while(!isAuthenticated()) {
			String choice = (String)console.getChoiceFromOptions(LOGIN_MENU_OPTIONS);
			if (LOGIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else if (LOGIN_MENU_OPTION_REGISTER.equals(choice)) {
				register();
			} else {
				// the only other option on the login menu is to exit
				exitProgram();
			}
		}
	}

	private boolean isAuthenticated() {
		return currentUser != null;
	}

	private void register() {
		console.displayMessage("Please register a new user account");
		boolean isRegistered = false;
        while (!isRegistered) //will keep looping until user is registered
        {
            UserCredentials credentials = collectUserCredentials();
            try {
            	authenticationService.register(credentials);
            	isRegistered = true;
				console.displayMessage("Registration successful. You can now login.");
            } catch(AuthenticationServiceException e) {
				console.showRegistrationFailed(e.getMessage());
            }
        }
	}

	private void login() {
		System.out.println("Please log in");
		currentUser = null;
		while (currentUser == null) //will keep looping until user is logged in
		{
			UserCredentials credentials = collectUserCredentials();
		    try {
				currentUser = authenticationService.login(credentials);
			} catch (AuthenticationServiceException e) {
				console.showLoginFailed(e.getMessage());
			}
		}
	}
	
	private UserCredentials collectUserCredentials() {
		String username = console.getUserInput("Username");
		String password = console.getUserInput("Password");
		return new UserCredentials(username, password);
	}
}
