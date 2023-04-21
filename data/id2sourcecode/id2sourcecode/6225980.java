    @Override
    public void endDocument() throws org.xml.sax.SAXException {
        ContentHandler th = getContentHandler();
        th.endElement(NaaTagNames.WRAPPER_URI, NaaTagNames.AIP, NaaTagNames.WRAPPER_AIP);
        if (digest != null) {
            checksumHandler.endDocument();
            AttributesImpl atts = new AttributesImpl();
            th.startElement(NaaTagNames.WRAPPER_URI, NaaTagNames.META, NaaTagNames.WRAPPER_META, atts);
            atts.addAttribute(NaaTagNames.WRAPPER_URI, "description", NaaTagNames.WRAPPER_PREFIX + ":description", "CDATA", description);
            atts.addAttribute(NaaTagNames.WRAPPER_URI, "algorithm", NaaTagNames.WRAPPER_PREFIX + ":algorithm", "CDATA", DEFAULT_CHECKSUM_ALGORITHM);
            th.startElement(NaaTagNames.WRAPPER_URI, NaaTagNames.SIGNATURE, NaaTagNames.WRAPPER_SIGNATURE, atts);
            char[] signatureVal = convertToHex(digest.digest()).toCharArray();
            th.characters(signatureVal, 0, signatureVal.length);
            th.endElement(NaaTagNames.WRAPPER_URI, NaaTagNames.SIGNATURE, NaaTagNames.WRAPPER_SIGNATURE);
            th.endElement(NaaTagNames.WRAPPER_URI, NaaTagNames.META, NaaTagNames.WRAPPER_META);
        }
        th.endElement(NaaTagNames.WRAPPER_URI, NaaTagNames.SIGNED_AIP, NaaTagNames.WRAPPER_SIGNED_AIP);
        try {
            if (checksumBAOS != null) {
                checksumBAOS.close();
            }
            if (checksumOSW != null) {
                checksumOSW.close();
            }
        } catch (IOException e) {
            throw new SAXException("Could not close checksum streams", e);
        }
        super.endDocument();
    }
