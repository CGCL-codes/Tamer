    private void updateReport() {
        Set<MoneyPit> sources = filter.getSources();
        Set<MoneyPit> destinations = filter.getDestinations();
        if (filter.showAllDestinations || filter.showAllSources) {
            List<Month> months = DataManager.getInstance().getMonths(filter);
            if (filter.showAllDestinations) {
                destinations = new HashSet<MoneyPit>();
                for (Month month : months) destinations.addAll(month.getBudget().getAllDestinations());
            }
            if (filter.showAllSources) {
                sources = new HashSet<MoneyPit>();
                for (Month month : months) sources.addAll(month.getBudget().getAllSources());
            }
        }
        Set<MoneyPit> accounts = new HashSet<MoneyPit>();
        Set<MoneyPit> expenses = new HashSet<MoneyPit>();
        boolean haveAccounts = false;
        boolean haveExpenses = false;
        for (MoneyPit source : sources) {
            if (source.getType() == MoneyPit.Type.ACCOUNT) {
                haveAccounts = true;
                accounts.add(source);
            } else if (source.getType() == MoneyPit.Type.EXPENSE) {
                haveExpenses = true;
                expenses.add(source);
            }
        }
        for (MoneyPit destination : destinations) {
            if (destination.getType() == MoneyPit.Type.ACCOUNT) {
                haveAccounts = true;
                accounts.add(destination);
            } else if (destination.getType() == MoneyPit.Type.EXPENSE) {
                haveExpenses = true;
                expenses.add(destination);
            }
        }
        long income = 0L;
        long refundToAccounts = 0L;
        long refundToAccountsFromCategories = 0L;
        long transferFromOther = 0L;
        long transferToOther = 0L;
        long transferBetween = 0L;
        long spentFromAccounts = 0L;
        long spentFromAccountsToCategories = 0L;
        long allocated = 0L;
        long spentInCategories = 0L;
        long refundedToCategories = 0L;
        List<Transaction> transactions = DataManager.getInstance().getTransactions(filter);
        for (Transaction transaction : transactions) for (Transaction.Transfer transfer : transaction.getTransfers()) {
            MoneyPit source = transfer.getSource();
            MoneyPit destination = transfer.getDestination();
            long amount = transfer.getAmount();
            if (source == MoneyPit.NONE || source.getType() == MoneyPit.Type.INCOME) {
                if (accounts.contains(destination)) income += amount;
                continue;
            }
            if (accounts.contains(source)) {
                if (destination.getType() == MoneyPit.Type.EXPENSE) {
                    if (amount > 0) {
                        spentFromAccounts += amount;
                        if (expenses.contains(destination)) {
                            spentInCategories += amount;
                            spentFromAccountsToCategories += amount;
                        }
                    } else {
                        refundToAccounts -= amount;
                        if (expenses.contains(destination)) {
                            refundedToCategories -= amount;
                            refundToAccountsFromCategories -= amount;
                        }
                    }
                    continue;
                }
                if (destination.getType() == MoneyPit.Type.ACCOUNT) {
                    if (accounts.contains(destination)) {
                        transferBetween += amount;
                        continue;
                    }
                    if (amount > 0) transferToOther += amount; else transferFromOther -= amount;
                    continue;
                }
            }
            if (source.getType() == MoneyPit.Type.ACCOUNT) {
                if (accounts.contains(destination)) {
                    if (amount > 0) transferFromOther += amount; else transferToOther -= amount;
                    continue;
                }
                if (expenses.contains(destination)) {
                    if (amount > 0) spentInCategories += amount; else refundedToCategories -= amount;
                    continue;
                }
            }
        }
        List<Transaction> allocations = DataManager.getInstance().getAllocations(filter);
        for (Transaction allocation : allocations) for (Transaction.Transfer transfer : allocation.getTransfers()) {
            MoneyPit source = transfer.getSource();
            MoneyPit destination = transfer.getDestination();
            long amount = transfer.getAmount();
            if (expenses.contains(source) && expenses.contains(destination)) {
                continue;
            }
            if (expenses.contains(source)) {
                allocated -= amount;
                continue;
            }
            if (expenses.contains(destination)) allocated += amount;
        }
        StringBuffer buffer = new StringBuffer();
        buffer.append("<html>");
        if (haveAccounts) {
            buffer.append("<h1>Selected Accounts</h1>");
            buffer.append("<hr><BR>");
            buffer.append("<b>Income:</b> ");
            buffer.append(CurrencyFormater.format(income));
            buffer.append("<BR>");
            buffer.append("<b>Spent from selected accounts:</b> ");
            buffer.append(CurrencyFormater.format(spentFromAccounts));
            buffer.append("<BR>");
            if (haveExpenses) {
                buffer.append("<b> Spent to selected categories:</b> ");
                buffer.append(CurrencyFormater.format(spentFromAccountsToCategories));
                buffer.append("<BR>");
            }
            buffer.append("<b>Refunded to selected accounts:</b> ");
            buffer.append(CurrencyFormater.format(refundToAccounts));
            buffer.append("<BR>");
            if (haveExpenses) {
                buffer.append("<b> Refunded from selected categories:</b> ");
                buffer.append(CurrencyFormater.format(refundToAccountsFromCategories));
                buffer.append("<BR>");
            }
            buffer.append("<b>Transfered from other accounts:</b> ");
            buffer.append(CurrencyFormater.format(transferFromOther));
            buffer.append("<BR>");
            buffer.append("<b>Transfered to other accounts:</b> ");
            buffer.append(CurrencyFormater.format(transferToOther));
            buffer.append("<BR>");
            buffer.append("<b>Transfered between selected accounts:</b> ");
            buffer.append(CurrencyFormater.format(transferBetween));
            buffer.append("<BR>");
            if (haveExpenses) buffer.append("<BR><HR><BR>");
        }
        if (haveExpenses) {
            buffer.append("<h1>Selected Expense Categories</h1>");
            buffer.append("<hr><BR>");
            buffer.append("<b>Allocated to selected categories:</b> ");
            buffer.append(CurrencyFormater.format(allocated));
            buffer.append("<BR>");
            buffer.append("<b>Spent in selected categories:</b> ");
            buffer.append(CurrencyFormater.format(spentInCategories));
            buffer.append("<BR>");
            buffer.append("<b>Refunded to selected categories:</b> ");
            buffer.append(CurrencyFormater.format(refundedToCategories));
            buffer.append("<BR>");
        }
        textPane.setText(buffer.toString());
    }
