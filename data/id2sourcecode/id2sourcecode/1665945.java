    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        java.io.InputStream is = request.getInputStream();
        String xmlStr = newGenXMLGenerator.getXMLDocument(is);
        org.jdom.Element root = newGenXMLGenerator.getRootElementFromXMLDocument(xmlStr);
        ejb.bprocess.administration.PrintComponentSession printSession = null;
        try {
            printSession = ((ejb.bprocess.administration.PrintComponentSessionHome) homeFactory.getRemoteHome("PrintComponentSession")).create();
            if (printSession != null) {
                if (root.getAttributeValue("no").equals("1")) {
                    System.out.println("This is Servlet No 1:");
                    java.util.Vector vec = new java.util.Vector();
                    xmlStr = printSession.getFormLetterDetails(xmlStr);
                    java.io.OutputStream os = response.getOutputStream();
                    newGenXMLGenerator.compressXMLDocument(os, xmlStr);
                } else if (root.getAttributeValue("no").equals("2")) {
                    System.out.println("This is Servlet No 2:");
                    xmlStr = printSession.getDetails(xmlStr);
                    java.io.OutputStream os = response.getOutputStream();
                    newGenXMLGenerator.compressXMLDocument(os, xmlStr);
                } else if (root.getAttributeValue("no").equals("3")) {
                    System.out.println("This is Servlet No 3:");
                    xmlStr = printSession.updateEndOfDayProcess(xmlStr);
                    java.io.OutputStream os = response.getOutputStream();
                    newGenXMLGenerator.compressXMLDocument(os, xmlStr);
                } else if (root.getAttributeValue("no").equals("4")) {
                    System.out.println("This is Servlet No 4:");
                    xmlStr = printSession.updatePrintStatus(xmlStr);
                    java.io.OutputStream os = response.getOutputStream();
                    newGenXMLGenerator.compressXMLDocument(os, xmlStr);
                } else if (root.getAttributeValue("no").equals("5")) {
                    System.out.println("This is Servlet No 5:");
                    response.setContentType("application/vnd.oasis.opendocument.text");
                    String filename = root.getChildText("Path");
                    java.io.File ff = new java.io.File(filename);
                    java.nio.channels.FileChannel fc = (new java.io.FileInputStream(ff)).getChannel();
                    int fileLength = (int) fc.size();
                    java.nio.MappedByteBuffer bb = fc.map(java.nio.channels.FileChannel.MapMode.READ_ONLY, 0, fileLength);
                    byte[] byx = new byte[bb.capacity()];
                    fc.close();
                    bb.get(byx);
                    response.getOutputStream().write(byx);
                } else if (root.getAttributeValue("no").equals("6")) {
                    System.out.println("This is Servlet No 6:");
                    xmlStr = printSession.getPrintFirmOrderDetails(xmlStr);
                    java.io.OutputStream os = response.getOutputStream();
                    newGenXMLGenerator.compressXMLDocument(os, xmlStr);
                }
            }
        } catch (java.lang.Exception ex) {
            ex.printStackTrace();
        }
    }
