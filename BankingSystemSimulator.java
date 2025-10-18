/*
Simulates a banking system supporting account creation,
deposits, withdrawals, transfers, viewing, and activation/deactivation.
Uses Singleton (single system), State (account status), and Strategy (transaction fees). 
Tracks transaction history and handles errors like inactive or missing accounts and insufficient funds.
*/

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Locale;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        BankingSystem bankingSystem = BankingSystem.getInstance(); // create unique instance of BankingSystem using Singleton
        for (int i = 0; i < n; i++) {
            String s = scanner.next(); // input operation
            String accountName;
            switch (s) {
                case "Create":
                    scanner.next();
                    String accountType = scanner.next();
                    accountName = scanner.next();
                    double balance = Double.parseDouble(scanner.next());
                    if (bankingSystem.getAccount(accountName) == null) { // if an account with that accountName does not exist yet
                        bankingSystem.createAccount(accountName, balance, accountType); // create account
                    }
                    break;
                case "Deposit":
                    accountName = scanner.next();
                    double depositAmount = Double.parseDouble(scanner.next());
                    if (bankingSystem.getAccount(accountName) == null) { // check if account does not exist yet
                        System.out.println("Error: Account " + accountName + " does not exist.");
                    } else {
                        bankingSystem.getAccount(accountName).addDeposit(depositAmount); // add deposit to an account with this accountName
                    }
                    break;
                case "Withdraw":
                    accountName = scanner.next();
                    double withdrawalAmount = Double.parseDouble(scanner.next());
                    if (bankingSystem.getAccount(accountName) == null) { // check if account does not exist yet
                        System.out.println("Error: Account " + accountName + " does not exist.");
                    } else {
                        bankingSystem.getAccount(accountName).withdraw(withdrawalAmount); // withdraw money from an account with this accountName
                    }
                    break;
                case "Transfer":
                    String fromAccountName = scanner.next();
                    String toAccountName = scanner.next();
                    double transferAmount = Double.parseDouble(scanner.next());
                    if (bankingSystem.getAccount(fromAccountName) == null || bankingSystem.getAccount(toAccountName) == null) { // check if accounts does not exist yet
                        if (bankingSystem.getAccount(fromAccountName) == null) { // if one of them does not exist
                            System.out.println("Error: Account " + fromAccountName + " does not exist.");
                        } else { // if another does not exist
                            System.out.println("Error: Account " + toAccountName + " does not exist.");
                        }
                    } else {
                        double transferAmount1 = bankingSystem.getAccount(fromAccountName).getMoney(transferAmount);
                        double difference = BigDecimal.valueOf(transferAmount - transferAmount1).setScale(5, RoundingMode.HALF_UP).doubleValue();
                        if (transferAmount1 != 0) { // if account has enough money
                            bankingSystem.getAccount(toAccountName).setMoney(transferAmount1); // transfer money
                            bankingSystem.getAccount(fromAccountName).transferMessage(transferAmount1, toAccountName, difference); // output the message about a successful money transfer
                        }
                    }
                    break;
                case "View":
                    accountName = scanner.next();
                    if (bankingSystem.getAccount(accountName) == null) { // check if account does not exist yet
                        System.out.println("Error: Account " + accountName + " does not exist.");
                    } else {
                        bankingSystem.getAccount(accountName).viewDetails(); // output details about an account with this accountName
                    }
                    break;
                case "Deactivate":
                    accountName = scanner.next();
                    if (bankingSystem.getAccount(accountName) == null) { // check if account does not exist yet
                        System.out.println("Error: Account " + accountName + " does not exist.");
                    } else {
                        bankingSystem.getAccount(accountName).setActive("Inactive"); // deactivate this account
                    }
                    break;
                case "Activate":
                    accountName = scanner.next();
                    if (bankingSystem.getAccount(accountName) == null) { // check if account does not exist yet
                        System.out.println("Error: Account " + accountName + " does not exist.");
                    } else {
                        bankingSystem.getAccount(accountName).setActive("Active"); // activate this account
                    }
                    break;
            }
        }
    }
}

