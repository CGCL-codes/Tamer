    public void updateDocument(Map<String, Object> docMap) {
        HttpPost post = new HttpPost("http://localhost/solr/update/json?commit=true");
        post.setHeader("Content-type", "application/json");
        try {
            post.setEntity(new StringEntity("[" + new JSONObject(docMap).toString() + "]", "utf-8"));
            HttpResponse result = httpClient.execute(post);
            int code = result.getStatusLine().getStatusCode();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }
