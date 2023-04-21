    private String httpRequest(HttpRequestBase request) {
        String strResponse = "";
        try {
            HttpResponse response = client.execute(request);
            HttpEntity entity = response.getEntity();
            strResponse = EntityUtils.toString(entity);
            LOG.d("Http Response", strResponse);
        } catch (ClientProtocolException e) {
            LOG.e(e);
        } catch (IOException e) {
            LOG.e(e);
        } catch (Exception e) {
            LOG.e(e);
        }
        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return strResponse;
    }
