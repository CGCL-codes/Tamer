    private static int downLoadFile(Context context, String serverUrl, String fileName) {
        HttpParams params = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params, 5 * 1000);
        HttpConnectionParams.setSoTimeout(params, 5 * 1000);
        HttpClient client = new DefaultHttpClient(params);
        int statusCode = ControllerException.CONTROLLER_UNAVAILABLE;
        try {
            URL uri = new URL(serverUrl);
            if ("https".equals(uri.getProtocol())) {
                Scheme sch = new Scheme(uri.getProtocol(), new SelfCertificateSSLSocketFactory(), uri.getPort());
                client.getConnectionManager().getSchemeRegistry().register(sch);
            }
            HttpGet get = new HttpGet(serverUrl);
            SecurityUtil.addCredentialToHttpRequest(context, get);
            HttpResponse response = client.execute(get);
            statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == Constants.HTTP_SUCCESS) {
                FileOutputStream fOut = context.openFileOutput(fileName, Context.MODE_PRIVATE);
                InputStream is = response.getEntity().getContent();
                byte buf[] = new byte[1024];
                int len;
                while ((len = is.read(buf)) > 0) {
                    fOut.write(buf, 0, len);
                }
                fOut.close();
                is.close();
            }
        } catch (MalformedURLException e) {
            Log.e("OpenRemote-HTTPUtil", "Create URL fail:" + serverUrl);
        } catch (IllegalArgumentException e) {
            Log.e("OpenRemote-IllegalArgumentException", "Download file " + fileName + " failed with URL: " + serverUrl, e);
        } catch (ClientProtocolException cpe) {
            Log.e("OpenRemote-ClientProtocolException", "Download file " + fileName + " failed with URL: " + serverUrl, cpe);
        } catch (IOException ioe) {
            Log.e("OpenRemote-IOException", "Download file " + fileName + " failed with URL: " + serverUrl, ioe);
        }
        return statusCode;
    }