interface TransactionFeeStrategy { // create strategy interface to describe some methods about calculating transaction money
    double transactionCount(double amountOfMoney); // calculate transaction
    void accountCreationMessage(String accountName, double initialDeposit); // output message about successful creating account
    String getType(); // getter for type of account
    double getTransactionFee(); // getter for transaction fee coefficient
}

class SavingsTransactionFee implements TransactionFeeStrategy { // create SavingsTransactionFee class to calculate transaction for savings accounts
    @Override
    public double transactionCount(double amountOfMoney) { // calculate transaction
        return amountOfMoney / 100 * 1.5;
    }
    @Override
    public void accountCreationMessage(String accountName, double initialDeposit) { // output message about successful creating account
        System.out.println("A new Savings account created for " + accountName + " with an initial balance of $" + String.format(Locale.getDefault(), "%.3f", initialDeposit).replace(",", ".") + ".");
    }
    @Override
    public String getType() { // getter for type of account
        return "Savings";
    }
    @Override
    public double getTransactionFee() { // getter for transaction fee coefficient
        return 1.5;
    }
}

class CheckingTransactionFee implements TransactionFeeStrategy { // create CheckingTransactionFee class to calculate transaction for checking accounts
    @Override
    public double transactionCount(double amountOfMoney) { // calculate transaction
        return amountOfMoney / 100 * 2;
    }
    @Override
    public void accountCreationMessage(String accountName, double initialDeposit) { // output message about successful creating account
        System.out.println("A new Checking account created for " + accountName + " with an initial balance of $" + String.format(Locale.getDefault(), "%.3f", initialDeposit).replace(",", ".") + ".");
    }
    @Override
    public String getType() { // getter for type of account
        return "Checking";
    }
    @Override
    public double getTransactionFee() { // getter for transaction fee coefficient
        return 2;
    }
}

class BusinessTransactionFee implements TransactionFeeStrategy { // create BusinessTransactionFee class to calculate transaction for business accounts
    @Override
    public double transactionCount(double amountOfMoney) { // calculate transaction
        return amountOfMoney / 100 * 2.5;
    }
    @Override
    public void accountCreationMessage(String accountName, double initialDeposit) { // output message about successful creating account
        System.out.println("A new Business account created for " + accountName + " with an initial balance of $" + String.format(Locale.getDefault(), "%.3f", initialDeposit).replace(",", ".") + ".");
    }
    @Override
    public String getType() { // getter for type of account
        return "Business";
    }
    @Override
    public double getTransactionFee() { // getter for transaction fee coefficient
        return 2.5;
    }
}

class BankingSystem { // creating a BankingSystem class using a Singleton pattern to create only one unique instance of the banking system
    private static BankingSystem unique;
    private Map<String, Account> accounts = new HashMap<>();
    private BankingSystem() {};
    public static BankingSystem getInstance() { // method for making unique instance of BankingSystem
        if (unique == null) // if the instance has not been created yet
            unique = new BankingSystem(); // create it
        return unique;
    }
    public void createAccount(String name, double balance, String type) { // the method for creating an account and saving it to the hashmap accounts
        Account account = new ProxyAccount(name, balance);
        switch (type) {
            case "Savings":
            case "savings":
                account.setTransactionFeeStrategy(new SavingsTransactionFee()); // set transaction fee strategy for savings account
                break;
            case "Checking":
            case "checking":
                account.setTransactionFeeStrategy(new CheckingTransactionFee()); // set transaction fee strategy for checking account
                break;
            case "Business":
            case "business":
                account.setTransactionFeeStrategy(new BusinessTransactionFee()); // set transaction fee strategy for business account
                break;
        }
        account.accountCreationMessage(); // output message about successful creating account
        accounts.put(name, account); // adding an account to a hashmap to check for the uniqueness of an account with that accountName
    }
    Account getAccount(String accountName) { // method for getting instance of account by accountName
        return accounts.get(accountName);
    }
}

