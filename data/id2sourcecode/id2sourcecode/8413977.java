    private boolean buildLinkbase(TS taxonomySchema, String taxonomySchemaName, String name, String extendedLinkRole, URL url, String linkbaseSource, Collection<String> references) throws IOException, JDOMException, TaxonomyCreationException {
        Set<URL> builtLinkbases_ = builtLinkbases.get(name);
        if (builtLinkbases_ == null) {
            builtLinkbases_ = new HashSet<URL>();
            builtLinkbases_.add(url);
            builtLinkbases.put(name, builtLinkbases_);
        } else if (!builtLinkbases_.contains(url)) {
            builtLinkbases_.add(url);
        } else {
            return true;
        }
        Document linkbaseDocument = instanceNameToDocument.get(url);
        if (linkbaseDocument == null) {
            try {
                URLConnection conn = fileLoader.openConnection(url);
                if (conn.getContentType() == null || (!conn.getContentType().contains("application/xml") && !conn.getContentType().contains("text/xml"))) return false;
                InputStream is = conn.getInputStream();
                if (is == null) return false;
                if (references == null) log.log(LogLevel.INFO, LOG_CHANNEL, "Building linkbase document " + linkbaseSource + "...");
                linkbaseDocument = saxBuilder.build(is);
                instanceNameToDocument.put(url, linkbaseDocument);
            } catch (FileNotFoundException e) {
                return false;
            } catch (ConnectException e) {
                return false;
            }
        } else {
            if (references == null) log.log(LogLevel.INFO, LOG_CHANNEL, "Building linkbase document " + linkbaseSource + "...");
        }
        @SuppressWarnings("unchecked") List<Element> extendedLinkRolesList = linkbaseDocument.getRootElement().getChildren(extendedLinkRole, toJDOM(NamespaceConstants.LINK_NAMESPACE));
        for (Element newExtendedLinkRoleElement : extendedLinkRolesList) {
            String currExtendedLinkRole = newExtendedLinkRoleElement.getAttributeValue("role", toJDOM(NamespaceConstants.XLINK_NAMESPACE));
            if (references == null) addExtendedLinkRole(taxonomySchema, taxonomySchemaName, name, currExtendedLinkRole);
            @SuppressWarnings("unchecked") List<Element> linkbaseElements = newExtendedLinkRoleElement.getChildren();
            for (Element currLinkbaseElement : linkbaseElements) {
                String typeAttrValue = currLinkbaseElement.getAttributeValue("type", toJDOM(NamespaceConstants.XLINK_NAMESPACE));
                if (typeAttrValue != null && (typeAttrValue.equals("locator") || typeAttrValue.equals("resource"))) {
                    String label = currLinkbaseElement.getAttributeValue("label", toJDOM(NamespaceConstants.XLINK_NAMESPACE));
                    if (label == null || label.length() == 0) {
                        log.log(LogLevel.ERROR, LOG_CHANNEL, "Could not find label for extended link element");
                    }
                    if (typeAttrValue.equals("locator")) {
                        String conceptName = currLinkbaseElement.getAttributeValue("href", toJDOM(NamespaceConstants.XLINK_NAMESPACE));
                        if (conceptName == null) {
                            log.log(LogLevel.ERROR, LOG_CHANNEL, "Could not find concept the label refers to");
                        } else {
                            try {
                                conceptName = java.net.URLDecoder.decode(conceptName, "UTF-8");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if (references != null) {
                            if (conceptName != null) {
                                String reference = conceptName.substring(0, conceptName.indexOf("#"));
                                references.add(reference);
                            }
                        } else {
                            String elementId = null;
                            if (conceptName != null) {
                                elementId = conceptName.substring(conceptName.indexOf("#") + 1);
                            }
                            String id = currLinkbaseElement.getAttributeValue("id");
                            String role = currLinkbaseElement.getAttributeValue("role", toJDOM(NamespaceConstants.XLINK_NAMESPACE));
                            String title = currLinkbaseElement.getAttributeValue("title", toJDOM(NamespaceConstants.XLINK_NAMESPACE));
                            createLocator(taxonomySchema, taxonomySchemaName, name, linkbaseSource, label, currExtendedLinkRole, id, role, title, conceptName, elementId);
                        }
                    } else if (references == null) {
                        String role = currLinkbaseElement.getAttributeValue("role", toJDOM(NamespaceConstants.XLINK_NAMESPACE));
                        String title = currLinkbaseElement.getAttributeValue("title", toJDOM(NamespaceConstants.XLINK_NAMESPACE));
                        String id = currLinkbaseElement.getAttributeValue("id");
                        String lang = currLinkbaseElement.getAttributeValue("lang", toJDOM(NamespaceConstants.XML_NAMESPACE));
                        String value = currLinkbaseElement.getValue();
                        ArrayList<String[]> hgbRefs = null;
                        ArrayList<String[]> refs = null;
                        {
                            hgbRefs = new ArrayList<String[]>();
                            for (Object o : currLinkbaseElement.getChildren()) {
                                if ((o instanceof Element) && (((Element) o).getNamespace().getPrefix().equals(NamespaceConstants.HGBREF_NAMESPACE.getPrefix()))) {
                                    Element elem = (Element) o;
                                    hgbRefs.add(new String[] { elem.getName(), elem.getValue() });
                                }
                            }
                            refs = new ArrayList<String[]>();
                            for (Object o : currLinkbaseElement.getChildren()) {
                                if ((o instanceof Element) && (((Element) o).getNamespace().getPrefix().equals(NamespaceConstants.REF_NAMESPACE.getPrefix()))) {
                                    Element elem = (Element) o;
                                    refs.add(new String[] { elem.getName(), elem.getValue() });
                                }
                            }
                        }
                        createResource(taxonomySchema, taxonomySchemaName, name, linkbaseSource, label, currExtendedLinkRole, role, title, id, lang, value, hgbRefs, refs);
                    }
                }
            }
        }
        return true;
    }
