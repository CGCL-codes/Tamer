    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();
        try {
            Map<String, String> xsltParams = getXsltParams(request);
            String specifiedXQuery = xsltParams.get("xquery");
            String xqueryPath = getServletContext().getRealPath(specifiedXQuery);
            File xqueryFile = new File(xqueryPath);
            FileInputStream xqueryStream = new FileInputStream(xqueryFile);
            String sourceUrl = xsltParams.get(XsltTransform.ThisDataSourceURL);
            if (sourceUrl == null) sourceUrl = getDataSourceURL(request);
            sourceUrl += "?";
            for (String xsltParamName : xsltParams.keySet()) {
                sourceUrl += xsltParamName + "=" + xsltParams.get(xsltParamName) + "&";
            }
            sourceUrl = sourceUrl.substring(0, sourceUrl.length() - 1);
            URL url = new URL(sourceUrl);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            byte[] readData = new byte[1024];
            int i = xqueryStream.read(readData);
            while (i != -1) {
                wr.write(new String(readData), 0, i);
                i = xqueryStream.read(readData);
            }
            xqueryStream.close();
            wr.flush();
            Source s = new StreamSource(conn.getInputStream());
            String filePath = getServletContext().getRealPath(getXsltScript(request));
            XsltTransform.doTransform(filePath, s, xsltParams, writer);
        } catch (Exception ex) {
            throw new ServletException(ex);
        }
    }
