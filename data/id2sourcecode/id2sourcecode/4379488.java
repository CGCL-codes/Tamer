    private StringBuffer copyToBuffer(InputStream inputStream) {
        if (inputStream == null) {
            return null;
        }
        StringWriter stringWriter = new StringWriter();
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        try {
            String s;
            while ((s = br.readLine()) != null) stringWriter.write(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringWriter.getBuffer();
    }
