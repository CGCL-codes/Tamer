    public static String[] connectURLSSL(String fullURL, final boolean loadExistingCookies) {
        HttpURLConnection conn = null;
        String[] tuple = new String[3];
        tuple[0] = "";
        tuple[1] = "";
        tuple[2] = "";
        try {
            System.getProperties().put("http.agent", getTestClient().getDefaultUserAgent());
            URL url = getSSLURL(fullURL);
            conn = (HttpsURLConnection) url.openConnection();
            boolean followRedirects = false;
            HttpURLConnection.setFollowRedirects(followRedirects);
            LoadTestCookieManager cookieManager = new LoadTestCookieManager();
            setReadCookieData(cookieManager, conn, url.getHost(), loadExistingCookies);
            cookieManager.queueRefererUrl(fullURL);
            conn.setDoInput(true);
            printHeaderInfo(conn);
            cookieManager.parseCookieData(conn, url.getHost(), loadExistingCookies);
            cookieManager.writeCookieData();
            InputStream inStream = conn.getInputStream();
            System.out.println("DEBUG: inputstream available=" + inStream.available());
            BufferedReader in = new BufferedReader(new InputStreamReader(inStream));
            String str;
            StringBuffer buf = new StringBuffer(500);
            while ((str = in.readLine()) != null) {
                buf.append(str);
            }
            LoadTestWriteHtmlDoc.writeOutput("data/html/" + LoadTestWriteHtmlDoc.generatedHtmlFilename(fullURL) + ".html", buf.toString());
            in.close();
            System.out.println("INFO: SSL content:");
            prettyPrintTrimData(buf.toString(), 40);
            int statusCode = conn.getResponseCode();
            if (!followRedirects) {
                if (statusCode == 302) {
                    String newLocation = conn.getHeaderField("Location");
                    System.out.println("INFO: following redirect to=" + newLocation);
                    tuple = connectURLSSL(newLocation, loadExistingCookies);
                }
            }
        } catch (MalformedURLException me) {
            me.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tuple;
    }
