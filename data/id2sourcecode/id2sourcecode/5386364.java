    public String readURL(URL url) throws IOException {
        URLConnection uc = url.openConnection();
        InputStream content = (InputStream) uc.getInputStream();
        BufferedReader in = new BufferedReader(new InputStreamReader(content));
        String line;
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        while ((line = in.readLine()) != null) {
            pw.println(line);
        }
        return sw.toString();
    }
