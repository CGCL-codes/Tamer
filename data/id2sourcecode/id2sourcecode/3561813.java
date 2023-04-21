    public static JSONArray Get(String pathSegment, int timeoutConnection, int timeoutSocket) throws ClientProtocolException, IOException, JSONException {
        try {
            URI url = URI.create(Variables.BASE_URI + pathSegment);
            HttpGet get = new HttpGet(url);
            DefaultHttpClient client = new DefaultHttpClient();
            if (timeoutConnection > 0 && timeoutSocket > 0) {
                HttpParams httpParameters = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
                HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
                client.setParams(httpParameters);
            }
            HttpResponse response = client.execute(get);
            return parseResponse(response);
        } catch (ConnectTimeoutException e) {
            return null;
        } catch (UnknownHostException e1) {
            return null;
        } catch (HttpHostConnectException e1) {
            return null;
        } catch (SocketException e1) {
            return null;
        } catch (SocketTimeoutException e1) {
            return null;
        } catch (SSLPeerUnverifiedException e1) {
            return null;
        } catch (ClientProtocolException e2) {
            return null;
        } catch (IOException e3) {
            throw e3;
        } catch (JSONException e4) {
            throw e4;
        }
    }
