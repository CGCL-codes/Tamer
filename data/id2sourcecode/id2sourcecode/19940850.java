    private String fireGetRequest(String urlForSalesData, Hashtable<String, String> headers) throws IOException {
        String PROXY_SERVER = this.httpProxy;
        String PROXY_PORT = Integer.toString(this.httpPort);
        InputStream inputStream = null;
        BufferedReader bufferedReader = null;
        DataInputStream input = null;
        StringBuffer sBuf = new StringBuffer();
        Proxy proxy;
        if (PROXY_SERVER != null && PROXY_PORT != null) {
            SocketAddress address = new InetSocketAddress(PROXY_SERVER, Integer.parseInt(PROXY_PORT));
            proxy = new Proxy(Proxy.Type.HTTP, address);
        } else {
            proxy = null;
        }
        proxy = null;
        URL url;
        try {
            url = new URL(urlForSalesData);
            HttpURLConnection httpUrlConnection;
            if (proxy != null) httpUrlConnection = (HttpURLConnection) url.openConnection(proxy); else httpUrlConnection = (HttpURLConnection) url.openConnection();
            httpUrlConnection.setDoInput(true);
            httpUrlConnection.setRequestMethod("GET");
            if (headers != null) {
                Enumeration<String> e = headers.keys();
                while (e.hasMoreElements()) {
                    String key = (String) e.nextElement();
                    httpUrlConnection.addRequestProperty(key, URLDecoder.decode(headers.get(key), "UTF-8"));
                }
            }
            httpUrlConnection.connect();
            sBuf.append("<responseCode>" + httpUrlConnection.getResponseCode() + "</responseCode>");
            System.out.println(httpUrlConnection.getResponseMessage());
            inputStream = httpUrlConnection.getInputStream();
            input = new DataInputStream(inputStream);
            bufferedReader = new BufferedReader(new InputStreamReader(input));
            String str;
            while (null != ((str = bufferedReader.readLine()))) {
                sBuf.append(str);
            }
            return sBuf.toString();
        } catch (MalformedURLException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        }
    }
