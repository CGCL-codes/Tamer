    public static synchronized void parserRoutingFile(String fileName) throws IOException {
        if (Main.chunkStoreLocal) return;
        File file = new File(fileName);
        SDFSLogger.getLog().info("Parsing routing config " + fileName);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e1) {
            SDFSLogger.getLog().fatal("unable to parse config file [" + fileName + "]", e1);
            throw new IOException(e1);
        }
        Document doc = null;
        try {
            doc = db.parse(file);
        } catch (SAXException e1) {
            SDFSLogger.getLog().fatal("unable to parse config file [" + fileName + "]", e1);
            throw new IOException(e1);
        }
        doc.getDocumentElement().normalize();
        SDFSLogger.getLog().info("Parsing " + doc.getDocumentElement().getNodeName());
        Element servers = (Element) doc.getElementsByTagName("servers").item(0);
        SDFSLogger.getLog().info("parsing Servers");
        NodeList server = servers.getElementsByTagName("server");
        for (int s = 0; s < server.getLength(); s++) {
            SDFSLogger.getLog().info("Connection to  Servers [" + server.getLength() + "]");
            Element _server = (Element) server.item(s);
            HCServer hcs = new HCServer(_server.getAttribute("host").trim(), Integer.parseInt(_server.getAttribute("port").trim()), Boolean.parseBoolean(_server.getAttribute("use-udp")), Boolean.parseBoolean(_server.getAttribute("compress")));
            try {
                HCServiceProxy.writeServers.put(_server.getAttribute("name").trim(), new HashClientPool(hcs, _server.getAttribute("name").trim(), Integer.parseInt(_server.getAttribute("network-threads"))));
                HCServiceProxy.readServers.put(_server.getAttribute("name").trim(), new HashClientPool(hcs, _server.getAttribute("name").trim(), Integer.parseInt(_server.getAttribute("network-threads"))));
            } catch (Exception e) {
                SDFSLogger.getLog().warn("unable to connect to server " + _server.getAttribute("name").trim(), e);
                throw new IOException("unable to connect to server");
            }
            SDFSLogger.getLog().info("Added Server " + _server.getAttribute("name"));
        }
        Element _c = (Element) doc.getElementsByTagName("chunks").item(0);
        NodeList chunks = _c.getElementsByTagName("chunk");
        for (int s = 0; s < chunks.getLength(); s++) {
            Element chunk = (Element) chunks.item(s);
            HCServiceProxy.writehashRoutes.put(chunk.getAttribute("name").trim(), HCServiceProxy.writeServers.get(chunk.getAttribute("server").trim()));
            HCServiceProxy.readhashRoutes.put(chunk.getAttribute("name").trim(), HCServiceProxy.readServers.get(chunk.getAttribute("server").trim()));
        }
    }
