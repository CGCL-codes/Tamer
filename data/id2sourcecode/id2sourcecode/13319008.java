    public String storeTransaction(Transaction transaction) {
        if (this.isReadOnly()) {
            System.out.println("Attempting to write new transaction to read only repository");
            return null;
        }
        boolean wasDisconnected = !this.isConnected();
        if (wasDisconnected && !this.connect()) {
            return null;
        }
        try {
            String transactionUUID = transaction.uuid;
            String resourceFileReference = null;
            System.out.println("Writing transaction: " + transactionUUID);
            resourceFileReference = this.basicStoreTransaction(transaction);
            if (resourceFileReference == null) {
                System.out.println("Could not add transaction.");
            }
            if (transactionCache != null) this.transactionCache.addTransaction(transaction);
            return resourceFileReference;
        } finally {
            if (wasDisconnected && !this.disconnect()) {
                System.out.println("Disconnection failed");
            }
        }
    }
