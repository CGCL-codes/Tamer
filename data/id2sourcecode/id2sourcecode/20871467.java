    @Override
    public void selectAttributes(NSArray attributesToFetch, EOFetchSpecification fetchSpecification, boolean shouldLock, EOEntity entity) {
        if (entity == null) {
            throw new IllegalArgumentException("null entity.");
        }
        if (attributesToFetch == null) {
            throw new IllegalArgumentException("null attributes.");
        }
        setAttributesToFetch(attributesToFetch);
        try {
            _fetchIndex = 0;
            NSMutableDictionary<String, Object> attributesFromQualifier = new NSMutableDictionary<String, Object>();
            ERXMutableURL url = new ERXMutableURL(urlForQualifier(entity, fetchSpecification.qualifier(), attributesFromQualifier));
            InputStream urlStream = new BufferedInputStream(url.toURL().openStream());
            Document document;
            try {
                document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(urlStream);
            } finally {
                urlStream.close();
            }
            document.normalize();
            _fetchedRows = new NSMutableArray<NSMutableDictionary<String, Object>>();
            NodeList rowElements = document.getElementsByTagName(singularName(entity));
            for (int rowNum = 0; rowNum < rowElements.getLength(); rowNum++) {
                NSMutableDictionary<String, Object> row = new NSMutableDictionary<String, Object>();
                Element rowElement = (Element) rowElements.item(rowNum);
                for (int attributeNum = 0; attributeNum < attributesToFetch.count(); attributeNum++) {
                    EOAttribute attribute = (EOAttribute) attributesToFetch.objectAtIndex(attributeNum);
                    String columnName = attribute.columnName();
                    NodeList attributeElements = rowElement.getElementsByTagName(columnName);
                    Object value;
                    if (attributeElements.getLength() == 0) {
                        if (rowElement.hasAttribute(columnName)) {
                            Attr columnAttribute = rowElement.getAttributeNode(columnName);
                            value = convertValue(columnAttribute.getValue(), attribute);
                        } else {
                            value = null;
                        }
                    } else if (attributeElements.getLength() > 1) {
                        throw new EOGeneralAdaptorException("There was more than one column named '" + columnName + "'.");
                    } else {
                        Element attributeElement = (Element) attributeElements.item(0);
                        if ("true".equals(attributeElement.getAttribute("nil"))) {
                            value = null;
                        } else {
                            String strValue = textValue(attributeElement);
                            if (attributeElement.hasAttribute("type")) {
                                String type = attributeElement.getAttribute("type");
                                if ("bigit".equals(type)) {
                                    value = new BigDecimal(strValue);
                                } else if ("boolean".equals(type)) {
                                    value = Boolean.parseBoolean(strValue);
                                } else if ("datetime".equals(type)) {
                                    value = ERRESTAdaptorChannel.restDateFormat.parseObject(strValue);
                                } else if ("double".equals(type)) {
                                    value = Double.parseDouble(strValue);
                                } else if ("float".equals(type)) {
                                    value = Float.parseFloat(strValue);
                                } else if ("integer".equals(type)) {
                                    value = Integer.parseInt(strValue);
                                } else if ("short".equals(type)) {
                                    value = Short.parseShort(strValue);
                                } else {
                                    throw new IllegalArgumentException("Unknown type '" + type + "'.");
                                }
                            } else {
                                value = convertValue(strValue, attribute);
                            }
                        }
                    }
                    if (value != null) {
                        row.setObjectForKey(value, attribute.name());
                    }
                }
                if (attributesFromQualifier.count() > 0) {
                    for (String qualifierAttributeName : attributesFromQualifier.allKeys()) {
                        if (!row.containsKey(qualifierAttributeName)) {
                            EOAttribute qualifierAttribute = entity.attributeNamed(qualifierAttributeName);
                            if (qualifierAttribute != null && attributesToFetch.containsObject(qualifierAttribute)) {
                                row.setObjectForKey(attributesFromQualifier.objectForKey(qualifierAttributeName), qualifierAttributeName);
                            }
                        }
                    }
                }
                EOQualifier qualifier = fetchSpecification.qualifier();
                if (qualifier == null || qualifier.evaluateWithObject(row)) {
                    _fetchedRows.addObject(row);
                } else {
                    System.out.println("ERRESTAdaptorChannel.selectAttributes: skipping " + row + " (" + qualifier + ")");
                }
            }
        } catch (EOGeneralAdaptorException e) {
            throw e;
        } catch (Throwable e) {
            e.printStackTrace();
            throw new EOGeneralAdaptorException("Failed to fetch '" + entity.name() + "' with fetch specification '" + fetchSpecification + "': " + e.getMessage());
        }
    }