interface Account { // create account interface to describe all operations with accounts
    void setTransactionFeeStrategy(TransactionFeeStrategy transactionFeeStrategy); // setter for transaction fee strategy
    double getTransactionFee(); // getter for transaction fee coefficient
    void accountCreationMessage(); // output message about successful creating of account
    public void addDeposit(double deposit); // add money to the account
    double transactionCount(double amountOfMoney); // calculate transaction
    String getType(); // getter for type of account
    void withdraw(double withdrawalAmount); // withdraw money from the account
    double getMoney(double amountOfMoney); // withdraw the required amount of money to transfer to another account
    void viewDetails(); // operation for output all the details about account
    void setActive(String newType); // setter for active variable
    void transferMessage(double amountOfMoney, String name, double part);
    void setMoney(double amountOfMoney); // setter for money
}

class RealAccount implements Account { // creating a RealAccount class that rewrites all interface methods
    private TransactionFeeStrategy transactionFeeStrategy;
    private final String accountName;
    private double balance;
    private String active;
    private final double initialDeposit;
    private String history = "";
    public RealAccount(String name, double balance) { // create instance of RealAccount
        this.accountName = name;
        this.balance = balance;
        this.active = "Active";
        this.initialDeposit = balance;
        this.history += "Initial Deposit $" + String.format(Locale.getDefault(), "%.3f", initialDeposit).replace(",", ".");
    }

    @Override
    public void setTransactionFeeStrategy(TransactionFeeStrategy transactionFeeStrategy) { // setter for transaction fee strategy
        this.transactionFeeStrategy = transactionFeeStrategy;
    }

    @Override
    public double getTransactionFee() { // getter for transaction fee
        return transactionFeeStrategy.getTransactionFee();
    }

    @Override
    public void accountCreationMessage() { // output message about successful creating of account
        if (transactionFeeStrategy == null) {
            return;
        }
        transactionFeeStrategy.accountCreationMessage(accountName, initialDeposit);
    }

    @Override
    public void addDeposit(double deposit) { 
        balance += deposit; // add transferred money from another account
        history += ", Deposit $" + String.format(Locale.getDefault(), "%.3f", deposit).replace(",", "."); // add information to history of this account
        System.out.println(accountName + " successfully deposited $" + String.format(Locale.getDefault(), "%.3f", deposit).replace(",", ".") + ". New Balance: $" + String.format(Locale.getDefault(), "%.3f", balance).replace(",", ".") + ".");
    }

    @Override
    public double transactionCount(double amountOfMoney) { // calculate transaction
        if (transactionFeeStrategy == null) {
            return 0;
        }
        return transactionFeeStrategy.transactionCount(amountOfMoney);
    }

    @Override
    public String getType() { // get type of this account
        return transactionFeeStrategy.getType();
    }

    @Override
    public void withdraw(double withdrawalAmount) { // function for withdrawing money from an account
        if (balance < withdrawalAmount) { // if not enough money to withdraw
            System.out.println("Error: Insufficient funds for " + accountName + "."); // output error message
        } else {
            balance -= withdrawalAmount; // subtract money to transfer it
            double part = BigDecimal.valueOf(transactionCount(withdrawalAmount)).setScale(4, RoundingMode.HALF_UP).doubleValue();
            history += ", Withdrawal $" + String.format(Locale.getDefault(), "%.3f", withdrawalAmount).replace(",", "."); // add information to history
            System.out.println(accountName + " successfully withdrew $" + String.format(Locale.getDefault(), "%.3f", withdrawalAmount - part).replace(",", ".") + ". New Balance: $" +
                    String.format(Locale.getDefault(), "%.3f", balance).replace(",", ".") + ". Transaction Fee: $" + BigDecimal.valueOf(part).setScale(3, RoundingMode.HALF_UP) + " (" + getTransactionFee() + "%) in the system.");
        }
    }

