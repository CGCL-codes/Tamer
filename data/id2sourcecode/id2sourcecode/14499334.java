    private ArrayList<String> getListFromXml() {
        ArrayList<String> list = new ArrayList<String>();
        try {
            URL url = new URL("http://www.ems.net.tr/aurunlerkisa.xml");
            DocumentBuilderFactory dFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dFactory.newDocumentBuilder();
            Document document = dBuilder.parse(new InputSource(url.openStream()));
            document.getDocumentElement().normalize();
            NodeList nodeListCountry = document.getElementsByTagName("MerchantItem");
            String urunsayi = Integer.toString(nodeListCountry.getLength());
            for (int i = 0; i < 100; i++) {
                Log.d("urunsayi", urunsayi);
                Node node = nodeListCountry.item(i);
                Element elementMain = (Element) node;
                NodeList nodeListText = elementMain.getElementsByTagName("itemTitle");
                Element elementText = (Element) nodeListText.item(0);
                NodeList nodeListValue = elementMain.getElementsByTagName("kdv_dahil");
                Element elementValue = (Element) nodeListValue.item(0);
                list.add(elementText.getChildNodes().item(0).getNodeValue() + "--->" + elementValue.getChildNodes().item(0).getNodeValue());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
