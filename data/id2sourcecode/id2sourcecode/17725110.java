    protected void writeXml(String xmlName, String sFile) throws Exception {
        log.info("<< GenXml.writeXml xmlName = " + xmlName);
        try {
            File f = null;
            DataOutputStream dos = null;
            if (xmlName.indexOf("http:") == -1) {
                log.info("<< GenXml.writeXml - file - BufferedWriter");
                f = new File(xmlName);
                Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f), StringUtilities.XML_DEFAULT_ENCODING));
                out.write(sFile);
                out.flush();
                out.close();
            } else {
                log.info("<< GenXml.writeXml - url");
                URL url = new URL(xmlName);
                URLConnection connection = url.openConnection();
                connection.setDoOutput(true);
                PrintWriter printWriter = new PrintWriter(connection.getOutputStream());
                printWriter.write(sFile);
                printWriter.flush();
                printWriter.close();
            }
        } catch (Exception e) {
            log.info("<< GenXml.writeXml - Exception : '" + e.getMessage() + "'");
            throw new Exception(" Exception - writeXml : " + e.getMessage());
        }
    }
