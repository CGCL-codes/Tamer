    private static int getChannel(ServiceRecord sr, int def) {
        try {
            Enumeration protocolDescriptorListElems = (Enumeration) (sr.getAttributeValue(0x0004)).getValue();
            protocolDescriptorListElems.nextElement();
            Enumeration deRFCOMMElems = (Enumeration) ((DataElement) protocolDescriptorListElems.nextElement()).getValue();
            deRFCOMMElems.nextElement();
            return HelperStd.parseInt("" + ((DataElement) deRFCOMMElems.nextElement()).getLong(), 1);
        } catch (Exception e) {
            e.printStackTrace();
            return def;
        }
    }
