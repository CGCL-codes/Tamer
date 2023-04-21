    private oOID getObjectRef(Node iObjNode, oPersistenceManager iManager, boolean iVerbose, boolean iEmbedded) throws ToolException {
        oOID destOID = null;
        String sourceOID = getSourceOID(iObjNode);
        if (sourceOID == null) sourceOID = "[no oid]"; else {
            destOID = (oOID) objectsImported.get(sourceOID);
            if (destOID != null) {
                if (iVerbose) writeObjectMessage("Warning: " + sourceOID + " already imported as " + destOID);
                ++warnCount;
                return destOID;
            }
        }
        oDynaObject obj = importNode(iObjNode, iManager, iVerbose, iEmbedded);
        if (obj != null) destOID = obj.getOid();
        return destOID;
    }
