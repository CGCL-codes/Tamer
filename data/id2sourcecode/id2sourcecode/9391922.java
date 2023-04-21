    public File downloadSpecificTag(OWLOntology ontology, Long versionNumber) throws IOException {
        File localFile = File.createTempFile(TagWriter.TAGPREFIX + versionNumber, TagWriter.TAGEXTENSION, new File(TEMPDIR + OntologyFileManager.shortenURI(ontology.getURI().toASCIIString())));
        OutputStream out = null;
        URLConnection conn;
        GZIPInputStream zipin = null;
        try {
            URL url = new URL(server.getServerBase() + "/" + OntologyFileManager.shortenURI(ontology.getURI().toASCIIString()) + "/" + OntologyFileManager.TAGSFOLDER + "/" + TagWriter.TAGPREFIX + versionNumber + TagWriter.TAGEXTENSION);
            out = new BufferedOutputStream(new FileOutputStream(localFile));
            conn = url.openConnection();
            zipin = new GZIPInputStream(conn.getInputStream());
            byte[] buffer = new byte[1024];
            int numRead;
            long numWritten = 0;
            while ((numRead = zipin.read(buffer)) != -1) {
                out.write(buffer, 0, numRead);
                numWritten += numRead;
            }
            System.out.println(localFile.getName() + "\t" + numWritten);
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            try {
                if (zipin != null) {
                    zipin.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException ioe) {
                System.out.println(ioe.toString());
            }
        }
        return localFile;
    }
