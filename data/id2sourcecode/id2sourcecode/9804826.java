    public void serialize(final OutputStream output) throws IOException {
        ZipOutputStream zipStream = new ZipOutputStream(output);
        WorkspaceComponentSerializer serializer = new WorkspaceComponentSerializer(zipStream);
        ArchiveContents archive = new ArchiveContents(workspace, serializer);
        workspace.preSerializationInit();
        serializeComponents(serializer, archive, zipStream);
        for (Coupling<?> coupling : workspace.getCouplingManager().getCouplings()) {
            archive.addCoupling(coupling);
        }
        ZipEntry entry = new ZipEntry("contents.xml");
        zipStream.putNextEntry(entry);
        archive.toXml(zipStream);
        zipStream.finish();
    }
