    private Update parseUpdate(String update) {
        System.out.println(update);
        try {
            String[] parts = update.split(":");
            Update dl = new Update(Integer.valueOf(parts[1]), parts[3]);
            File file = new File(parts[3]);
            if (file.exists()) {
                dl.setUpdateExists(true);
                MessageDigest digest = MessageDigest.getInstance("MD5");
                InputStream is = new FileInputStream(file);
                byte[] buffer = new byte[8192];
                int read = 0;
                while ((read = is.read(buffer)) > 0) {
                    digest.update(buffer, 0, read);
                }
                byte[] md5sum = digest.digest();
                BigInteger bigInt = new BigInteger(1, md5sum);
                String output = bigInt.toString(16);
                if (output.length() < 32) {
                    int remaining = 32 - output.length();
                    for (int i = 0; i != remaining; ++i) {
                        output = "0" + output;
                    }
                }
                is.close();
                if (!output.equals(parts[2])) {
                    return dl;
                }
            } else {
                dl.setUpdateExists(false);
                return dl;
            }
            return null;
        } catch (Exception e) {
            org.opencdspowered.opencds.core.logging.Logger.getInstance().logException(e);
            return null;
        }
    }
