    private String getSHA1(File outputFile) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(outputFile));
            byte[] digest;
            String hash = "";
            long size = outputFile.length();
            byte[] data = new byte[size > BUFFER_SIZE ? BUFFER_SIZE : (int) (size)];
            long i = 0;
            while (bis.read(data) > 0) {
                md.update(data);
                i += data.length;
                if (size - i < BUFFER_SIZE) data = new byte[(int) (size - i)];
            }
            digest = md.digest();
            for (byte aux : digest) {
                int b = aux & 0xff;
                if (Integer.toHexString(b).length() == 1) {
                    hash += "0";
                }
                hash += Integer.toHexString(b);
            }
            return hash;
        } catch (FileNotFoundException ex) {
            return null;
        } catch (NoSuchAlgorithmException ex) {
            return null;
        } catch (IOException ex) {
            return null;
        }
    }
