    public static boolean marshallRelationshipPart(PackageRelationshipCollection rels, PackagePartName relPartName, ZipOutputStream zos) {
        Document xmlOutDoc = DocumentHelper.createDocument();
        Namespace dfNs = Namespace.get("", PackageNamespaces.RELATIONSHIPS);
        Element root = xmlOutDoc.addElement(new QName(PackageRelationship.RELATIONSHIPS_TAG_NAME, dfNs));
        URI sourcePartURI = PackagingURIHelper.getSourcePartUriFromRelationshipPartUri(relPartName.getURI());
        for (PackageRelationship rel : rels) {
            Element relElem = root.addElement(PackageRelationship.RELATIONSHIP_TAG_NAME);
            relElem.addAttribute(PackageRelationship.ID_ATTRIBUTE_NAME, rel.getId());
            relElem.addAttribute(PackageRelationship.TYPE_ATTRIBUTE_NAME, rel.getRelationshipType());
            String targetValue;
            URI uri = rel.getTargetURI();
            if (rel.getTargetMode() == TargetMode.EXTERNAL) {
                try {
                    targetValue = URLEncoder.encode(uri.toString(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    targetValue = uri.toString();
                }
                relElem.addAttribute(PackageRelationship.TARGET_MODE_ATTRIBUTE_NAME, "External");
            } else {
                targetValue = PackagingURIHelper.relativizeURI(sourcePartURI, rel.getTargetURI()).getPath();
            }
            relElem.addAttribute(PackageRelationship.TARGET_ATTRIBUTE_NAME, targetValue);
        }
        xmlOutDoc.normalize();
        ZipEntry ctEntry = new ZipEntry(ZipHelper.getZipURIFromOPCName(relPartName.getURI().toASCIIString()).getPath());
        try {
            zos.putNextEntry(ctEntry);
            if (!StreamHelper.saveXmlInStream(xmlOutDoc, zos)) {
                return false;
            }
            zos.closeEntry();
        } catch (IOException e) {
            logger.error("Cannot create zip entry " + relPartName, e);
            return false;
        }
        return true;
    }
