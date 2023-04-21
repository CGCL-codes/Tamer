    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String xml = "";
            java.io.InputStream ios = request.getInputStream();
            String xmlStr = newGenXMLGenerator.getXMLDocument(ios);
            org.jdom.Document doc = newGenXMLGenerator.getDocFromXMLDocument(xmlStr);
            String operationid = doc.getRootElement().getAttributeValue("no");
            System.out.println("calling handler class");
            javax.servlet.http.HttpSession session = request.getSession(true);
            if (operationid.equals("1")) {
                xml = (new DisplayTitleServletHandler(home)).getTopicalTermDetails(doc, session);
                java.io.OutputStream os = response.getOutputStream();
                newGenXMLGenerator.compressXMLDocument(os, xml);
            } else if (operationid.equals("2")) {
                xml = (new DisplayTitleServletHandler(home)).getCallNoDetails(doc, session);
                java.io.OutputStream os = response.getOutputStream();
                newGenXMLGenerator.compressXMLDocument(os, xml);
            } else if (operationid.equals("3")) {
                xml = (new DisplayTitleServletHandler(home)).getPersonamNameDetails(doc, session);
                java.io.OutputStream os = response.getOutputStream();
                newGenXMLGenerator.compressXMLDocument(os, xml);
            } else if (operationid.equals("4")) {
                xml = (new DisplayTitleServletHandler(home)).getCorporateDetails(doc, session);
                java.io.OutputStream os = response.getOutputStream();
                newGenXMLGenerator.compressXMLDocument(os, xml);
            } else if (operationid.equals("5")) {
                xml = (new DisplayTitleServletHandler(home)).getMeetingDetails(doc, session);
                java.io.OutputStream os = response.getOutputStream();
                newGenXMLGenerator.compressXMLDocument(os, xml);
            } else if (operationid.equals("6")) {
                xml = (new DisplayTitleServletHandler(home)).getGeographicDetails(doc, session);
                java.io.OutputStream os = response.getOutputStream();
                newGenXMLGenerator.compressXMLDocument(os, xml);
            } else if (operationid.equals("7")) {
                xml = (new DisplayTitleServletHandler(home)).getUnifromDetails(doc, session);
                java.io.OutputStream os = response.getOutputStream();
                newGenXMLGenerator.compressXMLDocument(os, xml);
            } else if (operationid.equals("8")) {
                xml = (new DisplayTitleServletHandler(home)).getAuthorPersonalDetails(doc, session);
                java.io.OutputStream os = response.getOutputStream();
                newGenXMLGenerator.compressXMLDocument(os, xml);
            } else if (operationid.equals("9")) {
                xml = (new DisplayTitleServletHandler(home)).getAuthorCorporateDetails(doc, session);
                java.io.OutputStream os = response.getOutputStream();
                newGenXMLGenerator.compressXMLDocument(os, xml);
            } else if (operationid.equals("10")) {
                xml = (new DisplayTitleServletHandler(home)).getAuthorMeetingDetails(doc, session);
                java.io.OutputStream os = response.getOutputStream();
                newGenXMLGenerator.compressXMLDocument(os, xml);
            } else if (operationid.equals("11")) {
                xml = (new DisplayTitleServletHandler(home)).getSeriesDetails(doc, session);
                java.io.OutputStream os = response.getOutputStream();
                newGenXMLGenerator.compressXMLDocument(os, xml);
            } else if (operationid.equals("12")) {
                xml = (new DisplayTitleServletHandler(home)).getUniformTitleDetails(doc, session);
                java.io.OutputStream os = response.getOutputStream();
                newGenXMLGenerator.compressXMLDocument(os, xml);
            } else if (operationid.equals("13")) {
                xml = (new DisplayTitleServletHandler(home)).getFullCallNoDetails(doc);
                java.io.OutputStream os = response.getOutputStream();
                newGenXMLGenerator.compressXMLDocument(os, xml);
            } else if (operationid.equals("14")) {
                xml = (new DisplayTitleServletHandler(home)).getFullSeriesDetails(doc);
                java.io.OutputStream os = response.getOutputStream();
                newGenXMLGenerator.compressXMLDocument(os, xml);
            } else if (operationid.equals("15")) {
                xml = (new DisplayTitleServletHandler(home)).getFullUniformTitleDetails(doc);
                java.io.OutputStream os = response.getOutputStream();
                newGenXMLGenerator.compressXMLDocument(os, xml);
            } else if (operationid.equals("16")) {
                xml = (new DisplayTitleServletHandler(home)).getFullAuthorPersonalDetails(doc);
                java.io.OutputStream os = response.getOutputStream();
                newGenXMLGenerator.compressXMLDocument(os, xml);
            } else if (operationid.equals("17")) {
                xml = (new DisplayTitleServletHandler(home)).getFullAuthorCorporateDetails(doc);
                java.io.OutputStream os = response.getOutputStream();
                newGenXMLGenerator.compressXMLDocument(os, xml);
            } else if (operationid.equals("18")) {
                xml = (new DisplayTitleServletHandler(home)).getFullAuthorMeetingDetails(doc);
                java.io.OutputStream os = response.getOutputStream();
                newGenXMLGenerator.compressXMLDocument(os, xml);
            } else if (operationid.equals("19")) {
                xml = (new DisplayTitleServletHandler(home)).getFullTopicalTermDetails(doc);
                java.io.OutputStream os = response.getOutputStream();
                newGenXMLGenerator.compressXMLDocument(os, xml);
            } else if (operationid.equals("20")) {
                xml = (new DisplayTitleServletHandler(home)).getFullPersonalNameDetails(doc);
                java.io.OutputStream os = response.getOutputStream();
                newGenXMLGenerator.compressXMLDocument(os, xml);
            } else if (operationid.equals("21")) {
                xml = (new DisplayTitleServletHandler(home)).getFullCorporateNameDetails(doc);
                java.io.OutputStream os = response.getOutputStream();
                newGenXMLGenerator.compressXMLDocument(os, xml);
            } else if (operationid.equals("22")) {
                xml = (new DisplayTitleServletHandler(home)).getFullMeetingNameDetails(doc);
                java.io.OutputStream os = response.getOutputStream();
                newGenXMLGenerator.compressXMLDocument(os, xml);
            } else if (operationid.equals("23")) {
                xml = (new DisplayTitleServletHandler(home)).getFullGeographicNameDetails(doc);
                java.io.OutputStream os = response.getOutputStream();
                newGenXMLGenerator.compressXMLDocument(os, xml);
            } else if (operationid.equals("24")) {
                xml = (new DisplayTitleServletHandler(home)).getFullUniformNameDetails(doc);
                java.io.OutputStream os = response.getOutputStream();
                newGenXMLGenerator.compressXMLDocument(os, xml);
            } else if (operationid.equals("26")) {
                xml = (new DisplayTitleServletHandler(home)).getClassificationNoDetails(doc, session);
                java.io.OutputStream os = response.getOutputStream();
                newGenXMLGenerator.compressXMLDocument(os, xml);
            } else if (operationid.equals("27")) {
                xml = (new DisplayTitleServletHandler(home)).getFullClassificationNosDetails(doc);
                java.io.OutputStream os = response.getOutputStream();
                newGenXMLGenerator.compressXMLDocument(os, xml);
            } else if (operationid.equals("25")) {
                String file = doc.getRootElement().getChildText("FileName");
                System.out.println("file===============" + file);
                java.nio.channels.FileChannel fc = (new java.io.FileInputStream(file)).getChannel();
                int fileLength = (int) fc.size();
                System.out.println("lenght is =======" + fileLength);
                java.nio.MappedByteBuffer bb = fc.map(java.nio.channels.FileChannel.MapMode.READ_ONLY, 0, fileLength);
                System.out.println("this is bb---------" + bb.capacity());
                java.util.StringTokenizer stk = new java.util.StringTokenizer(file, ".");
                stk.nextToken();
                String ext = stk.nextToken();
                System.out.println("extension of file is==========" + ext);
                String contenttype = "";
                try {
                    contenttype = java.util.ResourceBundle.getBundle("server").getString(ext.trim().toUpperCase());
                } catch (Exception e) {
                    java.util.Properties p1 = new java.util.Properties();
                    java.io.File propfile = new java.io.File(ejb.bprocess.util.NewGenLibRoot.getRoot() + "/SystemFiles/ContentTypes.properties");
                    System.out.println("----" + propfile.getPath());
                    java.io.FileInputStream f1 = new java.io.FileInputStream(ejb.bprocess.util.NewGenLibRoot.getRoot() + java.util.ResourceBundle.getBundle("server").getString("SystemFilesPath") + "/ContentTypes.properties");
                    p1.load(f1);
                    contenttype = p1.getProperty(ext.trim().toUpperCase(), "");
                    System.out.println("content type in view attachmants page=" + contenttype);
                }
                System.out.println("content type=" + contenttype);
                response.setContentType(contenttype);
                java.nio.channels.WritableByteChannel wbc = java.nio.channels.Channels.newChannel(response.getOutputStream());
                wbc.write(bb);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
