    private void writeKml(final String filename, final KmlType kml) {
        try {
            ZipEntry ze = new ZipEntry(filename);
            ze.setMethod(ZipEntry.DEFLATED);
            this.zipOut.putNextEntry(ze);
            try {
                marshaller.marshal(kmlObjectFactory.createKml(kml), out);
            } catch (JAXBException e) {
                e.printStackTrace();
            }
            this.out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
