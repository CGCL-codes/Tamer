    public void validateInput(Map propertyMap) throws Exception {
        String requiredPropStr = " is not specified in configuration file. This is a required property. Please add one and retry";
        String authority = (String) propertyMap.get("authority");
        if (authority == null) {
            throw new IllegalArgumentException("'authority'" + requiredPropStr);
        }
        String conceptType = (String) propertyMap.get("conceptType");
        if (conceptType == null) {
            throw new IllegalArgumentException("'conceptType'" + requiredPropStr);
        }
        String type = "";
        if (conceptType.equals("ontylog")) {
            type = "O";
        } else if (conceptType.equals("thesaurus")) {
            type = "T";
        } else if (conceptType.equals("connection")) {
            type = "C";
        } else {
            throw new IllegalArgumentException(conceptType + " is not correct");
        }
        String versionStr = (String) propertyMap.get("version");
        if (versionStr == null) {
            throw new IllegalArgumentException("'version'" + requiredPropStr);
        }
        String versionNumStr = (String) propertyMap.get(DTSBaseTable.VERSION_NUM_STR);
        if (versionNumStr == null) {
            throw new IllegalArgumentException("'" + DTSBaseTable.VERSION_NUM_STR + "'" + requiredPropStr);
        } else {
            try {
                Integer.parseInt(versionNumStr);
            } catch (Exception ex) {
                throw new IllegalArgumentException("Property '" + DTSBaseTable.VERSION_NUM_STR + "' expects a numeric value");
            }
        }
        String accessMode = (String) propertyMap.get("accessMode");
        if (accessMode == null) {
            throw new IllegalArgumentException("'accessMode'" + requiredPropStr);
        }
        String mode = "";
        if (accessMode.equals("readonly")) {
            mode = "F";
        } else if (accessMode.equals("readwrite")) {
            mode = "T";
        } else {
            throw new IllegalArgumentException(accessMode + " is not correct");
        }
        String namespaceType = (String) propertyMap.get("namespaceType");
        if (namespaceType != null) {
            if (namespaceType.equals("local")) {
                GidGenerator.localNamespaceFlag = true;
                DTSBaseTable.setLocalNamespaceFlag(true);
            } else if (namespaceType.equals("nonlocal")) {
                GidGenerator.localNamespaceFlag = false;
                DTSBaseTable.setLocalNamespaceFlag(false);
            } else throw new IllegalArgumentException("Incorrect value [" + namespaceType + "] provided for 'namespaceType'. Expecting 'local' or 'nonlocal'");
        }
        String semanticType = (String) propertyMap.get("hasSemanticType");
        if (semanticType != null) {
            if (semanticType.equals("true")) {
                DTSBaseTable.setHasSemanticTypes("T");
            } else if (semanticType.equals("false")) {
                DTSBaseTable.setHasSemanticTypes("F");
            } else throw new IllegalArgumentException("Incorrect value [" + semanticType + "] provided for 'semanticType'. Expecting 'true' or 'false'");
        }
        String namespaceId = (String) propertyMap.get("namespaceId");
        if (namespaceId != null) {
            DTSBaseTable.setProperty("namespaceId", namespaceId);
        }
        String numSourceRecords = (String) propertyMap.get("numSourceRecords");
        if (numSourceRecords != null) {
            try {
                int value = Integer.parseInt(numSourceRecords);
                DTSBaseTable.setNumSourceRecords(value);
            } catch (Exception ex) {
                throw new IllegalArgumentException("Property 'numSourceRecords' expects a numeric value");
            }
        }
        String numTargetRecords = (String) propertyMap.get("numTargetRecords");
        if (numTargetRecords != null) {
            try {
                int value = Integer.parseInt(numTargetRecords);
                DTSBaseTable.setNumTargetRecords(value);
            } catch (Exception ex) {
                throw new IllegalArgumentException("Property 'numTargetRecords' expects a numeric value");
            }
        }
        DTSBaseTable.setAuthority(authority);
        DTSBaseTable.setConceptType(type);
        DTSBaseTable.setVersion(versionStr);
        DTSBaseTable.setAccessMode(mode);
        DTSBaseTable.setNamespaceType(namespaceType);
        DTSBaseTable.setProperty(DTSBaseTable.VERSION_NUM_STR, versionNumStr);
        Connection sourceConn = (Connection) propertyMap.get(DTSBaseTable.getSourceConKey());
        Connection targetConn = (Connection) propertyMap.get(DTSBaseTable.getTargetConKey());
        TableDTS_NAMESPACE.checkNameSpace(sourceConn, targetConn);
    }
