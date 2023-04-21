    public WebMapServer(URL url) {
        URLConnection urlc;
        try {
            urlc = url.openConnection();
            urlc.setReadTimeout(Navigator.TIME_OUT);
            InputStream urlIn = urlc.getInputStream();
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(urlIn);
            NodeList WMT_MS_Capabilities_NodeList = document.getElementsByTagName("WMT_MS_Capabilities");
            int NodeList_length = WMT_MS_Capabilities_NodeList.getLength();
            if (NodeList_length == 1) {
                org.w3c.dom.Node WMT_MS_Capabilities_Node = WMT_MS_Capabilities_NodeList.item(0);
                wMT_MS_Capabilities = WMT_MS_Capabilities.parse(WMT_MS_Capabilities_Node);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
