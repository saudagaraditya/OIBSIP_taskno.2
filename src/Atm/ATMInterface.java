package Atm;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class User {
	private String userId;
	private String pin;

	public User(String userId, String pin) {
		this.userId = userId;
		this.pin = pin;
	}

	public String getUserId() {
		return userId;
	}

	public String getPin() {
		return pin;
	}
}

class Account {
	private String accountId;
	private double balance;
	private List<Transaction> transactions;
	private StringBuilder transactionHistory = new StringBuilder();

	public Account(String accountId) {
		this.accountId = accountId;
		this.balance = 0.0;
		this.transactions = new ArrayList<>();
	}

	public String getAccountId() {
		return accountId;
	}

	public double getBalance() {
		return balance;
	}

	public void deposit(double amount) {
		balance += amount;
		transactions.add(new Transaction("Deposit", amount));
		String str = amount + "Rs deposited\n";
        transactionHistory.append(str);
	}

	public boolean withdraw(double amount) {
		if (amount <= balance) {
			balance -= amount;
			transactions.add(new Transaction("Withdraw", amount));
			String str = amount + "Rs Withdrawn\n";
            transactionHistory.append(str);
			return true;
		} else {
			return false;
		}
	}

	public void transfer(Account recipient, double amount) {
		if (withdraw(amount)) {
			recipient.deposit(amount);
			transactions.add(new Transaction("Transfer to " + recipient.getAccountId(), amount));
			String str = amount + "Rs transferred to " + recipient + "\n";
            transactionHistory.append(str);
		}
		
	}

	public List<Transaction> getTransactions() {
		return transactions;
	}
}

class Transaction {
	private String type;
	private double amount;

	public Transaction(String type, double amount) {
		this.type = type;
		this.amount = amount;
	}

	public String getType() {
		return type;
	}

	public double getAmount() {
		return amount;
	}
}

class ATM {
	private User currentUser;
	private Account currentAccount;

	public ATM(User currentUser, Account currentAccount) {
		this.currentUser = currentUser;
		this.currentAccount = currentAccount;
	}

	public void start() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Welcome to the ATM");
		System.out.print("Enter user id: ");
		String userId = scanner.nextLine();
		System.out.print("Enter pin: ");
		String pin = scanner.nextLine();

		if (authenticateUser(userId, pin)) {
			System.out.println("Authentication successful");
			displayMenu();
			int choice = scanner.nextInt();
			while (choice != 5) {
				switch (choice) {
				case 1:
					showTransactionsHistory();
					break;
				case 2:
					withdraw(scanner);
					break;
				case 3:
					deposit(scanner);
					break;
				case 4:
					transfer(scanner);
					break;
				default:
					System.out.println("Invalid choice");
				}
				displayMenu();
				choice = scanner.nextInt();
			}
			System.out.println("Thank you for using the ATM");
		} else {
			System.out.println("Invalid Username or Pin...Please try again!");
		}
	}

	private boolean authenticateUser(String userId, String pin) {
		return currentUser.getUserId().equals(userId) && currentUser.getPin().equals(pin);
	}

	private void displayMenu() {
		System.out.println("\nATM Menu:");
		System.out.println("1. View Transactions History");
		System.out.println("2. Withdraw");
		System.out.println("3. Deposit");
		System.out.println("4. Transfer");
		System.out.println("5. Quit");
		System.out.print("Enter your choice: ");
	}

	private void showTransactionsHistory() {
	    System.out.println("\nTransactions History:");

	    List<Transaction> transactions = currentAccount.getTransactions();
	    if (transactions.isEmpty()) {
	        System.out.println("No transactions found.");
	    } else {
	        for (Transaction transaction : transactions) {
	            System.out.println(transaction.getType() + ": " + transaction.getAmount());
	        }
	    }
	}

	private void withdraw(Scanner scanner) {
		System.out.print("Enter amount to withdraw: ");
		double amount = scanner.nextDouble();
		if (currentAccount.withdraw(amount)) {
			System.out.println("Withdrawal successful");
		} else {
			System.out.println("Insufficient funds");
		}
	}

	private void deposit(Scanner scanner) {
		System.out.print("Enter amount to deposit: ");
		double amount = scanner.nextDouble();
		currentAccount.deposit(amount);
		System.out.println("Deposit successful");
	}

	private void transfer(Scanner scanner) {
		System.out.print("Enter recipient account ID: ");
		String recipientAccountId = scanner.next();
		System.out.print("Enter amount to transfer: ");
		double amount = scanner.nextDouble();
		
		Account recipientAccount = new Account(recipientAccountId);
		currentAccount.transfer(recipientAccount, amount);
		System.out.println("Transfer successful");
	}
}

public class ATMInterface {
	public static void main(String[] args) {
		
		User user = new User("Aditya", "2903");
		Account account = new Account("9819511");

		
		ATM atm = new ATM(user, account);
		atm.start();
	}
}
