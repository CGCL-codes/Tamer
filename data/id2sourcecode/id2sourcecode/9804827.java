    private void serializeComponents(final WorkspaceComponentSerializer serializer, final ArchiveContents archive, final ZipOutputStream zipStream) throws IOException {
        for (WorkspaceComponent component : workspace.getComponentList()) {
            ArchiveContents.ArchivedComponent archiveComp = archive.addComponent(component);
            ZipEntry entry = new ZipEntry(archiveComp.getUri());
            zipStream.putNextEntry(entry);
            serializer.serializeComponent(component);
            GuiComponent<?> desktopComponent = SimbrainDesktop.getDesktop(workspace).getDesktopComponent(component);
            if (desktopComponent != null) {
                ArchiveContents.ArchivedComponent.ArchivedDesktopComponent dc = archiveComp.addDesktopComponent(desktopComponent);
                entry = new ZipEntry(dc.getUri());
                zipStream.putNextEntry(entry);
                desktopComponent.save(zipStream);
            }
        }
    }
