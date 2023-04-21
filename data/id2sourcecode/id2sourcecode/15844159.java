    protected VariableValue createIndexedVariableFromElement(Element e, int row, String name, String comment, String cvName, boolean readOnly, boolean infoOnly, boolean writeOnly, boolean opsOnly, int cv, String mask, String item, String productID) throws NumberFormatException {
        VariableValue iv = null;
        Element child;
        if ((child = e.getChild("indexedVal")) != null) {
            iv = processIndexedVal(child, row, name, comment, cvName, readOnly, infoOnly, writeOnly, opsOnly, cv, mask, item);
        } else if ((child = e.getChild("ienumVal")) != null) {
            iv = processIEnumVal(child, row, name, comment, cvName, readOnly, infoOnly, writeOnly, opsOnly, cv, mask, item, productID);
        } else if ((child = e.getChild("indexedPairVal")) != null) {
            iv = processIndexedPairVal(child, row, readOnly, infoOnly, writeOnly, name, comment, cvName, opsOnly, cv, mask, item);
        }
        return iv;
    }
