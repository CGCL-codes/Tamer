    @SuppressWarnings("unchecked")
    public final void serialize(OutputStream out) throws IOException {
        ZipOutputStream zip = new ZipOutputStream(out);
        for (String name : artifactMap.keySet()) {
            zip.putNextEntry(new ZipEntry(name));
            ArtifactSerializer serializer = getArtifactSerializer(name);
            if (serializer == null) {
                throw new IllegalStateException("Missing serializer for " + name);
            }
            serializer.serialize(artifactMap.get(name), zip);
            zip.closeEntry();
        }
        zip.finish();
        zip.flush();
    }