    @Override
    public double getMoney(double amountOfMoney) { // attempt to deduct money from the transfer account
        if (amountOfMoney > balance) {
            System.out.println("Error: Insufficient funds for " + accountName + "."); // the message that there is not enough money
            return 0;
        } else {
            balance -= amountOfMoney; // subtract money
            history += ", Transfer $" + String.format(Locale.getDefault(), "%.3f", amountOfMoney).replace(",", "."); // add operation to history
            return amountOfMoney - transactionCount(amountOfMoney); // return amount of money to add it to another account
        }
    }

    @Override
    public void viewDetails() {
        System.out.println(accountName + "'s Account: Type: " + getType() + ", Balance: $" + String.format(Locale.getDefault(), "%.3f", balance).replace(",", ".") + ", State: " +
                active + ", Transactions: [" + history + "]."); // output of all account details
    }

    @Override
    public void setActive(String newType) {
        if (newType.equals(active) && active.equals("Active")) {
            System.out.println("Error: Account " + accountName + " is already activated."); // error handling that the account is already active
        } else if (newType.equals(active) && active.equals("Inactive")) {
            System.out.println("Error: Account " + accountName + " is already deactivated."); // error handling that the account is already deactivate
        } else {
            if (newType.equals("Inactive")) {
                System.out.println(accountName + "'s account is now deactivated."); // successful deactivation
            } else {
                System.out.println(accountName + "'s account is now activated."); // successful activation
            }
            active = newType;
        }
    }

    @Override
    public void transferMessage(double amountOfMoney, String name, double part) { // output message about successful transferring money
        System.out.println(accountName + " successfully transferred $" + String.format(Locale.getDefault(), "%.3f", amountOfMoney).replace(",", ".") + " to " + name + ". New Balance: $" +
                String.format(Locale.getDefault(), "%.3f", balance).replace(",", ".") + ". Transaction Fee: $" + BigDecimal.valueOf(part).setScale(3, RoundingMode.HALF_UP) + " (" + getTransactionFee() + "%) in the system.");
    }

    @Override
    public void setMoney(double amountOfMoney) {
        balance += amountOfMoney; // add money from another account
    }
}

class ProxyAccount implements Account { // Creating a ProxyAccount to check account activity before performing certain operations
    private RealAccount realAccount;
    private TransactionFeeStrategy transactionFeeStrategy;
    private final String accountName;
    private double balance;
    private String active;
    private final double initialDeposit;
    private String history = "";
    public ProxyAccount(String name, double balance) { // create instance of ProxyAccount
        if (realAccount == null) realAccount = new RealAccount(name, balance);
        this.accountName = name;
        this.balance = balance;
        this.active = "Active";
        this.initialDeposit = balance;
        this.history += "Initial Deposit $" + String.format(Locale.getDefault(), "%.3f", initialDeposit).replace(",", ".");
    }

    @Override
    public void setTransactionFeeStrategy(TransactionFeeStrategy transactionFeeStrategy) { // setter for transaction fee strategy
        this.transactionFeeStrategy = transactionFeeStrategy;
    }

    @Override
    public double getTransactionFee() { // getter for transaction fee
        return transactionFeeStrategy.getTransactionFee();
    }

    @Override
    public void accountCreationMessage() { // output message about successful creating of account
        if (transactionFeeStrategy == null) {
            return;
        }
        transactionFeeStrategy.accountCreationMessage(accountName, initialDeposit);
    }

    @Override
    public void addDeposit(double deposit) {
        balance += deposit; // add transferred money from another account
        history += ", Deposit $" + String.format(Locale.getDefault(), "%.3f", deposit).replace(",", "."); // add information to history of this account
        System.out.println(accountName + " successfully deposited $" + String.format(Locale.getDefault(), "%.3f", deposit).replace(",", ".") + ". New Balance: $" + String.format(Locale.getDefault(), "%.3f", balance).replace(",", ".") + ".");
    }

