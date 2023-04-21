    public void createMD5Checksum() {
        checksum.setText("Checking...");
        try {
            InputStream fis = new FileInputStream(file.getText());
            byte[] buffer = new byte[1024];
            MessageDigest complete = MessageDigest.getInstance("MD5");
            int numRead;
            do {
                numRead = fis.read(buffer);
                if (numRead > 0) {
                    complete.update(buffer, 0, numRead);
                }
            } while (numRead != -1);
            fis.close();
            checksum.setText(new BigInteger(1, complete.digest()).toString(16));
        } catch (Exception e) {
            checksum.setText("Error:  " + e.getMessage());
        }
    }
