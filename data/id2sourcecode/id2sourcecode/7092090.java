    public void start(XFormsElementContext context, String uri, String localname, String qname, Attributes attributes) throws SAXException {
        final AttributesImpl newAttributes = new AttributesImpl(attributes);
        Map prefixToURI = context.getCurrentPrefixToURIMap();
        if (("if".equals(localname) || "when".equals(localname)) && Constants.XXFORMS_NAMESPACE_URI.equals(uri)) {
            String test = attributes.getValue("test");
            PooledXPathExpression expr = XPathCache.getXPathExpression(context.getPipelineContext(), context.getDocumentWrapper().wrap(context.getCurrentSingleNode()), "boolean(" + test + ")", prefixToURI, context.getRepeatIdToIndex());
            try {
                Boolean value = (Boolean) expr.evaluateSingle();
                addExtensionAttribute(newAttributes, "value", Boolean.toString(value.booleanValue()));
            } catch (XPathException e) {
                throw new OXFException(e);
            } finally {
                if (expr != null) expr.returnToPool();
            }
        } else if (context.getParentElement(0) instanceof Itemset && ("copy".equals(localname) || "label".equals(localname))) {
            Itemset itemset = (Itemset) context.getParentElement(0);
            if ("copy".equals(localname)) {
                itemset.setCopyRef(attributes.getValue("ref"), prefixToURI);
            } else {
                itemset.setLabelRef(attributes.getValue("ref"), prefixToURI);
            }
        } else {
            boolean bindPresent = attributes.getIndex("", "bind") != -1;
            boolean refPresent = attributes.getIndex("", "ref") != -1;
            boolean nodesetPresent = attributes.getIndex("", "nodeset") != -1;
            boolean positionPresent = attributes.getIndex(Constants.XXFORMS_NAMESPACE_URI, "position") != -1;
            if (refPresent || bindPresent || nodesetPresent || positionPresent) {
                InstanceData currentNodeInstanceData = XFormsUtils.getInstanceData(context.getCurrentSingleNode());
                addExtensionAttribute(newAttributes, Constants.XXFORMS_READONLY_ATTRIBUTE_NAME, Boolean.toString(currentNodeInstanceData.getReadonly().get()));
                addExtensionAttribute(newAttributes, Constants.XXFORMS_RELEVANT_ATTRIBUTE_NAME, Boolean.toString(currentNodeInstanceData.getRelevant().get()));
                addExtensionAttribute(newAttributes, Constants.XXFORMS_REQUIRED_ATTRIBUTE_NAME, Boolean.toString(currentNodeInstanceData.getRequired().get()));
                addExtensionAttribute(newAttributes, Constants.XXFORMS_VALID_ATTRIBUTE_NAME, Boolean.toString(currentNodeInstanceData.getValid().get()));
                if (currentNodeInstanceData.getInvalidBindIds() != null) addExtensionAttribute(newAttributes, Constants.XXFORMS_INVALID_BIND_IDS_ATTRIBUTE_NAME, currentNodeInstanceData.getInvalidBindIds());
                if (DATA_CONTROLS.containsKey(localname)) {
                    currentNodeInstanceData.setGenerated(true);
                    String id = Integer.toString(currentNodeInstanceData.getId());
                    if (XFormsUtils.isNameEncryptionEnabled()) id = SecureUtils.encrypt(context.getPipelineContext(), context.getEncryptionPassword(), id);
                    addExtensionAttribute(newAttributes, "name", "$node^" + id);
                    addExtensionAttribute(newAttributes, "value", context.getRefValue());
                } else if (ACTION_CONTROLS.containsKey(localname)) {
                    addExtensionAttribute(newAttributes, "value", context.getRefValue());
                }
                if (!positionPresent) {
                    StringBuffer ids = new StringBuffer();
                    boolean first = true;
                    for (Iterator i = context.getCurrentNodeset().iterator(); i.hasNext(); ) {
                        Node node = (Node) i.next();
                        if (!first) ids.append(' '); else first = false;
                        String id = Integer.toString(XFormsUtils.getInstanceData(node).getId());
                        if (XFormsUtils.isNameEncryptionEnabled()) id = SecureUtils.encrypt(context.getPipelineContext(), context.getEncryptionPassword(), id);
                        ids.append(id);
                    }
                    addExtensionAttribute(newAttributes, Constants.XXFORMS_NODE_IDS_ATTRIBUTE_NAME, ids.toString());
                }
            }
            if (attributes.getIndex("", "at") != -1) {
                NodeInfo contextNode = context.getDocumentWrapper().wrap(context.getCurrentSingleNode());
                PooledXPathExpression expr = XPathCache.getXPathExpression(context.getPipelineContext(), contextNode, "round(" + attributes.getValue("at") + ")", context.getCurrentPrefixToURIMap(), null, context.getFunctionLibrary());
                try {
                    Object at = expr.evaluateSingle();
                    if (!(at instanceof Number)) throw new ValidationException("'at' expression must return a number", new LocationData(context.getLocator()));
                    String atString = at.toString();
                    if (XFormsUtils.isNameEncryptionEnabled()) atString = SecureUtils.encrypt(context.getPipelineContext(), context.getEncryptionPassword(), atString);
                    addExtensionAttribute(newAttributes, "at-value", atString);
                } catch (XPathException e) {
                    throw new OXFException(e);
                } finally {
                    if (expr != null) expr.returnToPool();
                }
            }
            if (attributes.getIndex("", "value") != -1) {
                PooledXPathExpression expr = XPathCache.getXPathExpression(context.getPipelineContext(), context.getDocumentWrapper().wrap(context.getCurrentSingleNode()), "string(" + attributes.getValue("value") + ")", context.getCurrentPrefixToURIMap(), null, context.getFunctionLibrary());
                try {
                    Object value = expr.evaluateSingle();
                    if (!(value instanceof String)) throw new ValidationException("'value' expression must return a string", new LocationData(context.getLocator()));
                    addExtensionAttribute(newAttributes, "value-value", (String) value);
                } catch (XPathException e) {
                    throw new OXFException(e);
                } finally {
                    if (expr != null) expr.returnToPool();
                }
            }
            if (attributes.getIndex("", "src") != -1 && LINKING_CONTROLS.containsKey(localname)) {
                try {
                    String src = attributes.getValue("src");
                    URL url = URLFactory.createURL(src);
                    InputStreamReader stream = new InputStreamReader(url.openStream());
                    StringBuffer value = new StringBuffer();
                    char[] buff = new char[BUFFER_SIZE];
                    int c = 0;
                    while ((c = stream.read(buff, 0, BUFFER_SIZE - 1)) != -1) value.append(buff, 0, c);
                    addExtensionAttribute(newAttributes, "src-value", value.toString());
                } catch (MalformedURLException e) {
                    throw new OXFException(e);
                } catch (IOException ioe) {
                    throw new OXFException(ioe);
                }
            }
        }
        context.getContentHandler().startElement(uri, localname, qname, newAttributes);
    }
