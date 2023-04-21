    public String getHash(File file, Algorithms algorithm) {
        int fileSize = (int) file.length();
        try {
            MessageDigest md = MessageDigest.getInstance(algorithmNames.get(algorithm));
            DigestInputStream dis = new DigestInputStream(new FileInputStream(file), md);
            int numRead = 0;
            int prevPercentComplete = 0;
            try {
                while (dis.available() > 0) {
                    dis.read();
                    numRead++;
                    int percentComplete = Math.round(((float) numRead) / fileSize * 100);
                    if (percentComplete > prevPercentComplete) {
                        prevPercentComplete = percentComplete;
                        notifyListeners(percentComplete);
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return formatHash(dis.getMessageDigest().digest());
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }
