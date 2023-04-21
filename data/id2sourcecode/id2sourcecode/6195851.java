        public boolean marshall(PackagePart part, OutputStream os) throws OpenXML4JException {
            if (!(os instanceof ZipOutputStream)) {
                logger.error("unexpected class " + os.getClass().getName());
                throw new OpenXML4JException(" ZipOutputStream expected!");
            }
            ZipOutputStream out = (ZipOutputStream) os;
            addCreator();
            addTitle();
            addSubject();
            addModified();
            addCreated();
            addRevision();
            addLastModifiedBy();
            addDescription();
            addKeywords();
            ZipEntry ctEntry = new ZipEntry(corePropertiesZipEntry.getName());
            try {
                out.putNextEntry(ctEntry);
                if (!Package.saveAsXmlInZip(xmlDoc, corePropertiesZipEntry.getName(), out)) {
                    return false;
                }
                out.closeEntry();
            } catch (IOException e1) {
                logger.error(e1);
                return false;
            }
            return true;
        }
