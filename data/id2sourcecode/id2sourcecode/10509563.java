    public void write(Object o, OutputStream out) throws IOException {
        root = (RootDrawComponent) o;
        root.getBounds();
        manifestWriter = createManifestWriter();
        XStream xstream = new XStream(new XppDriver());
        initWriteSubTasks();
        ZipOutputStream zipStream = new ZipOutputStream(out);
        zipStream.setLevel(9);
        ZipEntry metaInfEntry = new ZipEntry(META_INF_DIR);
        metaInfEntry.setMethod(ZipEntry.STORED);
        metaInfEntry.setSize(0);
        metaInfEntry.setCrc(0);
        zipStream.putNextEntry(metaInfEntry);
        zipStream.closeEntry();
        writeManifestPropertiesZipEntry(zipStream);
        ZipEntry manifestEntry = new ZipEntry(META_INF_DIR + MANIFEST_NAME);
        zipStream.putNextEntry(manifestEntry);
        OutputStreamWriter manifestOut = new OutputStreamWriter(new BufferedOutputStream(zipStream), CHARSET_NAME);
        writeManifest(manifestOut, root);
        manifestOut.flush();
        zipStream.closeEntry();
        ZipEntry contentEntry = new ZipEntry(ManifestWriter.DEFAULT_CONTENT_NAME);
        zipStream.putNextEntry(contentEntry);
        OutputStreamWriter writer = new OutputStreamWriter(new BufferedOutputStream(zipStream), CHARSET_NAME);
        registerConverter(xstream);
        processSubTask(SUBTASK_COMPRESS);
        xstream.toXML(o, writer);
        processSubTask(SUBTASK_WRITING);
        zipStream.closeEntry();
        if (manifestWriter.containsImages(root)) {
            ZipEntry imagesEntry = new ZipEntry(IMAGES_DIR);
            imagesEntry.setMethod(ZipEntry.STORED);
            imagesEntry.setSize(0);
            imagesEntry.setCrc(0);
            zipStream.putNextEntry(imagesEntry);
            zipStream.closeEntry();
            writeImages(root, zipStream);
        }
        zipStream.finish();
        zipStream.close();
        manifestOut.close();
        writer.close();
    }
