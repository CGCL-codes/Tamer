    public boolean write(final CrueIOResu<CrueData> metier, final OutputStream out, final CtuluLog analyser) {
        return readerWriter.writeXML(metier, out, analyser);
    }
