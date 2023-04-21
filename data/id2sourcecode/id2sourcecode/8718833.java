    protected Vector<Transaction> getMyTransactions(Transaction[] transactions) {
        Vector<Transaction> match = new Vector<Transaction>(transactions.length);
        for (int i = 0; i < transactions.length; i++) {
            if (transactions[i].getChannel().equals(itsName)) {
                match.add(transactions[i]);
            }
        }
        return match;
    }