    @Override
    public double transactionCount(double amountOfMoney) { // calculate transaction
        if (transactionFeeStrategy == null) {
            return 0;
        }
        return transactionFeeStrategy.transactionCount(amountOfMoney);
    }

    @Override
    public String getType() { // get type of this account
        return transactionFeeStrategy.getType();
    }

    @Override
    public void withdraw(double withdrawalAmount) { // function for withdrawing money from an account
        if (errorStatus()) { // checking that the account is currently inactive
            if (balance < withdrawalAmount) { // if not enough money to withdraw
                System.out.println("Error: Insufficient funds for " + accountName + "."); // output error message
            } else {
                balance -= withdrawalAmount;
                double part = BigDecimal.valueOf(transactionCount(withdrawalAmount)).setScale(4, RoundingMode.HALF_UP).doubleValue();
                history += ", Withdrawal $" + String.format(Locale.getDefault(), "%.3f", withdrawalAmount).replace(",", "."); // add information to history
                System.out.println(accountName + " successfully withdrew $" + String.format(Locale.getDefault(), "%.3f", withdrawalAmount - part).replace(",", ".") + ". New Balance: $" +
                        String.format(Locale.getDefault(), "%.3f", balance).replace(",", ".") + ". Transaction Fee: $" + BigDecimal.valueOf(part).setScale(3, RoundingMode.HALF_UP) + " (" + getTransactionFee() + "%) in the system.");
            }
        }
    }

    @Override
    public double getMoney(double amountOfMoney) { // attempt to deduct money from the transfer account
        if (errorStatus()) { // checking that the account is currently inactive
            if (amountOfMoney > balance) {
                System.out.println("Error: Insufficient funds for " + accountName + "."); // the message that there is not enough money
                return 0;
            } else {
                balance -= amountOfMoney; // subtract money
                history += ", Transfer $" + String.format(Locale.getDefault(), "%.3f", amountOfMoney).replace(",", "."); // add operation to history
                return amountOfMoney - transactionCount(amountOfMoney); // return amount of money to add it to another account
            }
        } else {
            return 0; // return something because transferring is not possible. This account is inactive now.
        }
    }

    @Override
    public void viewDetails() {
        System.out.println(accountName + "'s Account: Type: " + getType() + ", Balance: $" + String.format(Locale.getDefault(), "%.3f", balance).replace(",", ".") + ", State: " +
                active + ", Transactions: [" + history + "]."); // output of all account details
    }

    @Override
    public void setActive(String newType) {
        if (newType.equals(active) && active.equals("Active")) {
            System.out.println("Error: Account " + accountName + " is already activated."); // error handling that the account is already active
        } else if (newType.equals(active) && active.equals("Inactive")) {
            System.out.println("Error: Account " + accountName + " is already deactivated."); // error handling that the account is already deactivate
        } else {
            if (newType.equals("Inactive")) {
                System.out.println(accountName + "'s account is now deactivated."); // successful deactivation
            } else {
                System.out.println(accountName + "'s account is now activated."); // successful activation
            }
            active = newType;
        }
    }

    @Override
    public void transferMessage(double amountOfMoney, String name, double part) { // output message about successful transferring money
        System.out.println(accountName + " successfully transferred $" + String.format(Locale.getDefault(), "%.3f", amountOfMoney).replace(",", ".") + " to " + name + ". New Balance: $" +
                String.format(Locale.getDefault(), "%.3f", balance).replace(",", ".") + ". Transaction Fee: $" + BigDecimal.valueOf(part).setScale(3, RoundingMode.HALF_UP) + " (" + getTransactionFee() + "%) in the system.");
    }

    @Override
    public void setMoney(double amountOfMoney) {
        balance += amountOfMoney; // add money from another account
    }

    public boolean errorStatus() { // checking if account is active now
        if (active.equals("Inactive")) {
            System.out.println("Error: Account " + accountName + " is inactive.");
            return false;
        }
        return true;
    }
}
