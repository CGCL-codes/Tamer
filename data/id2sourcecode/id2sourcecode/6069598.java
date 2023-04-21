    public void copyModelfilesTo(File rdfschemaFile, File rdfModelFile) {
        try {
            URL schemaURL = this.getBundle().getEntry(RDFS_FILE);
            InputStream iStream = schemaURL.openStream();
            FileOutputStream fOut = new FileOutputStream(rdfschemaFile);
            byte bytes[] = new byte[512];
            int read = -1;
            while ((read = iStream.read(bytes, 0, 512)) != -1) {
                fOut.write(bytes, 0, read);
            }
        } catch (Exception e) {
            showException("Exception while Copying rdfs schema", e);
        }
        try {
            URL modelURL = this.getBundle().getEntry(RDF_FILE);
            InputStream iStream = modelURL.openStream();
            FileOutputStream fOut = new FileOutputStream(rdfModelFile);
            byte bytes[] = new byte[512];
            int read = -1;
            while ((read = iStream.read(bytes, 0, 512)) != -1) {
                fOut.write(bytes, 0, read);
            }
        } catch (Exception e) {
            showException("Exception while Copying rdf model file", e);
        }
    }
