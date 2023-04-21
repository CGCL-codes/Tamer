    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int iLength = req.getContentLength();
        InputStream reqIn = req.getInputStream();
        OutputStream reqOut = resp.getOutputStream();
        byte buffer[] = null;
        String requestUrl = "http://" + req.getServerName() + ":" + req.getServerPort() + req.getServletPath();
        if (req.isSecure()) {
            requestUrl = "https://" + req.getServerName() + ":" + req.getServerPort() + req.getServletPath();
        }
        if (sg == null) {
            try {
                sg = new SchemaGenerator(requestUrl, this.schemaFilePath, this.wsClass, this.webMethods);
                this.strWsdl = sg.getWSDL();
                this.strXsd = sg.getSchema();
            } catch (Exception e) {
                System.out.println("Failed to create SchemaGenerator in POST: " + e);
                e.printStackTrace();
            }
        }
        if (iLength > 0) {
            RequestHandler rh = null;
            String requestString = null;
            String reply = null;
            buffer = new byte[iLength];
            reqIn.read(buffer, 0, iLength);
            requestString = new String(buffer, 0, iLength);
            try {
                byte[] buf = null;
                rh = new RequestHandler(requestString, this.wsClass, this.hashWebMethods, requestUrl);
                reply = rh.execute();
                resp.setContentType("text/xml");
                buf = reply.getBytes();
                if (buf.length > 10000000) {
                    throw new Exception("Buffer size exceed GAE limit 10.000.000 bytes: " + buf.length);
                }
                resp.setContentLength(buf.length);
                reqOut.write(buf);
                reqOut.flush();
                reqOut.close();
                reqIn.close();
                rh = null;
                Runtime.getRuntime().gc();
            } catch (Exception ex) {
                resp.setContentType("text/plain");
                resp.getWriter().println("WebService error:\n\n");
                ex.printStackTrace(resp.getWriter());
                ex.printStackTrace(System.out);
            }
        } else {
            resp.setContentType("text/html");
            resp.getWriter().println("<H1>WebService</H1>");
            resp.getWriter().println("WSDL url: <a href=\"" + requestUrl + "?wsdl\">" + requestUrl + "?wsdl</a>");
            resp.getWriter().println("<br><br><br>");
            resp.getWriter().println("<br><br><hr>");
            resp.getWriter().println("powered by WSS, WebServiceServlet " + this.version + "<br><a href=\"http://www.vnetcon.org\">www.vnetcon.org</a>");
        }
    }
