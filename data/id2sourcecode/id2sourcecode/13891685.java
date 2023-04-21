    protected static Serializable buildValueFromFile(String key, Cipher aCipher, String encryptedKey) {
        Serializable aFoundEntity = null;
        try {
            KVStore.claimSemaphore(key, 1);
        } catch (InterruptedException e) {
            theListener.errorHappened(key, null, new KVStorageException("Persistance failure", e));
            return null;
        }
        try {
            FileInputStream persistanceFileInputStream = theActivity.openFileInput(encryptedKey);
            long size = persistanceFileInputStream.getChannel().size();
            if (size == 0) {
                return null;
            }
            byte[] fileBytes = new byte[(int) size];
            persistanceFileInputStream.read(fileBytes);
            persistanceFileInputStream.close();
            if (aCipher != null) {
                fileBytes = aCipher.doFinal(fileBytes);
            }
            String textString = new String(fileBytes);
            aFoundEntity = (Serializable) JSONUtilities.parse(textString);
        } catch (Exception e) {
            if (theListener != null) {
                theListener.errorHappened(key, null, new KVStorageException("Persistance retrieval error", e));
            }
            KVStore.releaseSemaphore(key, 1);
            return null;
        }
        KVStore.releaseSemaphore(key, 1);
        return aFoundEntity;
    }
