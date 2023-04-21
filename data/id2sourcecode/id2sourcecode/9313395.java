    public InputSource resolveEntity(String publicId, String systemId) throws SAXException {
        try {
            if (publicId != null) {
                String internalEntity = (String) publicIdEntities.get(publicId);
                if (internalEntity != null) {
                    return getLocalInputSource(publicId, systemId, internalEntity);
                }
            }
            if (systemId != null) {
                String internalEntity = (String) systemIdEntities.get(systemId);
                if (internalEntity != null) {
                    return getLocalInputSource(publicId, systemId, internalEntity);
                } else if (systemId.startsWith("file://")) {
                    String localPath = systemId.substring(7);
                    File file = new File(localPath);
                    if (file.exists()) {
                        if (NucleusLogger.METADATA.isDebugEnabled()) {
                            NucleusLogger.METADATA.debug(LOCALISER.msg("028001", publicId, systemId));
                        }
                        FileInputStream in = new FileInputStream(file);
                        return new InputSource(in);
                    }
                    return null;
                } else if (systemId.startsWith("file:")) {
                    return getLocalInputSource(publicId, systemId, systemId.substring(5));
                } else if (systemId.startsWith("http:")) {
                    try {
                        if (NucleusLogger.METADATA.isDebugEnabled()) {
                            NucleusLogger.METADATA.debug(LOCALISER.msg("028001", publicId, systemId));
                        }
                        URL url = new URL(systemId);
                        InputStream url_stream = url.openStream();
                        return new InputSource(url_stream);
                    } catch (Exception e) {
                        NucleusLogger.METADATA.error(e);
                    }
                }
            }
            NucleusLogger.METADATA.error(LOCALISER.msg("028002", publicId, systemId));
            return null;
        } catch (Exception e) {
            NucleusLogger.METADATA.error(LOCALISER.msg("028003", publicId, systemId), e);
            throw new SAXException(e.getMessage(), e);
        }
    }
