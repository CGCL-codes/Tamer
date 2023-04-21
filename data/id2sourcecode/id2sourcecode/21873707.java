    public static void transferCashBookToCashBook(Properties ctx, int fromCashBookId, int toCashBookId, int currencyId, BigDecimal amount, Timestamp dateAcct, String trxName) throws OperationException {
        MCash fromJournal = MCash.get(ctx, fromCashBookId, dateAcct, trxName);
        transferFromJournalToCashBook(ctx, fromJournal, toCashBookId, currencyId, amount, dateAcct, trxName);
    }
