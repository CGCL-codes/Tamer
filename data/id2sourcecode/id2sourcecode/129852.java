    @org.junit.Test
    public void checkUpdateSalesOrders() throws IOException, SQLException, Exception {
        long salesPersonId = 1;
        long productId = 1;
        long orderQty = 13;
        long salesOrderId = 1;
        String query = "SELECT * FROM SalesOrder WHERE SalesOrderID= " + salesOrderId;
        resultSet = statement.executeQuery(query);
        List<TestSalesOrderService> expectedProductList = new ArrayList<TestSalesOrderService>();
        while (resultSet.next()) {
            TestSalesOrderService salesOrderBean = new TestSalesOrderService();
            salesOrderBean.setOrderQuantity(13);
            salesOrderBean.setProductID("1");
            salesOrderBean.setSalesOrderID("1");
            expectedProductList.add(salesOrderBean);
        }
        String urlForSalesData = "http://localhost:8080/SalesOrderService/SalesOrder/SalesOrderData";
        DataOutputStream outputStream = null;
        InputStream inputStream = null;
        BufferedReader bufferedReader = null;
        DataInputStream input = null;
        StringBuffer sBuf = new StringBuffer();
        URL url = null;
        try {
            url = new URL(urlForSalesData);
            HttpURLConnection httpUrlConnection;
            httpUrlConnection = (HttpURLConnection) url.openConnection();
            httpUrlConnection.setRequestMethod("POST");
            httpUrlConnection.setUseCaches(false);
            httpUrlConnection.setDoInput(true);
            httpUrlConnection.setDoOutput(true);
            httpUrlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpUrlConnection.setRequestProperty("Content-Language", "en-US");
            httpUrlConnection.setRequestProperty("Accept", "*/*");
            httpUrlConnection.setRequestProperty("Authorization", "Required");
            String name = rb.getString("WRAP_NAME");
            String password = rb.getString("WRAP_PASSWORD");
            Credentials simpleAuthCrentials = new Credentials(TOKEN_TYPE.SimpleApiAuthToken, name, password);
            ACSTokenProvider tokenProvider = new ACSTokenProvider(httpWebProxyServer, httpWebProxyPort, simpleAuthCrentials);
            String requestUriStr1 = "https://" + solutionName + "." + acmHostName + "/" + serviceName;
            String appliesTo1 = rb.getString("SIMPLEAPI_APPLIES_TO");
            String token = tokenProvider.getACSToken(requestUriStr1, appliesTo1);
            httpUrlConnection.addRequestProperty("token", "WRAPv0.9 " + token);
            httpUrlConnection.addRequestProperty("solutionName", solutionName);
            StringBuilder postData = new StringBuilder();
            postData.append("productId=" + URLEncoder.encode(Long.toString(productId), "UTF-8"));
            postData.append("&");
            postData.append("orderQty=" + URLEncoder.encode(Long.toString(orderQty), "UTF-8"));
            postData.append("&");
            postData.append("salesOrderId=" + URLEncoder.encode(Long.toString(salesOrderId), "UTF-8"));
            outputStream = new DataOutputStream(httpUrlConnection.getOutputStream());
            outputStream.writeBytes(postData.toString());
            outputStream.flush();
            inputStream = httpUrlConnection.getInputStream();
            if (httpUrlConnection.getResponseCode() == HttpServletResponse.SC_UNAUTHORIZED) {
                return;
            }
            input = new DataInputStream(inputStream);
            bufferedReader = new BufferedReader(new InputStreamReader(input));
            String str;
            while (null != ((str = bufferedReader.readLine()))) {
                sBuf.append(str);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String queryAfterUpdate = "SELECT * FROM SalesOrder WHERE SalesOrderID= " + salesOrderId;
        resultSet = statement.executeQuery(queryAfterUpdate);
        List<TestSalesOrderService> actualProductList = new ArrayList<TestSalesOrderService>();
        while (resultSet.next()) {
            TestSalesOrderService salesOrderBean = new TestSalesOrderService();
            salesOrderBean.setOrderQuantity(resultSet.getLong("orderQty"));
            salesOrderBean.setProductID(resultSet.getString("productId"));
            salesOrderBean.setSalesOrderID(resultSet.getString("salesOrderId"));
            actualProductList.add(salesOrderBean);
        }
        for (int i = 0; i < expectedProductList.size(); i++) {
            TestSalesOrderService s1 = expectedProductList.get(i);
            TestSalesOrderService s2 = actualProductList.get(i);
            assertSame(s1.getOrderQuantity(), s2.getOrderQuantity());
            assertTrue(s1.getProductID().equalsIgnoreCase(s2.getProductID()));
        }
    }
