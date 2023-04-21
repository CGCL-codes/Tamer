    public String[][] getAllModels() throws OdaException {
        if (modelsList != null) {
            return modelsList;
        }
        try {
            URL url = new URL(serverName + "/AdhocWebService?userid=" + userName + "&password=" + password + "&domain=" + domain + "&component=listbusinessmodels");
            System.out.println("URL r�cup models: " + url);
            URLConnection conn = url.openConnection();
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(conn.getInputStream());
            doc.getDocumentElement().normalize();
            NodeList nodeLst = doc.getElementsByTagName("model");
            modelsList = new String[nodeLst.getLength()][2];
            for (int s = 0; s < nodeLst.getLength(); s++) {
                Node fstNode = nodeLst.item(s);
                if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element Elmnt = (Element) fstNode;
                    NodeList fstNmElmntLst = Elmnt.getElementsByTagName("model_id");
                    Element fstNmElmnt = (Element) fstNmElmntLst.item(0);
                    NodeList fstNm = fstNmElmnt.getChildNodes();
                    System.out.println("Model ID : " + ((Node) fstNm.item(0)).getNodeValue());
                    modelsList[s][0] = ((Node) fstNm.item(0)).getNodeValue();
                    NodeList lstNmElmntLst = Elmnt.getElementsByTagName("model_name");
                    Element lstNmElmnt = (Element) lstNmElmntLst.item(0);
                    NodeList lstNm = lstNmElmnt.getChildNodes();
                    System.out.println("Model NAME : " + ((Node) lstNm.item(0)).getNodeValue());
                    modelsList[s][1] = ((Node) lstNm.item(0)).getNodeValue();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new OdaException(e);
        }
        return modelsList;
    }
