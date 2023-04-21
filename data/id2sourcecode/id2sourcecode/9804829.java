    @SuppressWarnings("unchecked")
    public void deserialize(final InputStream stream, final Collection<? extends String> exclude) throws IOException {
        Map<String, byte[]> entries = new HashMap<String, byte[]>();
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        byte[] buffer = new byte[BUFFER_SIZE];
        for (int read; (read = stream.read(buffer)) >= 0; ) {
            bytes.write(buffer, 0, read);
        }
        ZipInputStream zip = new ZipInputStream(new ByteArrayInputStream(bytes.toByteArray()));
        ArchiveContents contents = null;
        WorkspaceComponentDeserializer componentDeserializer = new WorkspaceComponentDeserializer();
        ZipEntry entry = zip.getNextEntry();
        for (ZipEntry next; entry != null; entry = next) {
            next = zip.getNextEntry();
            entries.put(entry.getName(), new byte[(int) entry.getSize()]);
        }
        zip = new ZipInputStream(new ByteArrayInputStream(bytes.toByteArray()));
        while ((entry = zip.getNextEntry()) != null) {
            byte[] data = entries.get(entry.getName());
            read(zip, data);
        }
        contents = (ArchiveContents) ArchiveContents.xstream().fromXML(new ByteArrayInputStream(entries.get("contents.xml")));
        if (contents.getWorkspaceParameters() != null) {
            workspace.setUpdateDelay(contents.getWorkspaceParameters().getUpdateDelay());
        }
        if (contents.getArchivedComponents() != null) {
            for (ArchiveContents.ArchivedComponent archivedComponent : contents.getArchivedComponents()) {
                if (exclude.contains(archivedComponent.getUri())) {
                    continue;
                }
                WorkspaceComponent wc = componentDeserializer.deserializeWorkspaceComponent(archivedComponent, new ByteArrayInputStream(entries.get(archivedComponent.getUri())));
                workspace.addWorkspaceComponent(wc);
                if (archivedComponent.getDesktopComponent() != null) {
                    Rectangle bounds = (Rectangle) new XStream(new DomDriver()).fromXML(new ByteArrayInputStream(entries.get(archivedComponent.getDesktopComponent().getUri())));
                    GuiComponent desktopComponent = desktop.getDesktopComponent(wc);
                    desktopComponent.getParentFrame().setBounds(bounds);
                }
            }
        }
        if (contents.getArchivedCouplings() != null) {
            for (ArchiveContents.ArchivedCoupling couplingRef : contents.getArchivedCouplings()) {
                if (exclude.contains(couplingRef.getArchivedProducer().getParentRef()) || exclude.contains(couplingRef.getArchivedProducer().getParentRef())) {
                    continue;
                }
                WorkspaceComponent sourceComponent = componentDeserializer.getComponent(couplingRef.getArchivedProducer().getParentRef());
                WorkspaceComponent targetComponent = componentDeserializer.getComponent(couplingRef.getArchivedConsumer().getParentRef());
                Producer<?> producer = (Producer<?>) sourceComponent.getAttributeManager().createProducer(sourceComponent.getObjectFromKey(couplingRef.getArchivedProducer().getBaseObjectKey()), couplingRef.getArchivedProducer().getMethodBaseName(), couplingRef.getArchivedProducer().getDataType(), couplingRef.getArchivedProducer().getArgumentDataTypes(), couplingRef.getArchivedProducer().getArgumentValues(), couplingRef.getArchivedProducer().getDescription());
                Class[] argDataTypes;
                if (couplingRef.getArchivedConsumer().getArgumentDataTypes() == null) {
                    System.out.println("using it...");
                    argDataTypes = new Class[] { couplingRef.getArchivedConsumer().getDataType() };
                } else {
                    argDataTypes = couplingRef.getArchivedConsumer().getArgumentDataTypes();
                }
                Consumer<?> consumer = (Consumer<?>) targetComponent.getAttributeManager().createConsumer(targetComponent.getObjectFromKey(couplingRef.getArchivedConsumer().getBaseObjectKey()), couplingRef.getArchivedConsumer().getMethodBaseName(), argDataTypes, couplingRef.getArchivedConsumer().getArgumentValues(), couplingRef.getArchivedConsumer().getDescription());
                workspace.addCoupling(new Coupling(producer, consumer));
            }
        }
    }
