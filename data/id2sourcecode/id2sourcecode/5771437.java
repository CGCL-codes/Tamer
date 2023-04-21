    public void addAndWriteTransaction(Transaction transaction) {
        if (backend.isReadOnly()) {
            System.out.println("Attempting to write new transaction to read only backend");
            return;
        }
        String transactionIdentifier = transaction.identifier;
        Boolean added = false;
        if (backend.supportsTransactionObjects()) {
            added = backend.storeTransactionForTransactionIdentifier(transactionIdentifier, transaction);
        } else {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            transaction.writeToUTF8Stream(outputStream);
            byte[] bytes = outputStream.toByteArray();
            InputStream inputStream = new ByteArrayInputStream(bytes);
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            added = backend.storeDataForTransactionIdentifier(transactionIdentifier, inputStream, bytes.length);
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (added) {
            addTransactionToCache(transactionIdentifier, transaction);
        } else {
            System.out.println("Could not add transaction.");
        }
    }
