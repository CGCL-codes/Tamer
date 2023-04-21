    public boolean marshall(PackagePart part, OutputStream os) throws OpenXML4JException {
        if (!(os instanceof ZipOutputStream)) {
            logger.error("unexpected class " + os.getClass().getName());
            throw new OpenXML4JException(" ZipOutputStream expected!");
        }
        if (logger.isDebugEnabled()) {
            logger.debug("saving CHANGED xml file");
        }
        ZipOutputStream out = (ZipOutputStream) os;
        ZipEntry ctEntry = new ZipEntry(part.getUri().getPath());
        try {
            out.putNextEntry(ctEntry);
            if (!Package.saveAsXmlInZip(xmlContent, part.getUri().getPath(), out)) {
                return false;
            }
            if (logger.isDebugEnabled()) {
                logger.debug("recording style relationship (should be none)");
            }
            if (part.hasRelationships()) {
                ZipPartMarshaller partMarshaller = new ZipPartMarshaller();
                partMarshaller.marshallRelationshipPart(part.getRelationships(), PackageURIHelper.getRelationshipPartUri(part.getUri()), out);
            }
            out.closeEntry();
        } catch (IOException e1) {
            logger.error("IO problem with " + part.getUri(), e1);
            return false;
        }
        return true;
    }
