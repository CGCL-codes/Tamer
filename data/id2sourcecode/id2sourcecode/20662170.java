    public static String execute(final HttpUriRequest request, final Configuration configuration) {
        HttpClient httpClient = new DefaultHttpClient();
        StringBuilder response = new StringBuilder("");
        try {
            httpClient.getParams().setParameter("http.socket.timeout", configuration.getReadTimeout());
            httpClient.getParams().setParameter("http.protocol.content-charset", "UTF-8");
            HttpResponse httpResponse = httpClient.execute(request);
            HttpEntity httpEntity = httpResponse.getEntity();
            StatusLine statusLine = httpResponse.getStatusLine();
            if ((statusLine.getStatusCode() == HTTP_OK || statusLine.getStatusCode() == HTTP_ISE) && httpEntity.getContentType().getValue().contains("application/xml")) {
                InputStreamReader inputStreamReader = new InputStreamReader(httpEntity.getContent());
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    response.append(line);
                    response.append('\n');
                }
                bufferedReader.close();
            } else {
                httpEntity.consumeContent();
                throw new AssertionError("Unexpected HTTP status received: " + statusLine.toString());
            }
        } catch (ClientProtocolException e) {
            throw new HttpClientException(e);
        } catch (IOException e) {
            throw new HttpClientException(e);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        return response.toString();
    }
