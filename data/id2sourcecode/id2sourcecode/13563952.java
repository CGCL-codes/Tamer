    @Override
    public boolean marshall(PackagePart part, OutputStream out) throws OpenXML4JException {
        if (!(out instanceof ZipOutputStream)) {
            throw new IllegalArgumentException("ZipOutputStream expected!");
        }
        ZipOutputStream zos = (ZipOutputStream) out;
        ZipEntry ctEntry = new ZipEntry(ZipHelper.getZipItemNameFromOPCName(part.getPartName().getURI().toString()));
        try {
            zos.putNextEntry(ctEntry);
            super.marshall(part, out);
            if (!StreamHelper.saveXmlInStream(xmlDoc, out)) {
                return false;
            }
            zos.closeEntry();
        } catch (IOException e) {
            throw new OpenXML4JException(e.getLocalizedMessage());
        }
        return true;
    }
