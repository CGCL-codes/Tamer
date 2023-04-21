    protected VariableValue processIEnumVal(Element child, int row, String name, String comment, String cvName, boolean readOnly, boolean infoOnly, boolean writeOnly, boolean opsOnly, int cv, String mask, String item, String productID) throws NumberFormatException {
        VariableValue iv;
        @SuppressWarnings("unchecked") List<Element> l = child.getChildren("ienumChoice");
        IndexedEnumVariableValue v1 = new IndexedEnumVariableValue(row, name, comment, cvName, readOnly, infoOnly, writeOnly, opsOnly, cv, mask, _indxCvModel.allIndxCvVector(), _status, item);
        iv = v1;
        for (int x = 0; x < l.size(); x++) {
            Element ex = l.get(x);
            if (DecoderFile.isIncluded(ex, productID) == false) {
                l.remove(x);
                x--;
            }
        }
        v1.nItems(l.size());
        for (int k = 0; k < l.size(); k++) {
            Element enumChElement = l.get(k);
            Attribute valAttr = enumChElement.getAttribute("value");
            if (valAttr == null) {
                v1.addItem(LocaleSelector.getAttribute(enumChElement, "choice"));
            } else {
                v1.addItem(LocaleSelector.getAttribute(enumChElement, "choice"), Integer.parseInt(valAttr.getValue()));
            }
        }
        v1.lastItem();
        return iv;
    }
