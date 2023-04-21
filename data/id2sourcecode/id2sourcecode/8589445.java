    public void fetchServerList() {
        try {
            serverList.clearSelection();
            servermodel.clear();
            servers.clear();
            String meta = referenceManager.getPreference("meta.server", "http://www.openrpg.com/openrpg_servers.php");
            URL url = new URL(meta + "?version=" + referenceManager.getSettingAttribute("client", "protocol", "version", "0.9.2"));
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(url.openStream());
            NodeList nl = doc.getElementsByTagName("server");
            for (int loop = 0; loop < nl.getLength(); loop++) {
                Element e = (Element) nl.item(loop);
                String name = e.getAttribute("name");
                String address = e.getAttribute("address");
                String reliability = e.getAttribute("failed_count");
                server_info info = new server_info(name, address, reliability);
                servers.add(info);
            }
            for (int loop = 0; loop < servers.size(); loop++) {
                servermodel.addElement((server_info) servers.get(loop));
            }
        } catch (Exception ex) {
            ExceptionHandler.handleException(ex);
        }
    }
