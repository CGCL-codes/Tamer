    public static String getMD5Checksum(String filename) {
        try {
            InputStream fis = new FileInputStream(filename);
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
            byte[] b = complete.digest();
            String result = "";
            for (int i = 0; i < b.length; i++) {
                result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
            }
            return result;
        } catch (FileNotFoundException fnf) {
            logger.warn("FileNotFoundException thrown: " + fnf.getLocalizedMessage());
            return "";
        } catch (NoSuchAlgorithmException nsa) {
            logger.warn("NoSuchAlgorithmException thrown: " + nsa.getLocalizedMessage());
            return "";
        } catch (IOException io) {
            logger.warn("IOException thrown: " + io.getLocalizedMessage());
            return "";
        }
    }
