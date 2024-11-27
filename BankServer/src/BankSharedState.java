public class BankSharedState {
    private double accountA = 1000;
    private double accountB = 1000;
    private double accountC = 1000;
    private boolean accessing = false;
    private int threadsWaiting = 0;

    //Acquire lock
    public synchronized void acquireLock() throws InterruptedException {
        Thread me = Thread.currentThread();
        System.out.println(me.getName() + " attempting to acquire lock...");
        ++threadsWaiting;
        while (accessing) {
            System.out.println(me.getName() + " waiting for lock...");
            wait();
        }
        --threadsWaiting;
        accessing = true;
        System.out.println(me.getName() + " acquired lock!");
    }

    //Release lock
    public synchronized void releaseLock() {
        accessing = false;
        notifyAll();
        Thread me = Thread.currentThread();
        System.out.println(me.getName() + " released lock!");
    }

    public synchronized String processRequest(String request) {
        String[] parts = request.split(" ");
        
        //Error checking, if user enters Add_money A e.g
        if (parts.length < 2) {
            return "Invalid command format. Please provide operation and account details.";
        }
        
        String operation = parts[0];
        String account = parts[1];
        double value = 0;
        String response = "";

        
        switch (operation) {
            case "Add_money":
                if (parts.length < 3) {
                    return "Add_money needs account + letter and amount (e.g., Add_money A 100).";
                }
                value = Double.parseDouble(parts[2]);
                response = addMoney(account, value);
                break;

            case "Subtract_money":
                if (parts.length < 3) {
                    return "Subtract_money needs account + letter and amount (e.g., Subtract_money B 50).";
                }
                value = Double.parseDouble(parts[2]);
                response = subtractMoney(account, value);
                break;

            case "Transfer_money":
                if (parts.length < 4) {
                    return "Transfer_money requires from account, to account, and amount (e.g., Transfer_money A B 200).";
                }
                String targetAccount = parts[2];
                value = Double.parseDouble(parts[3]);
                response = transferMoney(account, targetAccount, value);
                break;

            default:
                response = "Invalid operation. Please use Add_money, Subtract_money, or Transfer_money.";
        }

        return response;
    }


    private String addMoney(String account, double value) {
        double oldBalance = getAccountBalance(account);
        switch (account) {
            case "A":
                accountA += value;
                break;
            case "B":
                accountB += value;
                break;
            case "C":
                accountC += value;
                break;
            default:
                return "Invalid account.";
        }
        double newBalance = getAccountBalance(account);
        return "Added " + value + " to account " + account + ". Old Balance: " + oldBalance + ", New Balance: " + newBalance + ".";
    }

    private String subtractMoney(String account, double value) {
        double oldBalance = getAccountBalance(account);
        switch (account) {
            case "A":
                accountA -= value;
                break;
            case "B":
                accountB -= value;
                break;
            case "C":
                accountC -= value;
                break;
            default:
                return "Invalid account.";
        }
        double newBalance = getAccountBalance(account);
        return "Subtracted " + value + " from account " + account + ". Old Balance: " + oldBalance + ", New Balance: " + newBalance + ".";
    }

    private String transferMoney(String fromAccount, String toAccount, double value) {
        double oldFromBalance = getAccountBalance(fromAccount);
        double oldToBalance = getAccountBalance(toAccount);
        
        subtractMoney(fromAccount, value);
        addMoney(toAccount, value);

        double newFromBalance = getAccountBalance(fromAccount);
        double newToBalance = getAccountBalance(toAccount);

        return "Transferred " + value + " from " + fromAccount + " to " + toAccount + ". From Account - Old Balance: " + oldFromBalance + ", New Balance: " + newFromBalance + ". To Account - Old Balance: " + oldToBalance + ", New Balance: " + newToBalance + ".";
    }

    private double getAccountBalance(String account) {
        switch (account) {
            case "A":
                return accountA;
            case "B":
                return accountB;
            case "C":
                return accountC;
            default:
                return -1;  
        }
    }
}