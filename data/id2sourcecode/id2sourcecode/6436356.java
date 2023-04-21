        public boolean marshall(PackagePart part, OutputStream os) throws OpenXML4JException {
            if (!(os instanceof ZipOutputStream)) {
                logger.error("ZipOutputSTream expected!" + os.getClass().getName());
                throw new OpenXML4JException("ZipOutputSTream expected!");
            }
            ZipOutputStream out = (ZipOutputStream) os;
            ZipEntry ctEntry = new ZipEntry(part.getPartName().getURI().getPath());
            try {
                out.putNextEntry(ctEntry);
                if (!StreamHelper.saveXmlInStream(content, out)) {
                    return false;
                }
                logger.debug("recording word doc relationship");
                if (part.hasRelationships()) {
                    ZipPartMarshaller.marshallRelationshipPart(part.getRelationships(), PackagingURIHelper.getRelationshipPartName(part.getPartName()), out);
                }
                out.closeEntry();
            } catch (IOException e1) {
                logger.error("IO problem with " + part.getPartName(), e1);
                return false;
            }
            return true;
        }
