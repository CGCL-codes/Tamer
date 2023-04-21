    private static String getData(String url) throws IOException {
        HttpGet httpget = new HttpGet(url);
        HttpResponse response = httpclient.execute(httpget);
        HttpEntity entity = response.getEntity();
        if (entity == null) {
            throw new IOException();
        }
        StringBuffer sb = new StringBuffer();
        BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent()));
        String line;
        while (null != (line = br.readLine())) sb.append(line + "\n");
        entity.consumeContent();
        return sb.toString();
    }
