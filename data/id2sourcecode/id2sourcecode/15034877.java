    public MetaData parseMetaDataURL(URL url, String handlerName) {
        if (url == null) {
            String msg = LOCALISER.msg("044031");
            NucleusLogger.METADATA.error(msg);
            throw new NucleusException(msg);
        }
        InputStream in = null;
        try {
            in = url.openStream();
        } catch (Exception ignore) {
        }
        if (in == null) {
            try {
                in = new FileInputStream(StringUtils.getFileForFilename(url.getFile()));
            } catch (Exception ignore) {
            }
        }
        if (in == null) {
            NucleusLogger.METADATA.error(LOCALISER.msg("044032", url.toString()));
            throw new NucleusException(LOCALISER.msg("044032", url.toString()));
        }
        return parseMetaDataStream(in, url.toString(), handlerName);
    }
