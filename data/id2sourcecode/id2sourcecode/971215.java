    public final boolean writeMetierDirect(final M metier, final File f, final CtuluLog analyzer, CrueConfigMetier props) {
        return readerWriter.writeXMLMetier(new CrueIOResu<M>(metier), f, analyzer, props);
    }
