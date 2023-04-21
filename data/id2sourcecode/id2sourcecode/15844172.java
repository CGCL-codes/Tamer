    protected VariableValue processShortAddressVal(String name, String comment, boolean readOnly, boolean infoOnly, boolean writeOnly, boolean opsOnly, int CV, String mask, String item, Element child) {
        VariableValue v;
        ShortAddrVariableValue v1 = new ShortAddrVariableValue(name, comment, "", readOnly, infoOnly, writeOnly, opsOnly, CV, mask, _cvModel.allCvVector(), _status, item);
        v = v1;
        @SuppressWarnings("unchecked") List<Element> l = child.getChildren("shortAddressChanges");
        for (int k = 0; k < l.size(); k++) {
            try {
                v1.setModifiedCV(l.get(k).getAttribute("cv").getIntValue());
            } catch (org.jdom.DataConversionException e1) {
                log.error("invalid cv attribute in short address element of decoder file");
            }
        }
        return v;
    }
