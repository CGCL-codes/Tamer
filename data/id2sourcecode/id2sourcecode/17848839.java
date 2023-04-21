    public static Map<String, Integer> getDistances(String city, List<String> cities) {
        InputStream myStream = null;
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dbf.newDocumentBuilder();
            StringBuilder url = new StringBuilder("http://maps.googleapis.com/maps/api/distancematrix/xml?origins=");
            url.append(city.replace(' ', '+'));
            url.append("&destinations=");
            for (String s : cities) {
                String s2 = s.replace(' ', '+');
                url.append(s2);
                url.append("|");
            }
            url.append("&sensor=false");
            myStream = new URL(url.toString()).openStream();
            Document doc = builder.parse(myStream);
            NodeList myDistances = doc.getElementsByTagName("element");
            Map<String, Integer> distances = new HashMap<String, Integer>();
            for (int i = 0; i < myDistances.getLength(); i++) {
                if (!((Element) myDistances.item(i)).getElementsByTagName("status").item(0).getTextContent().equals("OK")) {
                    distances.put(cities.get(i).toLowerCase(), Integer.MAX_VALUE);
                } else {
                    distances.put(cities.get(i).toLowerCase(), Integer.parseInt(myDistances.item(i).getChildNodes().item(5).getChildNodes().item(1).getTextContent()));
                }
            }
            return distances;
        } catch (SAXException ex) {
            Logger.getLogger(DistanceParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DistanceParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(DistanceParser.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                myStream.close();
            } catch (IOException ex) {
                Logger.getLogger(DistanceParser.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
