    public boolean marshall(PackagePart part, OutputStream os) throws OpenXML4JException {
        if (!(os instanceof ZipOutputStream)) {
            logger.error("unexpected class " + os.getClass().getName());
            throw new OpenXML4JException(" ZipOutputStream expected!");
        }
        final String fileName = part.getUri().getPath();
        if (logger.isDebugEnabled()) {
            logger.debug("saving footer/header xml =" + fileName);
        }
        ZipOutputStream out = (ZipOutputStream) os;
        ZipEntry ctEntry = new ZipEntry(fileName);
        try {
            out.putNextEntry(ctEntry);
            if (!Package.saveAsXmlInZip(openXmlSource, fileName, out)) {
                return false;
            }
            if (logger.isDebugEnabled()) {
                logger.debug("recording  relationship of " + part.getUri());
            }
            if (relationships != null) {
                ZipPartMarshaller partMarshaller = new ZipPartMarshaller();
                PackageRelationshipCollection packageRelationships = relationships.buildPackageRelationships(part);
                partMarshaller.marshallRelationshipPart(packageRelationships, PackageURIHelper.getRelationshipPartUri(part.getUri()), out);
            }
            if (legacyRelations != null) {
                if (logger.isDebugEnabled()) {
                    logger.debug("saving header/footer legacy relationship");
                }
                ZipPartMarshaller partMarshaller = new ZipPartMarshaller();
                partMarshaller.marshallRelationshipPart(legacyRelations, PackageURIHelper.getRelationshipPartUri(part.getUri()), out);
            }
            out.closeEntry();
        } catch (IOException e1) {
            logger.error("IO problem with " + part.getUri(), e1);
            return false;
        }
        return true;
    }
