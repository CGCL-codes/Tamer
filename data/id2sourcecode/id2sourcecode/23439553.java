    private void fillStorage() {
        DataManagerFacade<String> storage = DataManagerFacade.getDataManager();
        for (int i = 0; i < values.length; i++) {
            byte[] hashed = digester.digest(values[i][0].getBytes());
            storage.put(new BigInteger(hashed), values[i][1]);
        }
    }
