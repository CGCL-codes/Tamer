    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("[ValidateServlet] start");
        PrintWriter out = response.getWriter();
        response.setContentType("text/html");
        String xmlStream = new String();
        xmlStream = (request.getParameter("xmldata") == null) ? "" : request.getParameter("xmldata");
        ServletConfig conf = this.getServletConfig();
        ServletContext contxt = conf.getServletContext();
        String basepath = contxt.getInitParameter("rootDir");
        String template = request.getParameter("template");
        String saveas = request.getParameter("saveas");
        String styleurl = request.getParameter("styleurl");
        String exit = request.getParameter("save");
        String apppath = contxt.getInitParameter("applicationDir");
        String sessionValidatorURL = contxt.getInitParameter("sessionValidatorURL");
        String sessionName = contxt.getInitParameter("sessionIdParameterName");
        String sessionIdValue = UserRights.getCustomSessionId(request, sessionName);
        String username = UserRights.getCustomUsername(sessionValidatorURL, sessionName, sessionIdValue);
        Element userNode = UserRights.getUserNode(apppath, username);
        if (UserRights.getUserRights(userNode, template, "read") && UserRights.getUserRights(userNode, template, "write")) {
            System.out.println("Authentication: OK");
        } else {
            System.out.println("Authentication: BAD");
            response.sendRedirect(contxt.getInitParameter("errorUrl"));
        }
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document templateDoc;
        DocumentType templateDocType;
        inputDocument = createInputDocument(xmlStream);
        outputDocument = createOutputDocument();
        evaluateXhtmlNodes(inputDocument, outputDocument);
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            templateDoc = builder.parse(new File(basepath + template));
            templateDocType = templateDoc.getDoctype();
            String publicId = templateDocType.getPublicId();
            String systemId = templateDocType.getSystemId();
            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, systemId);
            if (publicId != null) {
                transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, publicId);
            }
            DOMSource source = new DOMSource(outputDocument);
            StreamResult result = new StreamResult(new File(basepath + saveas));
            transformer.transform(source, result);
            Enumeration parameters = request.getAttributeNames();
            String nameBuffer;
            StringBuffer urlBuffer = new StringBuffer("?");
            for (Enumeration e = request.getParameterNames(); e.hasMoreElements(); ) {
                nameBuffer = (String) e.nextElement();
                if (!nameBuffer.equals("xmldata")) {
                    urlBuffer.append(nameBuffer);
                    urlBuffer.append("=");
                    if (nameBuffer.equals("template") && exit.equals("save")) {
                        urlBuffer.append(saveas);
                    } else {
                        urlBuffer.append(request.getParameter(nameBuffer).toString());
                    }
                    if (e.hasMoreElements()) {
                        urlBuffer.append("&");
                    }
                }
            }
            System.out.println(response.encodeURL(contxt.getInitParameter("exitUrl") + urlBuffer.toString()));
            if (exit.equals("exit")) {
                response.sendRedirect(response.encodeURL(contxt.getInitParameter("exitUrl") + urlBuffer.toString()));
            } else {
                response.sendRedirect(response.encodeURL("editor" + urlBuffer.toString()));
            }
        } catch (Exception e) {
            System.out.println("[ValidateServlet] exception");
            e.printStackTrace();
        }
        System.out.println("[ValidateServlet] stop");
    }
