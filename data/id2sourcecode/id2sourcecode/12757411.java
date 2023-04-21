    public Protocol(InputStream reader, OutputStream writer, byte packetnumber) {
        m_StreamLectura = reader;
        m_StreamEscritura = writer;
        m_NumeroPaquete = packetnumber;
        m_LeerDatos = true;
    }
