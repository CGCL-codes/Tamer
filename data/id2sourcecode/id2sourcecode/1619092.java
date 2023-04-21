    private static SnapshotAttribute loadCurrentAttribute(SnapshotAttributes snapshotAttributes, Node currentAttributeNode) throws Exception {
        Hashtable attributeProperties = XMLUtils.loadAttributes(currentAttributeNode);
        String id_s = (String) attributeProperties.get(SnapshotAttribute.ID_PROPERTY_XML_TAG);
        String dataType_s = (String) attributeProperties.get(SnapshotAttribute.DATA_TYPE_PROPERTY_XML_TAG);
        String dataFormat_s = (String) attributeProperties.get(SnapshotAttribute.DATA_FORMAT_PROPERTY_XML_TAG);
        String writable_s = (String) attributeProperties.get(SnapshotAttribute.WRITABLE_PROPERTY_XML_TAG);
        String completeName = (String) attributeProperties.get(SnapshotAttribute.COMPLETE_NAME_PROPERTY_XML_TAG);
        SnapshotAttribute currentAttribute = new SnapshotAttribute(completeName.trim(), snapshotAttributes);
        int id = -1;
        int dataType = -1;
        int dataFormat = -1;
        int writable = -1;
        try {
            id = Integer.parseInt(id_s);
        } catch (Exception e) {
        }
        try {
            dataType = Integer.parseInt(dataType_s);
        } catch (Exception e) {
        }
        try {
            dataFormat = Integer.parseInt(dataFormat_s);
        } catch (Exception e) {
        }
        try {
            writable = Integer.parseInt(writable_s);
        } catch (Exception e) {
        }
        currentAttribute.setAttribute_id(id);
        currentAttribute.setData_format(dataFormat);
        currentAttribute.setData_type(dataType);
        currentAttribute.setPermit(writable);
        if (currentAttributeNode.hasChildNodes()) {
            NodeList valueNodes = currentAttributeNode.getChildNodes();
            SnapshotAttributeReadValue readValue = null;
            SnapshotAttributeWriteValue writeValue = null;
            for (int i = 0; i < valueNodes.getLength(); i++) {
                Node currentValueNode = valueNodes.item(i);
                if (XMLUtils.isAFakeNode(currentValueNode)) {
                    continue;
                }
                String currentValueType = currentValueNode.getNodeName().trim();
                if (currentValueType.equals(SnapshotAttributeReadValue.XML_TAG)) {
                    readValue = loadReadValue(dataFormat, dataType, currentValueNode);
                    currentAttribute.setReadValue(readValue);
                } else if (currentValueType.equals(SnapshotAttributeWriteValue.XML_TAG)) {
                    writeValue = loadWriteValue(dataFormat, dataType, currentValueNode);
                    currentAttribute.setWriteValue(writeValue);
                } else {
                    throw new Exception();
                }
            }
            SnapshotAttributeDeltaValue deltaValue = SnapshotAttributeDeltaValue.getInstance(writeValue, readValue);
            currentAttribute.setDeltaValue(deltaValue);
            currentAttribute.updateDisplayFormat();
        }
        return currentAttribute;
    }
