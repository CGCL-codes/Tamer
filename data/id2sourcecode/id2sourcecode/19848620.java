        public ArrayList<HashMap<String, String>> ingest(String uri, String userAgent) {
            ArrayList<HashMap<String, String>> returnValue = new ArrayList<HashMap<String, String>>();
            try {
                DocumentBuilderFactory builderFactory = org.apache.xerces.jaxp.DocumentBuilderFactoryImpl.newInstance();
                builderFactory.setValidating(false);
                DocumentBuilder builder = builderFactory.newDocumentBuilder();
                Document doc;
                if (UtilMethods.isSet(userAgent)) {
                    URL urlObject = new URL(uri);
                    URLConnection con = urlObject.openConnection();
                    con.setRequestProperty("User-Agent", userAgent);
                    InputStream st = con.getInputStream();
                    doc = builder.parse(st);
                } else {
                    doc = builder.parse(uri);
                }
                NodeList items = doc.getElementsByTagName("item");
                for (int i = 0; i < items.getLength(); i++) {
                    try {
                        Element item = (Element) items.item(i);
                        NodeList titles = item.getElementsByTagName("title");
                        String titleValue = "";
                        if (titles.getLength() > 0) {
                            Element title = (Element) titles.item(0);
                            NodeList children = title.getChildNodes();
                            titleValue = children.item(0).getNodeValue();
                        }
                        NodeList links = item.getElementsByTagName("link");
                        String linkValue = "";
                        if (links.getLength() > 0) {
                            Element link = (Element) links.item(0);
                            NodeList children = link.getChildNodes();
                            linkValue = children.item(0).getNodeValue();
                        }
                        NodeList descriptions = item.getElementsByTagName("description");
                        String descriptionValue = "";
                        if (descriptions.getLength() > 0) {
                            Element description = (Element) descriptions.item(0);
                            NodeList children = description.getChildNodes();
                            descriptionValue = children.item(0).getNodeValue();
                        }
                        NodeList pubDates = item.getElementsByTagName("pubDate");
                        String pubDateValue = "";
                        if (pubDates.getLength() > 0) {
                            Element description = (Element) pubDates.item(0);
                            NodeList children = description.getChildNodes();
                            pubDateValue = children.item(0).getNodeValue();
                        }
                        HashMap<String, String> entry = new HashMap<String, String>();
                        entry.put("title", titleValue);
                        entry.put("link", linkValue);
                        entry.put("description", descriptionValue);
                        entry.put("pubDate", pubDateValue);
                        returnValue.add(entry);
                    } catch (Exception ex) {
                        Logger.debug(WebAPI.class, ex.toString());
                    }
                }
            } catch (Exception ex) {
                Logger.debug(RSSWebAPI.class, ex.toString());
            } finally {
                return returnValue;
            }
        }
