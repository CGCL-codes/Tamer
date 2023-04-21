    public final boolean write(final CrueIOResu<CrueData> metier, final File f, final CtuluLog analyzer) {
        return readerWriter.writeXML(metier, f, analyzer);
    }
