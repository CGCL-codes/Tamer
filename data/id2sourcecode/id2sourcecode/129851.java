    @org.junit.Test
    public void checkCreateSalesOrder() throws Exception {
        long salesPersonId = 11;
        long productId = 12;
        long orderQty = 15;
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
            httpUrlConnection.setRequestMethod("PUT");
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
            postData.append("salesPersonId=" + URLEncoder.encode(Long.toString(salesPersonId), "UTF-8"));
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
        String queryAfterInsert = "SELECT * FROM SalesOrder WHERE ProductID= " + productId + " AND OrderQty= " + orderQty + " AND SalesPersonID= " + salesPersonId;
        resultSet = statement.executeQuery(queryAfterInsert);
        List<TestSalesOrderService> expectedProductList = new ArrayList<TestSalesOrderService>();
        while (resultSet.next()) {
            TestSalesOrderService salesOrderBean = new TestSalesOrderService();
            salesOrderBean.setOrderDate(resultSet.getString("orderDate"));
            salesOrderBean.setOrderQuantity(resultSet.getLong("orderQty"));
            salesOrderBean.setRegionName(resultSet.getString("SalesPersonID"));
            salesOrderBean.setProductID(resultSet.getString("productId"));
            salesOrderBean.setSalesOrderID(resultSet.getString("salesOrderId"));
            expectedProductList.add(salesOrderBean);
        }
        if (expectedProductList.size() != 0) {
            assertNotNull("Insertion fails", expectedProductList);
        } else {
            throw new AssertionError("No Record found in the database.");
        }
    }
