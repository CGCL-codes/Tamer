    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String queryString = req.getQueryString();
        if (queryString == null) queryString = "";
        Enumeration paramNames = req.getParameterNames();
        Object paramName;
        while (paramNames.hasMoreElements()) {
            paramName = paramNames.nextElement();
            if (!queryString.isEmpty()) queryString += "&";
            queryString += paramName + "=" + URLEncoder.encode(req.getParameter(paramName.toString()));
        }
        Serializable ps = settingsManager.getPluginSettings("com.zagile.confluence.plugins.zsemantic_plugin");
        ZSemanticPluginSettings settings = (ZSemanticPluginSettings) ps;
        zRpcServBaseUrl = settings.getZOntoApiBaseUrl();
        logger.info("REQUESTING: " + zRpcServBaseUrl + "Search?" + queryString);
        URLConnection urlConnection;
        try {
            urlConnection = new URL(zRpcServBaseUrl + "Search?" + queryString).openConnection();
            urlConnection.setRequestProperty("Content-Type", "text/html;charset=UTF-8");
            urlConnection.connect();
            InputStream is = urlConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String strLine;
            int b = br.read();
            while (b != -1) {
                resp.getOutputStream().write(b);
                b = br.read();
            }
            is.close();
            resp.getOutputStream().close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
        }
    }
