    public void ConnectXML(String pURL) {
        Log.i("dev", "Featching by url = " + pURL);
        InputStream stream = null;
        try {
            if (Coords != null) Coords = null;
            if (data.size() > 0) {
                data.removeAll(data);
            }
            URL url = new URL(pURL);
            HttpURLConnection connect = (HttpURLConnection) url.openConnection();
            Log.i("dev", "response = " + Boolean.toString(connect.getResponseCode() == connect.HTTP_OK));
            stream = new BufferedInputStream(connect.getInputStream());
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            try {
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document dom = builder.parse(stream);
                Element root = dom.getDocumentElement();
                items = root.getChildNodes();
                try {
                    Coords = new String[2];
                    Coords[0] = items.item(1).getChildNodes().item(1).getAttributes().getNamedItem("latitude").getNodeValue();
                    Coords[1] = items.item(1).getChildNodes().item(1).getAttributes().getNamedItem("longitude").getNodeValue();
                    Log.i("dev", Coords[0] + ", " + Coords[1]);
                } catch (Exception e) {
                    System.out.println("Failed TestXML() get coords, Exception = " + e);
                    Log.i("dev", "Failed ConnectXML() get coords, Exception = " + e);
                }
                NodeList iContent = items.item(1).getChildNodes().item(1).getChildNodes();
                for (int i = 0; i < iContent.getLength(); i++) {
                    if ((iContent.item(i) != null) && (iContent.item(i).getNodeName().equalsIgnoreCase("FORECAST"))) {
                        ArrayList<String> tmp = new ArrayList<String>();
                        Node item = iContent.item(i);
                        NamedNodeMap attrs = item.getAttributes();
                        tmp.add(attrs.getNamedItem("day").getNodeValue());
                        tmp.add(attrs.getNamedItem("month").getNodeValue());
                        tmp.add(attrs.getNamedItem("year").getNodeValue());
                        tmp.add(attrs.getNamedItem("hour").getNodeValue());
                        tmp.add(attrs.getNamedItem("tod").getNodeValue());
                        tmp.add(attrs.getNamedItem("predict").getNodeValue());
                        tmp.add(attrs.getNamedItem("weekday").getNodeValue());
                        data.add(tmp);
                        try {
                            for (int j = 0; j < iContent.item(i).getChildNodes().getLength(); j++) {
                                item = iContent.item(i).getChildNodes().item(j);
                                if ((item != null) && (!item.getNodeName().equalsIgnoreCase("#text"))) {
                                    ArrayList<String> tmpList = data.get(data.size() - 1);
                                    attrs = item.getAttributes();
                                    String tmpName = item.getNodeName();
                                    if (tmpName.equalsIgnoreCase("PHENOMENA")) {
                                        tmpList.add(attrs.getNamedItem("cloudiness").getNodeValue());
                                        tmpList.add(attrs.getNamedItem("precipitation").getNodeValue());
                                        if (attrs.getNamedItem("rpower") != null) {
                                            tmpList.add(attrs.getNamedItem("rpower").getNodeValue());
                                        } else {
                                            tmpList.add("-1");
                                        }
                                        if (attrs.getNamedItem("spower") != null) {
                                            tmpList.add(attrs.getNamedItem("spower").getNodeValue());
                                        } else {
                                            tmpList.add("-1");
                                        }
                                    } else if (tmpName.equalsIgnoreCase("PRESSURE")) {
                                        tmpList.add(attrs.getNamedItem("max").getNodeValue());
                                        tmpList.add(attrs.getNamedItem("min").getNodeValue());
                                    } else if (tmpName.equalsIgnoreCase("TEMPERATURE")) {
                                        tmpList.add(attrs.getNamedItem("max").getNodeValue());
                                        tmpList.add(attrs.getNamedItem("min").getNodeValue());
                                    } else if (tmpName.equalsIgnoreCase("WIND")) {
                                        tmpList.add(attrs.getNamedItem("max").getNodeValue());
                                        tmpList.add(attrs.getNamedItem("min").getNodeValue());
                                        tmpList.add(attrs.getNamedItem("direction").getNodeValue());
                                    } else if (tmpName.equalsIgnoreCase("RELWET")) {
                                        tmpList.add(attrs.getNamedItem("max").getNodeValue());
                                        tmpList.add(attrs.getNamedItem("min").getNodeValue());
                                    } else if (tmpName.equalsIgnoreCase("HEAT")) {
                                        tmpList.add(attrs.getNamedItem("max").getNodeValue());
                                        tmpList.add(attrs.getNamedItem("min").getNodeValue());
                                    }
                                }
                            }
                        } catch (Exception e) {
                            System.out.println("Failed TestXML() for j, Exception = " + e);
                            Log.i("dev", "Failed ConnectXML() for j, Exception = " + e);
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Failed TestXML() parse, Exception = " + e);
                Log.i("dev", "Failed ConnectXML() parse, Exception = " + e);
            }
        } catch (Exception e) {
            System.out.println("Failed TestXML() connect, Exception = " + e);
            Log.i("dev", "Failed ConnectXML() connect, Exception = " + e);
        }
    }
