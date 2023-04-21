    public static String sendGetRequest(String endpoint, String requestParameters) {
        String result = null;
        if (true) {
            try {
                Properties sysProperties = System.getProperties();
                sysProperties.put("proxyHost", Constants.PROXY);
                sysProperties.put("proxyPort", Constants.PROXYPORT);
                sysProperties.put("proxySet", Constants.PROXYENABLED);
                Authenticator.setDefault(new MyAuthenticator());
                String urlStr = endpoint;
                if (requestParameters != null && requestParameters.length() > 0) {
                    urlStr += "?" + requestParameters;
                }
                java.net.URL url = new java.net.URL(urlStr);
                java.lang.System.out.println(urlStr);
                URLConnection conn = url.openConnection();
                BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()), 8000);
                StringBuffer sb = new StringBuffer();
                String line;
                while ((line = rd.readLine()) != null) {
                    sb.append(line);
                }
                rd.close();
                result = sb.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }
