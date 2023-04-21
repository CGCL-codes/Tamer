    public static void fileDownload(String pDwlType, ResourceRequest request, ResourceResponse response) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        String wf = request.getParameter("workflowID");
        WorkflowData t = null;
        if ("graf".equals(pDwlType)) {
            t = PortalCacheService.getInstance().getUser(request.getRemoteUser()).getAbstactWorkflow(wf);
            if (t.getWfsID() == null) {
                ServiceType st = InformationBase.getI().getService("wfs", "portal", new Hashtable(), new Vector());
                t.setWfsID(st.getServiceUrl());
                System.out.println("WFS:" + t.getWorkflowID() + ":" + st.getServiceUrl());
            }
            if (t.getStorageID() == null) {
                ServiceType st = InformationBase.getI().getService("storage", "portal", new Hashtable(), new Vector());
                t.setStorageID(st.getServiceUrl());
                System.out.println("STORAGE:" + t.getWorkflowID() + ":" + st.getServiceUrl());
            }
        } else if ("abst".equals(pDwlType)) t = PortalCacheService.getInstance().getUser(request.getRemoteUser()).getTemplateWorkflow(wf); else t = PortalCacheService.getInstance().getUser(request.getRemoteUser()).getWorkflow(wf);
        Hashtable<String, String> params = new Hashtable<String, String>();
        params.put("portalID", PropertyLoader.getInstance().getProperty("service.url"));
        params.put("userID", request.getRemoteUser());
        params.put("downloadType", pDwlType);
        params.put("workflowID", wf);
        params.put("wfsID", t.getWfsID());
        Enumeration<String> enm = request.getParameterNames();
        String key;
        while (enm.hasMoreElements()) {
            key = enm.nextElement();
            if (params.get(key) == null) params.put(key, request.getParameter(key));
        }
        Hashtable hsh = new Hashtable();
        try {
            hsh.put("url", t.getStorageID());
        } catch (Exception e) {
        }
        ServiceType st = InformationBase.getI().getService("storage", "portal", hsh, new Vector());
        PortalStorageClient ps = (PortalStorageClient) Class.forName(st.getClientObject()).newInstance();
        ps.setServiceURL(st.getServiceUrl());
        ps.setServiceID("/download");
        InputStream is = ps.getStream(params);
        byte[] b = new byte[1024];
        int nm;
        while ((nm = is.read(b)) > (-1)) response.getPortletOutputStream().write(b, 0, nm);
        is.close();
    }
