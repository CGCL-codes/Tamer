    private static String[] splitTemplate(String rsc) throws IOException {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(rsc);
        if (is == null) {
            throw new FileNotFoundException(rsc);
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buff = new byte[512];
        int read;
        while ((read = is.read(buff)) != -1) {
            baos.write(buff, 0, read);
        }
        String str = baos.toString();
        return str.split("@@");
    }
