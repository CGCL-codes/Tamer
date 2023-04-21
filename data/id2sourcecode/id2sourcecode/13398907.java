    public static void main(final String[] args) throws Exception {
        String soapURL = "http://localhost:8080/services/BusinessManager_QueryProductCatalog?wsdl";
        String xmlFile2Send = "properties/soap.xml";
        String soapAction = "";
        if (args.length > 2) {
            soapAction = args[2];
        }
        URL url = new URL(soapURL);
        URLConnection connection = url.openConnection();
        HttpURLConnection httpConn = (HttpURLConnection) connection;
        FileInputStream fin = new FileInputStream(xmlFile2Send);
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        copy(fin, bout);
        fin.close();
        byte[] b = bout.toByteArray();
        httpConn.setRequestProperty("Content-Length", String.valueOf(b.length));
        httpConn.setRequestProperty("Content-Type", "application/soap+xml; charset=UTF-8");
        httpConn.setRequestProperty("SOAPAction", soapAction);
        httpConn.setRequestMethod("POST");
        httpConn.setDoOutput(true);
        httpConn.setDoInput(true);
        OutputStream out = httpConn.getOutputStream();
        out.write(b);
        out.close();
        InputStreamReader isr = new InputStreamReader(httpConn.getInputStream());
        BufferedReader in = new BufferedReader(isr);
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        javax.xml.parsers.DocumentBuilderFactory factory = javax.xml.parsers.DocumentBuilderFactory.newInstance();
        javax.xml.parsers.DocumentBuilder db;
        db = factory.newDocumentBuilder();
        org.xml.sax.InputSource inStream = new org.xml.sax.InputSource();
        inStream.setCharacterStream(new java.io.StringReader(response.toString()));
        System.out.println(response.toString());
        org.w3c.dom.Document doc = db.parse(inStream);
        doc.getDocumentElement().normalize();
        org.w3c.dom.NodeList uuidList = doc.getElementsByTagName("uuid");
        for (int k = 0; k < uuidList.getLength(); k++) {
            org.w3c.dom.Node key = uuidList.item(k);
            if (key.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                org.w3c.dom.Element valueElement = (org.w3c.dom.Element) key;
                org.w3c.dom.NodeList fstNmElmntLst = valueElement.getElementsByTagName("value");
                org.w3c.dom.Element fstNmElmnt = (org.w3c.dom.Element) fstNmElmntLst.item(0);
                org.w3c.dom.NodeList fstNm = fstNmElmnt.getChildNodes();
                System.out.println("SLATemplateID : " + ((org.w3c.dom.Node) fstNm.item(0)).getNodeValue());
            }
        }
    }
