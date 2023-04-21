    protected void doSearch(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String communityId = getCurrentCommunityId(request.getSession());
        if ((communityId == null) || communityId.length() == 0) {
            LOG.warn("XQuerySearchServlet " + request.getMethod() + " Request is missing the id of the" + " community.");
            writeError(request, response, "<p><b>XQuerySearchServlet:</b> Request is missing" + " the ID of the community.</p>", MODE);
            return;
        }
        String filename = request.getParameter(HttpParams.UP2P_FILENAME);
        String xQueryStr = request.getParameter(HttpParams.UP2P_XQUERY_SEARCH);
        if (filename == null && xQueryStr == null) {
            LOG.warn("XQuerySearchServlet " + request.getMethod() + " request is missing both a file name or XQuery.");
            writeError(request, response, "<p><b>XQuerySearchServlet:</b> Request is missing" + " either a file name of an XQuery file or an" + " XQuery.</p>", MODE);
            return;
        }
        if (filename != null) {
            File xQueryFile = new File(filename);
            if (!xQueryFile.isAbsolute()) {
                LOG.debug("XQuerySearchServlet Converting relative filename " + xQueryFile.getPath());
                xQueryFile = new File(System.getProperty(WebAdapter.UP2P_HOME) + File.separator + xQueryFile.getPath());
                LOG.debug("XQuerySearchServlet Converted filename to" + " absolute file " + xQueryFile.getAbsolutePath());
            }
            try {
                xQueryStr = FileUtil.readFile(xQueryFile);
            } catch (IOException e) {
                LOG.error("XQuerySearchServlet Error reading XQuery from file " + xQueryFile.getAbsolutePath());
                writeError(request, response, "<p><b>XQuerySearchServlet:</b> Error reading XQuery" + " from file " + xQueryFile.getAbsolutePath() + "</p>", MODE);
                return;
            }
        }
        Map params = new HashMap();
        Enumeration requestParams = request.getParameterNames();
        while (requestParams.hasMoreElements()) {
            String paramName = (String) requestParams.nextElement();
            if (!paramName.startsWith("up2p:")) params.put(paramName, request.getParameter(paramName));
        }
        String result = null;
        try {
            result = adapter.localSearchXQuery(communityId, xQueryStr, params);
        } catch (CommunityNotFoundException e) {
            LOG.warn("XQuerySearchServlet " + request.getMethod() + " Community id of the search (" + communityId + ") is invalid.");
            writeError(request, response, "<p><b>XQuerySearchServlet:</b> Invalid community id: " + communityId + ".</p>", MODE);
            return;
        } catch (XMLDBException e) {
            LOG.warn("XQuerySearchServlet Error in the XQuery search.", e);
            String errorMsg = "<p><b>XQuerySearchServlet:</b> Error in the XQuery.</p><p>";
            if (e.getCause() != null) errorMsg += e.getCause().getMessage(); else errorMsg += e.getMessage();
            errorMsg += "</p><p>XQuery String:<br><pre>" + xQueryStr + "</pre></p>";
            writeError(request, response, errorMsg, MODE);
            return;
        }
        response.setContentType("text/xml");
        response.getWriter().print(result);
    }
