    public void run() {
        try {
            HttpResponse response = httpClient.execute(httpGet, context);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                byte[] bytes = EntityUtils.toByteArray(entity);
                System.out.println(id + " - " + bytes.length + " bytes read");
            }
        } catch (Exception ex) {
            System.out.println(id + " - error: " + ex);
        } finally {
            httpGet.abort();
        }
    }
