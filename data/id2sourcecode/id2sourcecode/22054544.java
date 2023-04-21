    private static Image createImage(InputStream is) {
        try {
            if (is == null) return null;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int scan = 0;
            while ((scan = is.read()) != -1) bos.write(scan);
            bos.flush();
            bos.close();
            return tk.createImage(bos.toByteArray());
        } catch (IOException ex) {
            return null;
        }
    }
