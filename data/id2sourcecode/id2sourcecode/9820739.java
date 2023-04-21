    private byte[] getTextHash(File file) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.reset();
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            try {
                while (true) {
                    String line = in.readLine();
                    if (line == null) {
                        break;
                    }
                    byte[] buffer = line.getBytes();
                    digest.update(buffer);
                }
            } finally {
                in.close();
            }
            return digest.digest();
        } catch (NoSuchAlgorithmException ex) {
            throw new BuildException("Cannot compute file hash.", ex);
        } catch (IOException ex) {
            throw new BuildException("Cannot compute file hash.", ex);
        }
    }
