    @SuppressWarnings("deprecation")
    private boolean postXml(Document xml, String id) {
        try {
            URL url;
            URLConnection urlConn;
            DataOutputStream printout;
            DataInputStream input;
            url = new URL(rootUrl + "djudge");
            urlConn = url.openConnection();
            urlConn.setDoInput(true);
            urlConn.setDoOutput(true);
            urlConn.setUseCaches(false);
            urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            printout = new DataOutputStream(urlConn.getOutputStream());
            String content = "action=post&id=" + id + "&xml=" + URLEncoder.encode(XmlTools.formatDoc(xml), "UTF-8");
            printout.writeBytes(content);
            printout.flush();
            printout.close();
            FileTools.writeFileContent("./dumps/sources/judge/" + logDir + "/" + id + ".xml", XmlTools.formatDoc(xml));
            input = new DataInputStream(urlConn.getInputStream());
            while (null != input.readLine()) ;
            input.close();
            callback.reportConnectionRecovered(judgeLinkId, "");
            return true;
        } catch (MalformedURLException me) {
            System.err.println("MalformedURLException: " + me);
            me.printStackTrace();
            log.error("fechXML", me);
            callback.reportError(judgeLinkId, me.getMessage());
        } catch (IOException ioe) {
            System.err.println("IOException: " + ioe.getMessage());
            ioe.printStackTrace();
            callback.reportConnectionLost(judgeLinkId, ioe.getMessage());
        }
        return false;
    }
