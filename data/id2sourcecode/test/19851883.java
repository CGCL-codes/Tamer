    private OWLOntology _loadOntology(InputStream stream) throws OWLOntologyCreationException, IOException {
        byte[] tempBuffer = new byte[4096];
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        int read;
        while ((read = stream.read(tempBuffer)) != -1) {
            bos.write(tempBuffer, 0, read);
        }
        byte[] buffer = bos.toByteArray();
        String s = new String(buffer);
        OWLOntology ontology = _manager.loadOntology(new StringInputSource(s));
        return ontology;
    }
