    private void buildArcs(TS taxonomySchema, String taxonomySchemaName, String linkbaseName, URL url, String linkbaseSource, String xbrlExtendedLinkRole, String arcName) throws IOException, JDOMException, TaxonomyCreationException {
        Set<URL> builtArcs_ = builtArcs.get(linkbaseName);
        if (builtArcs_ == null) {
            builtArcs_ = new HashSet<URL>();
            builtArcs_.add(url);
            builtArcs.put(linkbaseName, builtArcs_);
        } else if (!builtArcs_.contains(url)) {
            builtArcs_.add(url);
        } else {
            return;
        }
        Document linkbaseDocument = instanceNameToDocument.get(url);
        if (linkbaseDocument == null) {
            try {
                InputStream is = fileLoader.openConnection(url).getInputStream();
                if (is == null) return;
                linkbaseDocument = saxBuilder.build(is);
                instanceNameToDocument.put(url, linkbaseDocument);
            } catch (FileNotFoundException e) {
                return;
            } catch (ConnectException e) {
                return;
            }
        } else {
        }
        @SuppressWarnings("unchecked") List<Element> extendedLinkRolesList = linkbaseDocument.getRootElement().getChildren(xbrlExtendedLinkRole, toJDOM(NamespaceConstants.LINK_NAMESPACE));
        for (Element newExtendedLinkRoleElement : extendedLinkRolesList) {
            String currExtendedLinkRole = newExtendedLinkRoleElement.getAttributeValue("role", toJDOM(NamespaceConstants.XLINK_NAMESPACE));
            @SuppressWarnings("unchecked") List<Element> arcElementsList = newExtendedLinkRoleElement.getChildren(arcName, toJDOM(NamespaceConstants.LINK_NAMESPACE));
            for (Element currArcElement : arcElementsList) {
                String fromAttribute = currArcElement.getAttributeValue("from", toJDOM(NamespaceConstants.XLINK_NAMESPACE));
                String toAttribute = currArcElement.getAttributeValue("to", toJDOM(NamespaceConstants.XLINK_NAMESPACE));
                String arcRole = currArcElement.getAttributeValue("arcrole", toJDOM(NamespaceConstants.XLINK_NAMESPACE));
                String targetRole = currArcElement.getAttributeValue("targetRole", toJDOM(NamespaceConstants.XBRLDT_NAMESPACE));
                String title = currArcElement.getAttributeValue("title", toJDOM(NamespaceConstants.XLINK_NAMESPACE));
                String contextElementName = currArcElement.getAttributeValue("contextElement", toJDOM(NamespaceConstants.XBRLDT_NAMESPACE));
                String order_ = currArcElement.getAttributeValue("order");
                Float order = (order_ != null) ? Float.valueOf(order_) : null;
                String use = currArcElement.getAttributeValue("use");
                String priority_ = currArcElement.getAttributeValue("priority");
                Integer priority = (priority_ != null) ? Integer.valueOf(priority_) : null;
                boolean usable = Boolean.valueOf(currArcElement.getAttributeValue("usable", toJDOM(NamespaceConstants.XBRLDT_NAMESPACE)));
                String weight_ = currArcElement.getAttributeValue("weight");
                Float weight = (weight_ != null) ? Float.valueOf(weight_) : null;
                createArcs(taxonomySchema, taxonomySchemaName, linkbaseName, linkbaseSource, fromAttribute, toAttribute, currExtendedLinkRole, arcRole, targetRole, title, contextElementName, order, use, priority, usable, weight);
            }
        }
    }
