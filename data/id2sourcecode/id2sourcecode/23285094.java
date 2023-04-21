    public LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, Object>>> parseRssFeed(String urlEndPoint) {
        try {
            LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, Object>>> lMap = new LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, Object>>>();
            URL url = new URL(urlEndPoint);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; .NET CLR 1.1.4322)");
            conn.setRequestProperty("Referer", "http://code.google.com/p/openmeetings/");
            conn.connect();
            SAXReader reader = new SAXReader();
            Document document = reader.read(conn.getInputStream());
            Element root = document.getRootElement();
            int l = 0;
            for (Iterator i = root.elementIterator(); i.hasNext(); ) {
                Element item = (Element) i.next();
                LinkedHashMap<String, LinkedHashMap<String, Object>> items = new LinkedHashMap<String, LinkedHashMap<String, Object>>();
                boolean isSubElement = false;
                for (Iterator it2 = item.elementIterator(); it2.hasNext(); ) {
                    Element subItem = (Element) it2.next();
                    LinkedHashMap<String, Object> itemObj = new LinkedHashMap<String, Object>();
                    itemObj.put("name", subItem.getName());
                    itemObj.put("text", subItem.getText());
                    LinkedHashMap<String, String> attributes = new LinkedHashMap<String, String>();
                    for (Iterator attr = subItem.attributeIterator(); attr.hasNext(); ) {
                        Attribute at = (Attribute) attr.next();
                        attributes.put(at.getName(), at.getText());
                    }
                    itemObj.put("attributes", attributes);
                    items.put(subItem.getName(), itemObj);
                    isSubElement = true;
                }
                if (isSubElement) {
                    l++;
                    lMap.put("item" + l, items);
                }
            }
            return lMap;
        } catch (Exception err) {
            log.error("[parseRssFeed]", err);
        }
        return null;
    }
