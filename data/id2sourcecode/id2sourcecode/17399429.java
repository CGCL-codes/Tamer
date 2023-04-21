    public void write(OutputStream os) throws IOException {
        ZipOutputStream zos = new ZipOutputStream(os);
        ZipEntry mimeEntry = new ZipEntry("mimetype");
        mimeEntry.setMethod(ZipEntry.STORED);
        mimeEntry.setCrc(0x2CAB616F);
        mimeEntry.setSize(mimeBytes.length);
        zos.putNextEntry(mimeEntry);
        zos.write(mimeBytes, 0, mimeBytes.length);
        zos.closeEntry();
        OutputFile containerWriter = new ContainerWriter();
        ZipEntry containerEntry = new ZipEntry("META-INF/container.xml");
        zos.putNextEntry(containerEntry);
        writeZipEntry(containerWriter, zos);
        zos.closeEntry();
        OPFWriter manifest = new OPFWriter(xhtmlResult);
        ZipEntry manifestEntry = new ZipEntry("OEBPS/book.opf");
        zos.putNextEntry(manifestEntry);
        writeZipEntry(manifest, zos);
        zos.closeEntry();
        OutputFile ncx = new NCXWriter(xhtmlResult, manifest.getUid());
        ZipEntry ncxEntry = new ZipEntry("OEBPS/book.ncx");
        zos.putNextEntry(ncxEntry);
        writeZipEntry(ncx, zos);
        zos.closeEntry();
        Iterator<OutputFile> iter = xhtmlResult.iterator();
        while (iter.hasNext()) {
            OutputFile file = iter.next();
            ZipEntry entry = new ZipEntry("OEBPS/" + file.getFileName());
            zos.putNextEntry(entry);
            writeZipEntry(file, zos);
            zos.closeEntry();
        }
        zos.close();
    }
