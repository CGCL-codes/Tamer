    protected String readFile(String path) throws IOException {
        StringWriter sw = new StringWriter();
        BufferedReader r = null;
        try {
            r = new BufferedReader(new InputStreamReader(new FileInputStream(getFile(path)), "ISO-8859-1"));
            char[] buf = new char[4096];
            int bread;
            while ((bread = r.read(buf)) > 0) sw.write(buf, 0, bread);
            return sw.toString();
        } finally {
            if (r != null) try {
                r.close();
            } catch (IOException e) {
            }
        }
    }
