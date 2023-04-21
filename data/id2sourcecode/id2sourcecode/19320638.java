    public void postMidia(String url, String nomeMidia, String caminhoMidia) throws Exception {
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost postRequest = new HttpPost(new URI(url));
            MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            FileBody bin = new FileBody(new File(caminhoMidia));
            reqEntity.addPart("attachment_field", bin);
            reqEntity.addPart("nomeMidia", new StringBody(nomeMidia));
            postRequest.setEntity(reqEntity);
            HttpResponse response = httpClient.execute(postRequest);
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            String sResponse;
            StringBuilder s = new StringBuilder();
            while ((sResponse = reader.readLine()) != null) {
                s = s.append(sResponse);
            }
        } catch (Exception e) {
            Log.e(e.getClass().getName(), e.getMessage());
        }
    }
