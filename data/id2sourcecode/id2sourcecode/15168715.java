        public boolean marshall(PackagePart part, OutputStream os) throws OpenXML4JException {
            if (!(os instanceof ZipOutputStream)) {
                logger.error("ZipOutputSTream expected!" + os.getClass().getName());
                throw new OpenXML4JException("ZipOutputSTream expected!");
            }
            ZipOutputStream out = (ZipOutputStream) os;
            ZipEntry ctEntry = new ZipEntry(part.getUri().getPath());
            try {
                out.putNextEntry(ctEntry);
                if (!Package.saveAsXmlInZip(content, part.getUri().getPath(), out)) {
                    return false;
                }
                if (logger.isDebugEnabled()) {
                    logger.debug("recording word doc relationship");
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
