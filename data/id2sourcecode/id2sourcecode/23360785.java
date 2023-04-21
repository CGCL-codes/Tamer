    public void transferFrom(MoneySource source, int amount) {
        deposit(amount);
        gui.message("Deposited " + amount);
    }
