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
        System.out.println("KBserver accepted store command |" + cmd + "|");
        try {
            switch(cmd) {
                case KBinfo.KB_Submit_Query_Model:
                    System.out.println("-- Submit Query --");
                    _modelMan.handleOQL().storeOQLModel(in);
                    try {
                        _queryEngine.evaluateQueryRDB();
                    } catch (Exception ex1) {
                        ex1.printStackTrace();
                    }
                    out = response.getOutputStream();
                    String modelname = _modelMan.handleOQL().getQueryContext();
                    if (modelname.equals(KBinfo.KB_SSL_MODEL_NAME)) {
                        _modelMan.handleSSL().formulateQueryResults(out);
                    } else if (modelname.toUpperCase().equals(KBinfo.KB_SDL_MODEL_NAME)) {
                        _modelMan.handleSDL().formulateQueryResults(out);
                    } else if (modelname.equals(KBinfo.KB_ODM_MODEL_NAME)) {
                    }
                    _modelMan.handleOQL().clearRepository();
                    break;
                case KBinfo.KB_Store_SSL_Model:
                    System.out.println("-- Store SSL model --");
                    _modelMan.handleSSL().storeSSLModel(in);
                    out = response.getOutputStream();
                    out.println("<SSL_MODEL_STORED/>");
                    break;
                case KBinfo.KB_Store_SDL_Model:
                    System.out.println("-- Store SDL model --");
                    _modelMan.handleSDL().storeSDLModel(in);
                    out = response.getOutputStream();
                    out.println("<SDL_MODEL_STORED/>");
                    break;
                case KBinfo.KB_Store_ODM_Model:
                    System.out.println("-- Store ODM model --");
                    _modelMan.handleODM().storeODMModel(in);
                    out = response.getOutputStream();
                    out.println("<ODM_MODEL_STORED/>");
                    break;
                case KBinfo.KB_Store_SCM_Model:
                    System.out.println("-- Store SCM model --");
                    _modelMan.handleSCM().storeSCMModel(in);
                    out = response.getOutputStream();
                    out.println("<SCM_MODEL_STORED/>");
                    break;
                case KBinfo.KB_Store_Usage_Data:
                    System.out.println("-- Store Usage Data --");
                    try {
                        _usgMan.storeUsageDataBatch(in);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        throw new ServletException(ex.getMessage());
                    }
                    break;
                default:
                    in.close();
                    throw new Exception("KB: Command not Found");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ServletException("KB: Constraint Violation Error");
        }
        System.out.println("KBserver completed store command |" + cmd + "|");
    }
