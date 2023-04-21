    private void writeKml(final String filename, final KML kml) {
        try {
            ZipEntry ze = new ZipEntry(filename);
            ze.setMethod(ZipEntry.DEFLATED);
            this.zipOut.putNextEntry(ze);
            this.writeXMLDeclaration(this.out);
            int offset = 0;
            String offsetString = "  ";
            kml.writeKML(this.out, this.xmlNS, offset, offsetString, this.xmlNS);
            this.out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
