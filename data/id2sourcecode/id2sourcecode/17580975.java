    public static List<SalesOrderServiceBean> fireGetRequest(LoggerHelper.RecordType reqType, String urlForSalesData, HttpServletRequest request) throws IOException {
        AtomParser atomParser = new AtomParser();
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
            httpUrlConnection.addRequestProperty("token", (String) request.getSession().getAttribute("acsToken"));
            httpUrlConnection.addRequestProperty("solutionName", (String) request.getSession().getAttribute("solutionName"));
            httpUrlConnection.connect();
            System.out.println(httpUrlConnection.getResponseMessage());
            if (httpUrlConnection.getResponseCode() == HttpServletResponse.SC_UNAUTHORIZED) {
                List<SalesOrderServiceBean> salesOrderServiceBean = new ArrayList<SalesOrderServiceBean>();
                SalesOrderServiceBean response = new SalesOrderServiceBean();
                response.setResponseCode(HttpServletResponse.SC_UNAUTHORIZED);
                response.setResponseMessage(httpUrlConnection.getResponseMessage());
                salesOrderServiceBean.add(response);
                return salesOrderServiceBean;
            }
            inputStream = httpUrlConnection.getInputStream();
            input = new DataInputStream(inputStream);
            bufferedReader = new BufferedReader(new InputStreamReader(input));
            String str;
            while (null != ((str = bufferedReader.readLine()))) {
                sBuf.append(str);
            }
            String responseString = sBuf.toString();
            List<SalesOrderServiceBean> salesOrderServiceBean = new ArrayList<SalesOrderServiceBean>();
            salesOrderServiceBean = atomParser.parseString(responseString);
            return salesOrderServiceBean;
        } catch (MalformedURLException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        }
    }
