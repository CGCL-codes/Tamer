    public static HttpData getRequest(HttpGet getMethod) throws SocketTimeoutException, SocketException, ClientProtocolException, IOException {
        String statusCode = null;
        HttpData data = new HttpData();
        try {
            getMethod.addHeader("Content-Type", "text/xml; charset=utf-8");
            getMethod.addHeader("User-Agent", "Openwave");
            printGetRequestHeader(getMethod);
            HttpClient client = new DefaultHttpClient(new BasicHttpParams());
            client.getParams().setIntParameter(HttpConnectionParams.CONNECTION_TIMEOUT, DEFAULT_GET_REQUEST_TIMEOUT);
            client.getParams().setIntParameter(HttpConnectionParams.SO_TIMEOUT, DEFAULT_GET_REQUEST_TIMEOUT);
            HttpResponse httpResponse = client.execute(getMethod);
            printResponseHeader(httpResponse, data);
            data.setStatusCode(statusCode);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                byte byteArray[] = retrieveInputStream(httpResponse.getEntity());
                if (byteArray != null) data.setHttpLength(Integer.toString(byteArray.length));
                data.setByteArray(byteArray);
                if (byteArray != null) {
                    Log.i(TAG, "Response Data:" + new String(byteArray, "utf-8"));
                } else {
                    Log.e(TAG, "None Response Data");
                }
            } else {
                Log.e(TAG, "httpResponse.getStatusLine().getStatusCode():" + httpResponse.getStatusLine().getStatusCode());
            }
        } catch (SocketTimeoutException e) {
            throw new SocketTimeoutException();
        } catch (SocketException e) {
            throw new SocketException();
        } catch (ClientProtocolException e) {
            throw new ClientProtocolException();
        } catch (IOException e) {
            throw new IOException();
        } finally {
            getMethod.abort();
        }
        return data;
    }
