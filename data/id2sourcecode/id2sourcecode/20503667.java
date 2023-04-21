    private static String hashIt(String path) {
        String hash = "";
        InputStream input = null;
        try {
            File theFile = new File(path);
            input = new FileInputStream(theFile);
            byte[] buffer = new byte[8192];
            int read = 0;
            while ((read = input.read(buffer)) > 0) {
                digest_.update(buffer, 0, read);
            }
            byte[] hashValues = digest_.digest();
            BigInteger bigInt = new BigInteger(1, hashValues);
            hash = bigInt.toString(16);
        } catch (NullPointerException exc) {
            exc.printStackTrace();
            log_.appendString("Problem encountered hashing file: " + path);
        } catch (FileNotFoundException exc) {
            exc.printStackTrace();
            log_.appendString("Problem encountered hashing file: " + path);
        } catch (SecurityException exc) {
            exc.printStackTrace();
            log_.appendString("Problem encountered hashing file: " + path);
        } catch (NumberFormatException exc) {
            exc.printStackTrace();
            log_.appendString("Problem encountered hashing file: " + path);
        } catch (IOException exc) {
            exc.printStackTrace();
            log_.appendString("Problem encountered hashing file: " + path);
        } finally {
            try {
                input.close();
            } catch (IOException exc) {
                exc.printStackTrace();
                log_.appendString("Problem encountered hashing file: " + path);
            }
        }
        while (hash.length() < length_) {
            hash = "0" + hash;
        }
        return hash;
    }
