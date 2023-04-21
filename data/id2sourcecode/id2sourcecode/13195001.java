    public static void main(String[] args) throws Exception {
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet("http://www.tianya.cn/publicforum/content/news/1/91217.shtml");
        HttpResponse response = httpclient.execute(httpget);
        System.out.println(response.getStatusLine());
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            InputStream instream = entity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(instream, "GBK"));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            instream.close();
        }
    }
