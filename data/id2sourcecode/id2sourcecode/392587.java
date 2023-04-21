    public static String doGetPage(final String urlString, final String cookieString, final String addToString) {
        StringBuffer sb = new StringBuffer(addToString);
        HttpURLConnection conn = null;
        String location = null;
        try {
            URL url = new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();
            if (conn instanceof HttpsURLConnection) {
                HttpsURLConnection sconn = (HttpsURLConnection) conn;
                SSLContext sslContext = SSLContext.getInstance("SSL");
                sslContext.init(null, new X509TrustManager[] { new X509TrustManager() {

                    public void checkClientTrusted(X509Certificate[] chain, String authType) {
                    }

                    public void checkServerTrusted(X509Certificate[] chain, String authType) {
                    }

                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }
                } }, null);
                sconn.setSSLSocketFactory(sslContext.getSocketFactory());
                sconn.setHostnameVerifier(new HostnameVerifier() {

                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                });
            }
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setInstanceFollowRedirects(false);
            if (cookieString != null) {
                conn.setRequestProperty("Cookie", cookieString);
            }
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String str;
            int n = 1;
            boolean done = false;
            while (!done) {
                String headerKey = conn.getHeaderFieldKey(n);
                String headerVal = conn.getHeaderField(n);
                if (headerKey != null || headerVal != null) {
                    if (headerKey.equals("Location")) {
                        location = headerVal;
                    } else if (headerKey.equals("Set-Cookie")) {
                        cookie = headerVal;
                    }
                    System.out.println(headerKey + " = " + headerVal);
                } else {
                    done = true;
                }
                n++;
            }
            while ((str = in.readLine()) != null) {
                sb.append(str + "\n");
            }
            in.close();
            if (location != null) {
                conn.disconnect();
                return doGetPage(location, cookie, sb.toString());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException("getPage: " + e.getClass().getName() + ":" + e.getMessage() + "...");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("getPage: " + e.getClass().getName() + ":" + e.getMessage() + "...");
        } catch (Exception e) {
            e.printStackTrace();
            isJavaReady = false;
        } finally {
            if (conn != null) conn.disconnect();
        }
        return sb.toString();
    }
