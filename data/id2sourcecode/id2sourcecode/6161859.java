        private void parseDependenciesFile(Composite composite) {
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
                    new Label(composite, SWT.NONE).setText("Components");
                    final Combo componentTemplatesCombo = new Combo(composite, SWT.DROP_DOWN | SWT.BORDER | SWT.READ_ONLY);
                    componentTemplatesCombo.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL));
                    final HashMap<String, String[]> descToIds = new HashMap<String, String[]>();
                    for (int i = 0; i < elemChilds.getLength(); i++) {
                        if (elemChilds.item(i) instanceof Element) {
                            Element e = (Element) elemChilds.item(i);
                            componentTemplatesCombo.add(TEMPLATES_SEPARATOR_STRING + (e.hasAttribute("description") ? e.getAttribute("description") : e.getNodeName()) + TEMPLATES_SEPARATOR_STRING);
                            componentTemplatesCombo.addModifyListener(new ModifyListener() {

                                public void modifyText(ModifyEvent event) {
                                    String selected = componentTemplatesCombo.getItem(componentTemplatesCombo.getSelectionIndex());
                                    if (selected.startsWith(TEMPLATES_SEPARATOR_STRING)) {
                                        componentTemplatesCombo.pack();
                                    } else {
                                        String[] values = descToIds.get(componentTemplatesCombo.getItem(componentTemplatesCombo.getSelectionIndex()));
                                        depGroupId.setText(values[0]);
                                        depArtifactId.setText(values[1]);
                                        depVersion.setText(values[2]);
                                    }
                                }
                            });
                            NodeList dependencies = e.getChildNodes();
                            for (int j = 0; j < dependencies.getLength(); j++) {
                                if (dependencies.item(j) instanceof Element) {
                                    Element depElem = (Element) dependencies.item(j);
                                    String depGroupId = depElem.getElementsByTagName("groupId").item(0).getTextContent();
                                    String depArtifactId = depElem.getElementsByTagName("artifactId").item(0).getTextContent();
                                    String depVersion = depElem.getElementsByTagName("version").item(0).getTextContent();
                                    String depDesc = depElem.getElementsByTagName("description").getLength() >= 1 ? depElem.getElementsByTagName("description").item(0).getTextContent() : depGroupId + " : " + depArtifactId + " : " + depVersion;
                                    componentTemplatesCombo.add(depDesc);
                                    descToIds.put(depDesc, new String[] { depGroupId, depArtifactId, depVersion });
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
