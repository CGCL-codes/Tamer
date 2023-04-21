    public void stressTest(int dataStorages, int amountPerStorage) {
        try {
            System.out.println(dataStorages + " storages with " + amountPerStorage + " entries in each ( total of " + (dataStorages * amountPerStorage) + " entries)");
            MessageDigest digester = MessageDigest.getInstance("SHA-1");
            Runtime runtime = Runtime.getRuntime();
            System.gc();
            long initialUsedMemo = runtime.totalMemory() - runtime.freeMemory();
            DataStorage[] storages = new DataStorage[dataStorages];
            for (int i = 0; i < dataStorages; i++) {
                DataStorage storage = new DataStorage();
                for (int j = 0; j < amountPerStorage; j++) {
                    String value = j + "01234567890123456789012345678901234567890123456789";
                    byte[] hashed = digester.digest(value.getBytes());
                    BigInteger bigInt = new BigInteger(hashed);
                    storage.put(bigInt, value);
                }
                storages[i] = storage;
            }
            System.gc();
            long finalUsedMemo = runtime.totalMemory() - runtime.freeMemory();
            long delta = finalUsedMemo - initialUsedMemo;
            long storageUse = delta / dataStorages;
            long entryUse = storageUse / amountPerStorage;
            System.out.println("Initial used memo: " + initialUsedMemo + " bytes (" + (initialUsedMemo / 1024) + " kb)");
            System.out.println("Final used memo:   " + finalUsedMemo + " bytes (" + (finalUsedMemo / 1024) + " kb)");
            System.out.println("Delta:             " + delta + " bytes (" + (delta / 1024) + " kb)");
            System.out.println("Amount used per storage (average): " + storageUse + " bytes (" + ((double) storageUse / 1024.0) + " kb)");
            System.out.println("Amount used per entry (average):   " + entryUse + " bytes (" + ((double) entryUse / 1024.0) + " kb)");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
