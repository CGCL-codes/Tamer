    public static byte[] md5(File archivo) {
        try {
            FileInputStream fis = new FileInputStream(archivo);
            MessageDigest messagedigest = MessageDigest.getInstance("MD5");
            byte[] buff = new byte[4092];
            int r;
            while ((r = fis.read(buff, 0, 4092)) != -1) {
                messagedigest.update(buff, 0, r);
            }
            return messagedigest.digest();
        } catch (FileNotFoundException e) {
            return new byte[] {};
        } catch (NoSuchAlgorithmException e) {
            return new byte[] {};
        } catch (IOException e) {
            return new byte[] {};
        }
    }
