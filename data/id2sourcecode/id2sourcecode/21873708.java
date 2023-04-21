    public static void transferFromJournalToCashBook(Properties ctx, MCash cashJournal, int toCashBookId, int currencyId, BigDecimal amount, Timestamp dateAcct, String trxName) throws OperationException {
        MCashBook fromCashBook = new MCashBook(ctx, cashJournal.getC_CashBook_ID(), trxName);
        MCashBook toCashBook = new MCashBook(ctx, toCashBookId, trxName);
        int fromBankAccountId = OrganisationManager.getCreateTransferBankAccount(ctx, fromCashBook.getAD_Org_ID(), currencyId, null);
        int toBankAccountId = OrganisationManager.getCreateTransferBankAccount(ctx, toCashBook.getAD_Org_ID(), currencyId, null);
        MCash toCash = MCash.get(ctx, toCashBookId, dateAcct, trxName);
        String description = fromCashBook.getName() + " (CB) -> " + toCashBook.getName() + " (CB)";
        MCashLine fromCashLine = new MCashLine(cashJournal);
        fromCashLine.setDescription(description);
        fromCashLine.setC_Currency_ID(currencyId);
        fromCashLine.setCashType(MCashLine.CASHTYPE_BankAccountTransfer);
        fromCashLine.setC_BankAccount_ID(fromBankAccountId);
        fromCashLine.setAmount(amount.negate());
        fromCashLine.setIsGenerated(true);
        PoManager.save(fromCashLine);
        if (fromBankAccountId != toBankAccountId) {
            transferBankToBank(ctx, fromBankAccountId, toBankAccountId, currencyId, amount, dateAcct, trxName);
        }
        MCashLine toCashLine = new MCashLine(toCash);
        toCashLine.setDescription(description);
        toCashLine.setC_Currency_ID(currencyId);
        toCashLine.setCashType(MCashLine.CASHTYPE_BankAccountTransfer);
        toCashLine.setC_BankAccount_ID(toBankAccountId);
        toCashLine.setAmount(amount);
        toCashLine.setIsGenerated(true);
        PoManager.save(toCashLine);
    }
