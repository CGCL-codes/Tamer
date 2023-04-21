    private synchronized LSN escribir(byte[] datos) {
        try {
            LSN lsn = LSN.nuevoLSN(this.escritor.getChannel().position());
            this.escritor.write(datos);
            return lsn;
        } catch (IOException e) {
            throw new RuntimeException("Error when writing in log!", e);
        }
    }
