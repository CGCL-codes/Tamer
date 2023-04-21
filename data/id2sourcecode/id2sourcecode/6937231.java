    public String sendReceiveData(String request, String servletName) {
        String ocurl = OCStaticValues.getInstance().getOCURL();
        String response = "";
        try {
            System.out.println(ocurl + "/OpenCatalog/" + servletName);
            java.net.URL url = new java.net.URL("http://" + ocurl + "/OpenCatalog/" + servletName);
            java.net.URLConnection urlconn = (java.net.URLConnection) url.openConnection();
            urlconn.setConnectTimeout(5000);
            urlconn.setDoOutput(true);
            urlconn.setRequestProperty("Content-type", "text/plain; charset=UTF-8");
            java.io.OutputStream os = urlconn.getOutputStream();
            String req1xml = request;
            java.util.zip.CheckedOutputStream cos = new java.util.zip.CheckedOutputStream(os, new java.util.zip.Adler32());
            java.util.zip.GZIPOutputStream gop = new java.util.zip.GZIPOutputStream(cos);
            java.io.OutputStreamWriter dos = new java.io.OutputStreamWriter(gop, "UTF-8");
            System.out.println("#########***********$$$$$$$$##########" + req1xml);
            dos.write(req1xml);
            dos.flush();
            dos.close();
            System.out.println("url conn: " + urlconn.getContentEncoding() + "  " + urlconn.getContentType());
            java.io.InputStream ios = urlconn.getInputStream();
            java.util.zip.CheckedInputStream cis = new java.util.zip.CheckedInputStream(ios, new java.util.zip.Adler32());
            java.util.zip.GZIPInputStream gip = new java.util.zip.GZIPInputStream(cis);
            java.io.BufferedReader br = new BufferedReader(new java.io.InputStreamReader(gip, "UTF-8"));
            while (br.ready()) {
                response += br.readLine();
            }
        } catch (java.net.ConnectException conexp) {
        } catch (Exception exp) {
            exp.printStackTrace(System.out);
        }
        return response;
    }
