    public String getPhoneNo() {
        HttpClient httpclient = null;
        String phoneNo = "";
        try {
            httpclient = new DefaultHttpClient();
            URI uri = getURI(url);
            HttpGet httpget = new HttpGet(uri);
            HttpResponse response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docbuilder = dbfactory.newDocumentBuilder();
            Document doc = docbuilder.parse(entity.getContent());
            NodeList nodeList = doc.getElementsByTagName("a");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                NamedNodeMap nnm = node.getAttributes();
                if (nnm.getNamedItem("href") == null) {
                    continue;
                }
                String value = nnm.getNamedItem("href").getNodeValue();
                if (value.indexOf("TEL:") != -1) {
                    phoneNo = value.split(":")[1];
                    break;
                }
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        return phoneNo;
    }
