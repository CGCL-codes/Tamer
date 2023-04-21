    public static void fileView(ResourceRequest request, ResourceResponse response) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        String wf = request.getParameter("workflowID");
        WorkflowData t = PortalCacheService.getInstance().getUser(request.getRemoteUser()).getWorkflow(wf);
        Hashtable<String, String> params = new Hashtable<String, String>();
        params.put("portalID", PropertyLoader.getInstance().getProperty("service.url"));
        params.put("userID", request.getRemoteUser());
        params.put("workflowID", wf);
        params.put("wfsID", t.getWfsID());
        Enumeration<String> enm = request.getParameterNames();
        String key;
        while (enm.hasMoreElements()) {
            key = enm.nextElement();
            if (params.get(key) == null) params.put(key, request.getParameter(key));
        }
        Hashtable hsh = new Hashtable();
        hsh.put("url", t.getStorageID());
        ServiceType st = InformationBase.getI().getService("storage", "portal", hsh, new Vector());
        PortalStorageClient ps = (PortalStorageClient) Class.forName(st.getClientObject()).newInstance();
        ps.setServiceURL(st.getServiceUrl());
        ps.setServiceID("/viewer");
        InputStream is = ps.getStream(params);
        byte[] b = new byte[1024];
        int nm;
        response.setContentType("text/html");
        response.getPortletOutputStream().write("<textarea readonly=\"true\" cols=\"100\" rows=\"8\">\n".getBytes());
        while ((nm = is.read(b)) > (-1)) response.getPortletOutputStream().write(b, 0, nm);
        response.getPortletOutputStream().write("</textarea>\n".getBytes());
        is.close();
    }
