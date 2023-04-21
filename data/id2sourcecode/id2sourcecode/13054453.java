    private void parseDependenciesFile() {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            URL url = FileLocator.find(ServiceCreationPlugin.getDefault().getBundle(), new Path("/eclipslee-maven-dependencies.xml"), null);
            URL furl = FileLocator.toFileURL(url);
            Document doc = db.parse(furl.openStream());
            NodeList compNodeList = doc.getDocumentElement().getElementsByTagName("sbb");
            if (compNodeList.getLength() != 1) {
            } else {
                NodeList elemChilds = compNodeList.item(0).getChildNodes();
                for (int i = 0; i < elemChilds.getLength(); i++) {
                    if (elemChilds.item(i) instanceof Element) {
                        Element e = (Element) elemChilds.item(i);
                        String componentType = e.getNodeName();
                        NodeList dependencies = e.getChildNodes();
                        if (dependencies.getLength() == 0) {
                        } else {
                            for (int j = 0; j < dependencies.getLength(); j++) {
                                if (dependencies.item(j) instanceof Element) {
                                    Element depElem = (Element) dependencies.item(j);
                                    String depGroupId = depElem.getElementsByTagName("groupId").item(0).getTextContent();
                                    String depArtifactId = depElem.getElementsByTagName("artifactId").item(0).getTextContent();
                                    String depVersion = depElem.getElementsByTagName("version").item(0).getTextContent();
                                    String depDesc = depElem.getElementsByTagName("description").getLength() > 0 ? depElem.getElementsByTagName("description").item(0).getTextContent() : "";
                                    switch(ComponentType.fromString(componentType)) {
                                        case ENABLER:
                                            enablersComponents.add(new ComponentEntry(ComponentType.fromString(componentType), depGroupId, depArtifactId, depVersion, depDesc));
                                            break;
                                        case RATYPE:
                                            ratypesComponents.add(new ComponentEntry(ComponentType.fromString(componentType), depGroupId, depArtifactId, depVersion, depDesc));
                                            break;
                                        case LIBRARY:
                                            librariesComponents.add(new ComponentEntry(ComponentType.fromString(componentType), depGroupId, depArtifactId, depVersion, depDesc));
                                            break;
                                        default:
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
