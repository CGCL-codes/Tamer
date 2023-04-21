    public static Service[] parse(String s, String s1, String s2) {
        try {
            ArrayList arraylist;
            Service aservice[];
            arraylist = new ArrayList();
            URL url = new URL(s + "/gps/menu?alias=" + s1 + "&hCheck='" + s2.replace(" ", "%20") + "'");
            DocumentBuilderFactory documentbuilderfactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentbuilder = documentbuilderfactory.newDocumentBuilder();
            Document document = documentbuilder.parse(url.openStream());
            NodeList nodelist = document.getElementsByTagName("entryset");
            for (int i = 0; i < nodelist.getLength(); i++) {
                Element element = (Element) nodelist.item(i);
                String s3 = element.getAttribute("name");
                String s4 = element.getAttribute("src");
                if (s4 == null || s4.equals("")) s4 = ""; else if (s4.length() < 7 || !s4.substring(0, 7).equals("http://")) s4 = s + s4;
                arraylist.add(new Service(s3, s1 + "." + s2 + "." + s3, s4));
                NodeList nodelist1 = element.getChildNodes();
                for (int j = 0; j < nodelist1.getLength(); j++) {
                    if (nodelist1.item(j).getNodeType() != 1 || !nodelist1.item(j).getNodeName().equals("entry")) continue;
                    Element element1 = (Element) nodelist1.item(j);
                    String s5 = element1.getAttribute("name");
                    String s6 = element1.getAttribute("src");
                    if (s6 == null || s6.equals("")) s6 = ""; else if (s6.length() < 7 || !s6.substring(0, 7).equals("http://")) s6 = s + s6;
                    arraylist.add(new Service(s5, s1 + "." + s2 + "." + s3 + "." + s5, s6));
                    NodeList nodelist2 = element1.getChildNodes();
                    for (int k = 0; k < nodelist2.getLength(); k++) {
                        if (nodelist2.item(k).getNodeType() != 1 || !nodelist2.item(k).getNodeName().equals("menu")) continue;
                        Element element2 = (Element) nodelist2.item(k);
                        String s7 = element2.getAttribute("name");
                        String s8 = element2.getAttribute("src");
                        if (s8 == null || s8.equals("")) s8 = ""; else if (s8.length() < 7 || !s8.substring(0, 7).equals("http://")) s8 = s + s8;
                        arraylist.add(new Service(s7, s1 + "." + s2 + "." + s3 + "." + s5 + "." + s7, s8));
                    }
                }
            }
            aservice = new Service[arraylist.size()];
            return (Service[]) arraylist.toArray(aservice);
        } catch (Exception ex) {
            System.err.println("---Error occured. MenuXmlParser.parse()---");
            System.out.println("Exception:" + ex.getMessage());
            ex.printStackTrace();
            return null;
        }
    }
