    protected byte[] getRequestHash(String rqFile) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA");
            FileInputStream fis = new FileInputStream(rqFile);
            byte[] content = new byte[fis.available()];
            int nb = fis.read(content);
            while (nb != -1) {
                md.update(content, 0, nb);
                nb = fis.read(content);
            }
            byte[] result = md.digest();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
