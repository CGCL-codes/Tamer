        private boolean upload(String filePath, String fileName, int chunkNum, boolean chunkFinal) throws Exception {
            TrustManager easyTrustManager = new X509TrustManager() {

                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            SSLContext sslcontext = SSLContext.getInstance("TLS");
            sslcontext.init(null, new TrustManager[] { easyTrustManager }, null);
            SSLSocketFactory sf = new SSLSocketFactory(sslcontext);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            Scheme https = new Scheme("https", sf, 443);
            DefaultHttpClient httpclient = new DefaultHttpClient();
            httpclient.getConnectionManager().getSchemeRegistry().register(https);
            ProxySelectorRoutePlanner routePlanner = new ProxySelectorRoutePlanner(httpclient.getConnectionManager().getSchemeRegistry(), ProxySelector.getDefault());
            httpclient.setRoutePlanner(routePlanner);
            HttpPost httppost = new HttpPost(uploadURL);
            httppost.addHeader("Cookie", browserCookie);
            httppost.getParams().setBooleanParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, false);
            MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            reqEntity.addPart("chunk_num", new StringBody(Integer.toString(chunkNum)));
            if (chunkFinal) {
                reqEntity.addPart("chunk_final", new StringBody("1"));
            }
            FileBody uplFile = new FileBody(new File(filePath));
            reqEntity.addPart("file", uplFile);
            reqEntity.addPart("filename", new StringBody(fileName));
            httppost.setEntity(reqEntity);
            HttpResponse response = httpclient.execute(httppost);
            if (response.getStatusLine().getStatusCode() != 200) {
                return false;
            }
            return true;
        }
