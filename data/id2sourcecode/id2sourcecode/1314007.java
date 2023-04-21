    private String readTemplate(String template) throws IOException {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        URL url = cl.getResource(template);
        InputStream ins = null;
        try {
            ins = url.openStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(ins));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + LINE_SEP);
            }
            reader.close();
            return sb.toString();
        } finally {
            if (ins != null) {
                ins.close();
            }
        }
    }
