    public void readSite() {
        try {
            conn = feedurl.openConnection();
            httpConn = (HttpURLConnection) conn;
            int responseCode = httpConn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream in = httpConn.getInputStream();
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document dom = db.parse(in);
                Element docEle = dom.getDocumentElement();
                NodeList n1 = docEle.getElementsByTagName("item");
                if (n1 != null && n1.getLength() > 0) {
                    for (int i = 0; i < n1.getLength(); i++) {
                        Element entry = (Element) n1.item(i);
                        Element title = (Element) entry.getElementsByTagName("title").item(0);
                        Element link = (Element) entry.getElementsByTagName("link").item(0);
                        parsedXML.add(new ArrayList<String>());
                        parsedXML.getLast().add(title.getFirstChild().getNodeValue());
                        parsedXML.getLast().add(link.getFirstChild().getNodeValue());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }
