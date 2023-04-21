    private String readFile(String test, String version) throws Exception {
        String filename = test;
        if (version != null) {
            filename += "_" + version;
        }
        File file = new File(getTestFileDirectory(), filename + ".properties");
        FileInputStream fileInputStream = new FileInputStream(file);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        while (fileInputStream.available() > 0) {
            int read = fileInputStream.read(buffer);
            baos.write(buffer, 0, read);
        }
        return new String(baos.toByteArray()).replaceAll("\\x0d\\x0a", System.getProperty("line.separator", "\n"));
    }
