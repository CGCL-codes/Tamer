    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String ProductId = request.getParameter("product");
        String urlForAddSales = "http://" + ConfigUtil.getServiceHost() + "/" + ConfigUtil.getServiceName() + "/SalesOrder/SalesOrderData";
        StringBuilder postData = new StringBuilder();
        postData.append("productId=" + URLEncoder.encode(ProductId, AppFabricEnvironment.ENCODING));
        postData.append("&");
        postData.append("orderQty=" + URLEncoder.encode(request.getParameter("txtProductQty"), AppFabricEnvironment.ENCODING));
        postData.append("&");
        postData.append("salesPersonId=" + URLEncoder.encode((String) request.getSession().getAttribute("salesPersonId"), AppFabricEnvironment.ENCODING));
        if (ConfigUtil.isMessageBuffer) {
            HashMap<String, String> headers = new HashMap<String, String>();
            headers.put("token", request.getSession().getAttribute("acsToken").toString());
            headers.put("Content-Type", "application/x-www-form-urlencoded");
            headers.put("Content-Language", "en-US");
            headers.put("Accept", "*/*");
            MessageBufferPolicy messageBufferPolicy = new MessageBufferPolicy("Required", "None", "PT5M", 10);
            Credentials credentials;
            try {
                credentials = new Credentials(TOKEN_TYPE.SharedSecretToken, issuerName, issuerKey);
                ServiceBusRequest msgBufferClient = new ServiceBusRequest(messageBufferName, messageBufferPolicy, PROXY_SERVER, Integer.parseInt(PROXY_PORT), solutionName, credentials);
                String xmlData = msgBufferClient.xmlForPollingService(urlForAddSales, HttpVerbs.PUT, request.getSession().getId(), headers, "", true);
                if (LoggerUtil.getIsLoggingOn()) LoggerHelper.logMessage(URLEncoder.encode(xmlData, "UTF-8"), LoggerHelper.RecordType.AddEditSalesData_REQUEST);
                String responseData = msgBufferClient.sendPostOrPutRequest(urlForAddSales, HttpVerbs.PUT, request.getSession().getId(), headers, postData.toString());
                if (LoggerUtil.getIsLoggingOn()) LoggerHelper.logMessage(URLEncoder.encode(responseData, "UTF-8"), LoggerHelper.RecordType.AddEditSalesData_RESPONSE);
            } catch (AppFabricException e) {
                response.sendRedirect("/" + ConfigUtil.getClientName() + "/pages/error.jsp?message=" + e.getMessage());
                return;
            }
        } else {
            DataOutputStream outputStream = null;
            InputStream inputStream = null;
            BufferedReader bufferedReader = null;
            DataInputStream input = null;
            StringBuffer sBuf = new StringBuffer();
            URL url = null;
            url = new URL(urlForAddSales);
            try {
                HttpURLConnection httpUrlConnection;
                httpUrlConnection = (HttpURLConnection) url.openConnection();
                httpUrlConnection.setRequestMethod("PUT");
                httpUrlConnection.setUseCaches(false);
                httpUrlConnection.setDoInput(true);
                httpUrlConnection.setDoOutput(true);
                httpUrlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                httpUrlConnection.setRequestProperty("Content-Language", "en-US");
                httpUrlConnection.setRequestProperty("Accept", "*/*");
                httpUrlConnection.setRequestProperty("Authorization", (String) request.getSession().getAttribute("acsToken"));
                httpUrlConnection.addRequestProperty("token", (String) request.getSession().getAttribute("acsToken"));
                outputStream = new DataOutputStream(httpUrlConnection.getOutputStream());
                outputStream.writeBytes(postData.toString());
                outputStream.flush();
                inputStream = httpUrlConnection.getInputStream();
                if (httpUrlConnection.getResponseCode() == HttpServletResponse.SC_UNAUTHORIZED) {
                    response.sendRedirect("/" + ConfigUtil.getClientName() + "/pages/error.jsp?code=" + httpUrlConnection.getResponseCode() + "&message=" + httpUrlConnection.getResponseMessage() + "&method=Add Order");
                    return;
                }
                input = new DataInputStream(inputStream);
                bufferedReader = new BufferedReader(new InputStreamReader(input));
                String str;
                while (null != ((str = bufferedReader.readLine()))) {
                    sBuf.append(str);
                }
            } catch (MalformedURLException e) {
                response.sendRedirect("/" + ConfigUtil.getClientName() + "/pages/error.jsp?message=Malformed URL exception");
            } catch (IOException e) {
                response.sendRedirect("/" + ConfigUtil.getClientName() + "/pages/error.jsp?message=IOException in Add sales order");
            }
        }
        response.sendRedirect("/" + ConfigUtil.getClientName() + "/pages/personalSalesOrder.jsp");
    }
