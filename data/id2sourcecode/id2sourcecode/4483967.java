    public static String readFile(File file) {
        try {
            if (file.length() > (5 * 1000 * 1000)) throw new RuntimeException("the file is " + file.length() + " bytes. that is too large. it can't be larger than 5000000 bytes.");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            FileInputStream fis = new FileInputStream(file);
            copy(fis, baos);
            fis.close();
            baos.flush();
            baos.close();
            return new String(baos.toByteArray(), "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
