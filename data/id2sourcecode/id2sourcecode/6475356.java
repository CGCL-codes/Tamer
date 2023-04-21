    public Object sendObjectRequest(java.lang.String servletName, java.lang.Object request) {
        Object reqxml = null;
        org.jdom.Document retdoc = null;
        String myurl = org.verus.ngl.client.components.NGLUtilities.getInstance().getServerIp();
        String myport = org.verus.ngl.client.components.NGLUtilities.getInstance().getServerPort();
        if (myport == null || myport.trim().equals("")) {
            myport = "8080";
        }
        if (myurl == null || myurl.trim().equals("")) {
            myurl = "localhost";
        }
        try {
            java.net.URL url = new java.net.URL("http://" + myurl + ":" + myport + "/NGL/FileUploadDownLoadServlet?HANDLER=" + servletName);
            System.out.println("sendObjectRequest: " + "http://" + myurl + ":" + myport + "/NGL/FileUploadDownLoadServlet?HANDLER=" + servletName);
            java.net.URLConnection urlconn = (java.net.URLConnection) url.openConnection();
            urlconn.setDoOutput(true);
            java.io.OutputStream os = urlconn.getOutputStream();
            java.util.zip.CheckedOutputStream cos = new java.util.zip.CheckedOutputStream(os, new java.util.zip.Adler32());
            java.util.zip.GZIPOutputStream gop = new java.util.zip.GZIPOutputStream(cos);
            java.io.ObjectOutputStream dos = new java.io.ObjectOutputStream(gop);
            dos.writeObject(request);
            dos.flush();
            dos.close();
            java.io.InputStream ios = urlconn.getInputStream();
            java.util.zip.CheckedInputStream cis = new java.util.zip.CheckedInputStream(ios, new java.util.zip.Adler32());
            java.util.zip.GZIPInputStream gip = new java.util.zip.GZIPInputStream(cis);
            java.io.ObjectInputStream br = new java.io.ObjectInputStream(gip);
            reqxml = br.readObject();
        } catch (Exception exp) {
            exp.printStackTrace(System.out);
            System.out.println("Exception in Servlet Connector: " + exp);
        }
        return reqxml;
    }
