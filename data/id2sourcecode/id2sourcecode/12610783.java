    public void leeDesde(int posicion) throws CoverlessException {
        offset = posicion;
        int chunk = BUFFER;
        while (bytesread >= 0) {
            if ((longitudArchivo - offset) < BUFFER) {
                chunk = longitudArchivo - offset;
            }
            try {
                bytesread = archivoInputStream.read(b, offset, chunk);
                ver("leeDesde(int posicion): Leyendo y guardando en B en posici�n = " + offset);
            } catch (IOException e) {
                error("leeDesde(int posicion): IOException mientras leia " + offset + " con chunk = " + chunk);
                throw new CoverlessException("leeDesde(int posicion): ERROR #RC7--> Problemas de cobertura" + "\n longitud de archivo le�da = " + longitudArchivo + "\n Offset = " + offset);
            }
            if (bytesread == -1) {
                ver("leeDesde(int posicion): Archivo leido");
                break;
            }
            if (bytesread == 0) {
                ver("leeDesde(int posicion): Archivo leido = 0");
                break;
            }
            try {
                outCliente.write(b, offset, bytesread);
                outCliente.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            escribirArchivo(b, offset, bytesread);
            offset += bytesread;
        }
        if (offset != longitudArchivo) {
            throw new CoverlessException("leeDesde(int posicion): ERROR #RC7--> Problemas de cobertura" + "\n longitud de archivo le�da = " + longitudArchivo + "\n Offset = " + offset);
        }
        ver("--> TRACE: leeDesde(int posicion): lectura terminada. Se leyeron " + offset);
        try {
            fout.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
