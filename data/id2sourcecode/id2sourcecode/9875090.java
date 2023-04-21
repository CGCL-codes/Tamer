    private void syncXplanner(String buId, String deptId, String customerId, String projectId, String contractId, String note, String adminIds) throws ClientProtocolException, IOException {
        HttpParams params = new BasicHttpParams();
        ConnManagerParams.setMaxTotalConnections(params, 200);
        ConnPerRouteBean connPerRoute = new ConnPerRouteBean(20);
        HttpHost gsssvn = new HttpHost("gsssvn", 80);
        connPerRoute.setMaxForRoute(new HttpRoute(gsssvn), 50);
        ConnManagerParams.setMaxConnectionsPerRoute(params, connPerRoute);
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager(params, schemeRegistry);
        HttpContext localContext = new BasicHttpContext();
        DefaultHttpClient httpClient = new DefaultHttpClient(cm, params);
        HttpPost httpPost = new HttpPost("http://gsssvn/cgi-bin/test/xp.pl");
        if (loginSyncServer(localContext, httpClient, httpPost)) {
            ArrayList<NameValuePair> pairList = new ArrayList<NameValuePair>();
            pairList.add(new BasicNameValuePair("action", "xplanner"));
            pairList.add(new BasicNameValuePair("bu", buId));
            pairList.add(new BasicNameValuePair("dept", deptId));
            pairList.add(new BasicNameValuePair("customer", customerId));
            pairList.add(new BasicNameValuePair("project", projectId));
            pairList.add(new BasicNameValuePair("contract", contractId));
            pairList.add(new BasicNameValuePair("note", note));
            pairList.add(new BasicNameValuePair("admin", adminIds));
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(pairList, "UTF-8");
            httpPost.setEntity(entity);
            HttpResponse response = httpClient.execute(httpPost);
            String responseString = EntityUtils.toString(response.getEntity());
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                System.out.println(responseString);
                if (responseString.indexOf("失敗") != -1) {
                    System.out.println(false);
                } else {
                    System.out.println(responseString.indexOf("不存在") == -1);
                }
            } else {
                System.out.println(response.getStatusLine());
            }
        }
    }
