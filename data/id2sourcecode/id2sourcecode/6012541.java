    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletOutputStream out = response.getOutputStream();
        if (!_isactive) {
            response.setContentType(HTML_TYPE);
            out.write(getDisableMessage().getBytes());
            return;
        }
        int cmd = 0;
        InputStream in = request.getInputStream();
        StringWriter sw = new StringWriter();
        int x;
        while ((x = in.read()) != '\n') sw.write(x);
        sw.flush();
        cmd = Integer.parseInt(sw.toString());
        System.out.println("SR service accepted store command |" + cmd + "|");
        try {
            switch(cmd) {
                case SRinfo.Publish_Service_Manifest:
                    System.out.println("-- Publish Service Manifest --");
                    String sslModelID = null;
                    String sdlModelID = null;
                    String sslDataID = null;
                    _smMan.processSM(in);
                    InputStream insm = null;
                    try {
                        insm = _smMan.getBMLmodel();
                        sslModelID = _modelMan.handleSSL().storeSSLModel(insm);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    ;
                    try {
                        insm = _smMan.getSDLmodel();
                        sdlModelID = _modelMan.handleSDL().storeSDLModel(insm);
                    } catch (Exception ex) {
                    }
                    ;
                    try {
                        insm = _smMan.getBMLdata();
                        sslDataID = _modelMan.handleSSL().storeSSLModel(insm);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    ;
                    String smid = _smMan.storeSM(sslModelID, sdlModelID, null, sslDataID);
                    out = response.getOutputStream();
                    out.println("<SMID>" + smid + "</SMID>");
                    break;
                case SRinfo.SR_Submit_Query_Model:
                    System.out.println("-- Submit Query --");
                    _modelMan.handleOQL().storeOQLModel(in);
                    String modelName = _modelMan.handleOQL().getQueryContext();
                    try {
                        _queryEngine.evaluateQueryRDB();
                    } catch (Exception ex1) {
                        ex1.printStackTrace();
                    }
                    out = response.getOutputStream();
                    _smMan.formulateQueryResults(modelName, out);
                    _modelMan.handleOQL().clearRepository();
                    break;
                default:
                    in.close();
                    throw new Exception("SR: Command not Found");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ServletException("SR: Constraint Violation Error");
        }
        System.out.println("SR service completed store command |" + cmd + "|");
    }
