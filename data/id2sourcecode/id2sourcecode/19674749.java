    public ServerConfiguration getElementValues() {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db;
        URL url;
        ServerConfiguration serverConfiguration = new ServerConfiguration();
        try {
            db = dbf.newDocumentBuilder();
            String xmlFileName = this.getClass().getResource("/conf/oviyam-config.xml").toString();
            if (xmlFileName.indexOf("default") > 0) {
                File srcFile = new File(this.getClass().getResource("/conf/oviyam-config.xml").toURI());
                File destFile = new File(xmlFileName.substring(5, xmlFileName.indexOf("default")) + "default/" + fname);
                if (!destFile.exists()) {
                    FileInputStream in = new FileInputStream(srcFile);
                    FileOutputStream out = new FileOutputStream(destFile);
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    in.close();
                    out.close();
                }
                url = destFile.toURL();
            } else {
                url = this.getClass().getResource("/conf/oviyam-config.xml");
            }
            InputSource is = new InputSource(url.openStream());
            dom = db.parse(is);
            NodeList root = dom.getElementsByTagName("server");
            for (int i = 0; i < root.getLength(); i++) {
                Element element = (Element) root.item(i);
                NodeList aetitle = element.getElementsByTagName("aetitle");
                Element aetitleElement = (Element) aetitle.item(0);
                serverConfiguration.setAeTitle(aetitleElement.getFirstChild().getNodeValue());
                NodeList hostname = element.getElementsByTagName("hostname");
                Element hostnameElement = (Element) hostname.item(0);
                serverConfiguration.setHostName(hostnameElement.getFirstChild().getNodeValue());
                NodeList port = element.getElementsByTagName("port");
                Element portElement = (Element) port.item(0);
                serverConfiguration.setPort(portElement.getFirstChild().getNodeValue());
                NodeList wadoport = element.getElementsByTagName("wadoport");
                Element wadoportElement = (Element) wadoport.item(0);
                serverConfiguration.setWadoPort(wadoportElement.getFirstChild().getNodeValue());
                NodeList dcmProtocol = element.getElementsByTagName("dcmprotocol");
                Element dcmProtocolElement = (Element) dcmProtocol.item(0);
                serverConfiguration.setDcmProtocol(dcmProtocolElement.getFirstChild().getNodeValue());
            }
        } catch (Exception e) {
            log.error("Unable to get the values from the XML document.", e);
            e.printStackTrace();
        }
        return serverConfiguration;
    }
