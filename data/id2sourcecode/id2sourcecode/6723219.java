    public static String[] connectURL(final String fullURL, final boolean loadExistingCookies) {
        String[] tuple = new String[3];
        tuple[0] = "";
        tuple[1] = "";
        tuple[2] = "";
        HttpURLConnection conn = null;
        try {
            System.getProperties().put("http.agent", getTestClient().getDefaultUserAgent());
            System.getProperties().put("proxySet", "" + getTestClient().isEnableProxy());
            System.getProperties().put("proxyHost", getTestClient().getProxyHost());
            System.getProperties().put("proxyPort", getTestClient().getProxyPort());
            URL url = new URL(fullURL);
            conn = (HttpURLConnection) url.openConnection();
            LoadTestCookieManager cookieManager = new LoadTestCookieManager();
            setReadCookieData(cookieManager, conn, url.getHost(), loadExistingCookies);
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            printHeaderInfo(conn);
            cookieManager.parseCookieData(conn, url.getHost(), loadExistingCookies);
            cookieManager.writeCookieData();
            String str;
            StringBuffer buf = new StringBuffer(500);
            while ((str = in.readLine()) != null) {
                buf.append(str);
            }
            LoadTestWriteHtmlDoc.writeOutput("data/html/" + LoadTestWriteHtmlDoc.generatedHtmlFilename(fullURL) + ".html", buf.toString());
            in.close();
            tuple[0] = "" + conn.getResponseCode();
            tuple[1] = buf.toString();
            if (getTestClient().isDebugEnabled()) {
                LoadTestManager.prettyPrintTrimData("[" + conn.getResponseCode() + "] Data Returned=" + buf.toString(), MAX_HEADER_THRES);
            }
        } catch (MalformedURLException me) {
            me.printStackTrace();
        } catch (IOException e) {
            System.out.println("ERR: IOException error=" + e.getMessage());
            if (conn != null) {
                try {
                    if (conn != null) {
                        String errContent = readInputStream(new BufferedReader(new InputStreamReader(((HttpURLConnection) conn).getErrorStream())));
                        tuple[2] = errContent;
                    }
                    tuple[0] = "" + conn.getResponseCode();
                    tuple[1] = conn.getResponseMessage();
                } catch (IOException ie) {
                }
            }
            e.printStackTrace();
        }
        return tuple;
    }
