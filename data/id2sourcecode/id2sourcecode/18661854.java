    public static File createTestfile_40k() {
        String filename = null;
        URL url = QAUtil.class.getResource("Testfile_40k.html");
        try {
            if (url != null) {
                InputStream is = url.openConnection().getInputStream();
                File file = createTempfile(is, ".html");
                is.close();
                return file;
            } else {
                filename = new File("src" + File.separator + "test" + File.separator + "resources" + File.separator + "org" + File.separator + "xsocket" + File.separator + "Testfile_40k.html").getAbsolutePath();
                FileInputStream fis = new FileInputStream(filename);
                File file = QAUtil.createTempfile(fis, ".html");
                fis.close();
                return file;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
