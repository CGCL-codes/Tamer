    private GeoObject getGeoFromUrl(String locName) {
        GeoObject geoObj = null;
        String urlString = geoPath + "/search?maxRows=1&isNameRequired=true&q=" + locName.trim().replaceAll("\\s+", "+") + featClassStr;
        try {
            InputStream inStream = new URL(urlString).openStream();
            Document doc = docBuilder.parse(inStream);
            NodeList totalNote = doc.getElementsByTagName("totalResultsCount");
            int cnt = 0;
            while (totalNote.getLength() == 0 && retry && cnt < 10) {
                Thread.sleep(1000);
                totalNote = doc.getElementsByTagName("totalResultsCount");
                cnt++;
            }
            if (totalNote.getLength() > 0) {
                String totalStr = totalNote.item(0).getTextContent();
                int totalCnt = Integer.parseInt(totalStr);
                NodeList geoNodes = doc.getElementsByTagName("geoname");
                if (geoNodes != null && geoNodes.getLength() > 0) {
                    Element geoElem = (Element) geoNodes.item(0);
                    int pop = 0;
                    for (int i = 0; i < geoNodes.getLength(); i++) {
                        Element tstElem = (Element) geoNodes.item(i);
                        Element nameElem = (Element) tstElem.getElementsByTagName("name").item(0);
                        String nameStr = nameElem.getTextContent().trim();
                        Element popElem = (Element) tstElem.getElementsByTagName("population").item(0);
                        String popStr = popElem.getTextContent().trim();
                        if (popStr != null && popStr.length() > 0 && nameStr.contains(locName)) {
                            int tstPop = Integer.parseInt(popStr);
                            if (tstPop > pop) {
                                geoElem = tstElem;
                                pop = tstPop;
                            }
                        }
                    }
                    Element nameElem = (Element) geoElem.getElementsByTagName("name").item(0);
                    Element typeElem = (Element) geoElem.getElementsByTagName("fcode").item(0);
                    Element latElem = (Element) geoElem.getElementsByTagName("lat").item(0);
                    Element lonElem = (Element) geoElem.getElementsByTagName("lng").item(0);
                    Element cntryElem = (Element) geoElem.getElementsByTagName("countryName").item(0);
                    String nameStr = nameElem.getTextContent().trim();
                    String typeStr = typeElem.getTextContent().trim();
                    GeoFeatureType type = getTypeForCode(typeStr);
                    if (totalCnt < 200 || type.equals(GeoFeatureType.COUNTRY) || type.equals(GeoFeatureType.OTHER)) {
                        String latStr = latElem.getTextContent().trim();
                        String lonStr = lonElem.getTextContent().trim();
                        String cntryStr = cntryElem.getTextContent().trim();
                        double lat = Double.parseDouble(latStr);
                        double lon = Double.parseDouble(lonStr);
                        geoObj = new GeoObject(nameStr);
                        geoObj.setCenter(new GeoCoord(lat, lon));
                        geoObj.addAltName(locName);
                        geoObj.addAltName(nameStr);
                        geoObj.setFeatureType(type);
                        if (cntryStr != null) {
                            geoObj.setCountry(cntryStr);
                            if (type.equals(GeoFeatureType.COUNTRY)) {
                                geoObj.addAltName(cntryStr);
                            }
                        }
                    }
                }
            }
            inStream.close();
        } catch (Exception exc) {
            System.err.println("GeoNamesLocator: Error when retrieving " + urlString + ": Service not available.");
        }
        return geoObj;
    }
