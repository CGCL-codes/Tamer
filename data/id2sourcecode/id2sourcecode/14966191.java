    public static boolean detectGroupMembers(Context context) {
        Log.i(LOG_CATEGORY, "Detecting group members with current controller server url " + AppSettingsModel.getCurrentServer(context));
        HttpParams params = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params, 5 * 1000);
        HttpConnectionParams.setSoTimeout(params, 5 * 1000);
        HttpClient httpClient = new DefaultHttpClient(params);
        String url = AppSettingsModel.getSecuredServer(context);
        HttpGet httpGet = new HttpGet(url + "/rest/servers");
        if (httpGet == null) {
            Log.e(LOG_CATEGORY, "Create HttpRequest fail.");
            return false;
        }
        SecurityUtil.addCredentialToHttpRequest(context, httpGet);
        try {
            URL uri = new URL(url);
            if ("https".equals(uri.getProtocol())) {
                Scheme sch = new Scheme(uri.getProtocol(), new SelfCertificateSSLSocketFactory(), uri.getPort());
                httpClient.getConnectionManager().getSchemeRegistry().register(sch);
            }
            HttpResponse httpResponse = httpClient.execute(httpGet);
            try {
                if (httpResponse.getStatusLine().getStatusCode() == Constants.HTTP_SUCCESS) {
                    InputStream data = httpResponse.getEntity().getContent();
                    try {
                        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                        DocumentBuilder builder = factory.newDocumentBuilder();
                        Document dom = builder.parse(data);
                        Element root = dom.getDocumentElement();
                        NodeList nodeList = root.getElementsByTagName("server");
                        int nodeNums = nodeList.getLength();
                        List<String> groupMembers = new ArrayList<String>();
                        for (int i = 0; i < nodeNums; i++) {
                            groupMembers.add(nodeList.item(i).getAttributes().getNamedItem("url").getNodeValue());
                        }
                        Log.i(LOG_CATEGORY, "Detected groupmembers. Groupmembers are " + groupMembers);
                        return saveGroupMembersToFile(context, groupMembers);
                    } catch (IOException e) {
                        Log.e(LOG_CATEGORY, "The data is from ORConnection is bad", e);
                    } catch (ParserConfigurationException e) {
                        Log.e(LOG_CATEGORY, "Cant build new Document builder", e);
                    } catch (SAXException e) {
                        Log.e(LOG_CATEGORY, "Parse data error", e);
                    }
                } else {
                    Log.e(LOG_CATEGORY, "detectGroupMembers Parse data error");
                }
            } catch (IllegalStateException e) {
                Log.e(LOG_CATEGORY, "detectGroupMembers Parse data error", e);
            } catch (IOException e) {
                Log.e(LOG_CATEGORY, "detectGroupMembers Parse data error", e);
            }
        } catch (MalformedURLException e) {
            Log.e(LOG_CATEGORY, "Create URL fail:" + url);
        } catch (ConnectException e) {
            Log.e(LOG_CATEGORY, "Connection refused: " + AppSettingsModel.getCurrentServer(context), e);
        } catch (ClientProtocolException e) {
            Log.e(LOG_CATEGORY, "Can't Detect groupmembers with current controller server " + AppSettingsModel.getCurrentServer(context), e);
        } catch (SocketTimeoutException e) {
            Log.e(LOG_CATEGORY, "Can't Detect groupmembers with current controller server " + AppSettingsModel.getCurrentServer(context), e);
        } catch (IOException e) {
            Log.e(LOG_CATEGORY, "Can't Detect groupmembers with current controller server " + AppSettingsModel.getCurrentServer(context), e);
        } catch (IllegalArgumentException e) {
            Log.e(LOG_CATEGORY, "Host name can be null :" + AppSettingsModel.getCurrentServer(context), e);
        }
        return false;
    }
