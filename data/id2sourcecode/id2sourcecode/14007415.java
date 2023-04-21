    private static String request(HttpPost httpRequest, List<NameValuePair> params) {
        try {
            httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpResponse httpResponse = httpClient.execute(httpRequest);
            List<Cookie> list = httpClient.getCookieStore().getCookies();
            for (Cookie cookie : list) {
                Log.d(cookie.getName(), cookie.getValue());
                sessionId = cookie.getValue();
            }
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                String strResult = EntityUtils.toString(httpResponse.getEntity());
                Log.d(TAG, "strResult=" + strResult);
                return strResult;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
