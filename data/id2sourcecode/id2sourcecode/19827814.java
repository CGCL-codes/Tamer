    public byte[] berechnen(byte[] text) {
        Fehler.objekt.wenn_Null(text);
        byte[] hash = md.digest(text);
        md.reset();
        return hash;
    }
