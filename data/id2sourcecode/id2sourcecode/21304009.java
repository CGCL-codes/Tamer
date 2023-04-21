    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String salesOrderId = request.getParameter("txtSalesOrderId");
        String productId = request.getParameter("product");
        String orderQty = request.getParameter("txtProductQty");
        String urlForAddSales = "http://" + ConfigUtil.getServiceHost() + "/" + ConfigUtil.getServiceName() + "/SalesOrder/SalesOrderData";
        String responseString = null;
        DataOutputStream outputStream = null;
        InputStream inputStream = null;
        BufferedReader bufferedReader = null;
        DataInputStream input = null;
        StringBuffer sBuf = new StringBuffer();
        URL url = null;
        StringBuilder postData = new StringBuilder();
        postData.append("productId=" + URLEncoder.encode(productId, AppFabricEnvironment.ENCODING));
        postData.append("&");
        postData.append("orderQty=" + URLEncoder.encode(orderQty, AppFabricEnvironment.ENCODING));
        postData.append("&");
        postData.append("salesOrderId=" + URLEncoder.encode(salesOrderId, AppFabricEnvironment.ENCODING));
        if (ConfigUtil.isMessageBuffer) {
            HashMap<String, String> headers = new HashMap<String, String>();
            headers.put("token", request.getSession().getAttribute("acsToken").toString());
            MessageBufferPolicy messageBufferPolicy = new MessageBufferPolicy("Required", "None", "PT5M", 10);
            Credentials credentials;
            try {
                credentials = new Credentials(TOKEN_TYPE.SharedSecretToken, issuerName, issuerKey);
                ServiceBusRequest msgBufferClient = new ServiceBusRequest(messageBufferName, messageBufferPolicy, PROXY_SERVER, Integer.parseInt(PROXY_PORT), solutionName, credentials);
                String xmlData = msgBufferClient.xmlForPollingService(urlForAddSales, HttpVerbs.POST, request.getSession().getId(), headers, "", true);
                if (LoggerUtil.getIsLoggingOn()) LoggerHelper.logMessage(URLEncoder.encode(xmlData, "UTF-8"), LoggerHelper.RecordType.UpdateSalesOrder_REQUEST);
                String responseData = msgBufferClient.sendPostOrPutRequest(urlForAddSales, HttpVerbs.POST, request.getSession().getId(), headers, postData.toString());
                if (LoggerUtil.getIsLoggingOn()) LoggerHelper.logMessage(URLEncoder.encode(responseData, "UTF-8"), LoggerHelper.RecordType.UpdateSalesOrder_RESPONSE);
            } catch (AppFabricException e) {
                response.sendRedirect("/" + ConfigUtil.getClientName() + "/pages/error.jsp?message=" + e.getMessage());
                return;
            }
        } else {
            try {
                url = new URL(urlForAddSales);
                HttpURLConnection httpUrlConnection;
                httpUrlConnection = (HttpURLConnection) url.openConnection();
                httpUrlConnection.setRequestMethod("POST");
                httpUrlConnection.setUseCaches(false);
                httpUrlConnection.setDoInput(true);
                httpUrlConnection.setDoOutput(true);
                httpUrlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                httpUrlConnection.setRequestProperty("Content-Language", "en-US");
                httpUrlConnection.setRequestProperty("Accept", "*/*");
                httpUrlConnection.addRequestProperty("token", (String) request.getSession().getAttribute("acsToken"));
                httpUrlConnection.addRequestProperty("solutionName", (String) request.getSession().getAttribute("solutionName"));
                outputStream = new DataOutputStream(httpUrlConnection.getOutputStream());
                outputStream.writeBytes(postData.toString());
                outputStream.flush();
                if (httpUrlConnection.getResponseCode() == HttpServletResponse.SC_UNAUTHORIZED) {
                    response.sendRedirect("/" + ConfigUtil.getClientName() + "/pages/error.jsp?code=" + httpUrlConnection.getResponseCode() + "&message=" + httpUrlConnection.getResponseMessage() + "&method=Update Order");
                    return;
                }
                inputStream = httpUrlConnection.getInputStream();
                input = new DataInputStream(inputStream);
                bufferedReader = new BufferedReader(new InputStreamReader(input));
                String str;
                while (null != ((str = bufferedReader.readLine()))) {
                    sBuf.append(str);
                }
                responseString = sBuf.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        response.sendRedirect("/" + ConfigUtil.getClientName() + "/pages/personalSalesOrder.jsp");
    }
