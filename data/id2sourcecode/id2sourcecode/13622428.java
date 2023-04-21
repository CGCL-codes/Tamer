    public byte[] verifyFile(String fileName) throws RemoteException {
        try {
            return MessageDigest.getInstance("MD5").digest(downloadFile(fileName));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
