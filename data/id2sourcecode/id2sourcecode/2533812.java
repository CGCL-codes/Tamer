    private void configureSSL() throws IOException {
        if (sslKeyStoreFile == null) {
            return;
        }
        File serverXMLFile = new File(tomcatHome, "conf" + File.separator + "server.xml");
        Files.write(Files.toString(serverXMLFile, Charset.forName("UTF-8")).replace("        <Connector port=\"8009\" protocol=\"AJP/1.3\" redirectPort=\"8443\" />", "        <Connector port=\"8009\" protocol=\"AJP/1.3\" redirectPort=\"8443\" />\n" + "        <Connector port=\"" + httpsPort + "\" protocol=\"HTTP/1.1\" SSLEnabled=\"true\"\n" + "               maxThreads=\"150\" scheme=\"https\" secure=\"true\"\n" + "               clientAuth=\"false\" sslProtocol=\"TLS\"\n" + "               keystoreFile=\"" + sslKeyStoreFile + "\" " + "keystorePass=\"" + Strings.nullToEmpty(sslKeyStorePassword) + "\" />").replace("8443", Integer.toString(httpsPort)), serverXMLFile, Charset.forName("UTF-8"));
    }
