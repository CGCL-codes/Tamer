    public void transferMoneyTo(MoneySink sink, int amount) throws InsufficientFundsException {
        tm.begin();
        boolean failed = false;
        try {
            if (availableBalance() - amount < 0) {
                failed = true;
                throw new InsufficientFundsException("Available balance:" + availableBalance());
            }
            withdraw(amount);
            sink.transferFrom(this, amount);
            gui.message("Transferred " + amount);
        } finally {
            tm.end(failed);
        }
    }
