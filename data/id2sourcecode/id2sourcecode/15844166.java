    protected VariableValue processEnumVal(Element child, String name, String comment, boolean readOnly, boolean infoOnly, boolean writeOnly, boolean opsOnly, int CV, String mask, String item) throws NumberFormatException {
        VariableValue v;
        @SuppressWarnings("unchecked") List<Element> l = child.getChildren("enumChoice");
        EnumVariableValue v1 = new EnumVariableValue(name, comment, "", readOnly, infoOnly, writeOnly, opsOnly, CV, mask, 0, l.size() - 1, _cvModel.allCvVector(), _status, item);
        v = v1;
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
        return v;
    }
