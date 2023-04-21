    public static void main(String[] args) {
        long begin = System.currentTimeMillis();
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet("http://localhost:8983/solr/select?q=name:monsters&wt=json&indent=true");
            HttpResponse response = httpclient.execute(httpget);
            InputStream is = response.getEntity().getContent();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(System.currentTimeMillis() - begin + " ms");
    }
