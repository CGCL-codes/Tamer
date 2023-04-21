    public String sendRequest(java.lang.String handlerName, java.lang.String request) {
        String xmlResp = "";
        try {
            Element rootreq = org.verus.ngl.utilities.NGLXMLUtility.getInstance().getRootElementFromXML(request);
            rootreq.setAttribute("handler", handlerName);
            request = org.verus.ngl.utilities.NGLXMLUtility.getInstance().generateXML(rootreq);
            String reqxml = "";
            org.jdom.Document retdoc = null;
            String myurl = org.verus.ngl.client.components.NGLUtilities.getInstance().getServerIp();
            String myport = org.verus.ngl.client.components.NGLUtilities.getInstance().getServerPort();
            if (myport == null || myport.trim().equals("")) {
                myport = "8080";
            }
            if (myurl == null || myurl.trim().equals("")) {
                myurl = "localhost";
            }
            System.out.println("http://" + myurl + ":" + myport + "/NGL/NGLMasterServlet");
            java.net.URL url = new java.net.URL("http://" + myurl + ":" + myport + "/NGL/NGLMasterServlet");
            java.net.URLConnection urlconn = (java.net.URLConnection) url.openConnection();
            urlconn.setDoOutput(true);
            urlconn.setRequestProperty("Content-type", "text/xml; charset=UTF-8");
            java.io.OutputStream os = urlconn.getOutputStream();
            String req1xml = request;
            java.util.zip.CheckedOutputStream cos = new java.util.zip.CheckedOutputStream(os, new java.util.zip.Adler32());
            java.util.zip.GZIPOutputStream gop = new java.util.zip.GZIPOutputStream(cos);
            java.io.OutputStreamWriter dos = new java.io.OutputStreamWriter(gop, "UTF-8");
            System.out.println(req1xml);
            dos.write(req1xml);
            dos.flush();
            dos.close();
            System.out.println("url conn: " + urlconn.getContentEncoding() + "  " + urlconn.getContentType());
            java.io.InputStream ios = urlconn.getInputStream();
            java.util.zip.CheckedInputStream cis = new java.util.zip.CheckedInputStream(ios, new java.util.zip.Adler32());
            java.util.zip.GZIPInputStream gip = new java.util.zip.GZIPInputStream(cis);
            java.io.InputStreamReader br = new java.io.InputStreamReader(gip, "UTF-8");
            retdoc = (new org.jdom.input.SAXBuilder()).build(br);
            xmlResp = (new org.jdom.output.XMLOutputter()).outputString(retdoc);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return xmlResp;
    }
