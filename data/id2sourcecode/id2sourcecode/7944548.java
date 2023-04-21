    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SessionManager sessionManager = SessionManager.getSessionManager(request);
        EntrySet entrySet = sessionManager.getEntrySet();
        if (entrySet.getEntryList().get(0).getSource() == null) new AddSourceToFile().execute(mapping, form, request, response);
        String file = entrySet.toXML();
        if (file.length() > 0) {
            StringBuffer stringBuffer = new StringBuffer(file);
            OutputStream output = response.getOutputStream();
            response.setContentLength(stringBuffer.length());
            response.setContentType("text/xml;charset=utf-8");
            StringBufferInputStream input = new StringBufferInputStream(stringBuffer.toString());
            byte[] buffer = new byte[1024];
            int count = 0;
            while ((count = input.read(buffer)) >= 0) output.write(buffer, 0, count);
            output.close();
        }
        return null;
    }
