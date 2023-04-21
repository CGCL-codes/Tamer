    protected List<String> search(String term) {
        List<String> result = new LinkedList<String>();
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://www.pdb.org/pdb/rest/search/");
        Log.i("JMOL", "Searching for " + term);
        try {
            httppost.setHeader("Content-Type", "application/x-www-form-urlencoded");
            httppost.setEntity(new StringEntity(URLEncoder.encode(String.format(query, term), "UTF-8")));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                BufferedReader reader = new BufferedReader(new StringReader(EntityUtils.toString(entity)));
                String id = null;
                while ((id = reader.readLine()) != null) result.add(id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("JMOL", "Found " + result);
        return result;
    }
