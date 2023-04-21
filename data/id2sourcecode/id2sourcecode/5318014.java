    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        if ((uri.equals(TagNames.PACKAGE_URI) && (localName.equals(TagNames.CONTENT)) && (qName.equals(TagNames.PACKAGE_CONTENT)))) {
            startedChecksumming = false;
            setProperty("http://xena/digest", convertToHex(digest.digest()));
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
        }
        if (startedChecksumming) {
            checksumHandler.endElement(uri, localName, qName);
            try {
                checksumOSW.flush();
                checksumBAOS.flush();
                digest.update(checksumBAOS.toByteArray());
                checksumBAOS.reset();
            } catch (IOException iex) {
                throw new SAXException("Problem updating checksum", iex);
            }
        }
    }
