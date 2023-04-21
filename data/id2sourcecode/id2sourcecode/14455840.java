    public void reEnviarAlCliente() {
        if (cliente == null) {
            log.info("Se nulizo el cliente!!!");
        }
        byte[] c = b;
        try {
            log.info("Loop parameters: " + offset + "/" + bytesread);
            outCliente.write(c, offset, bytesread);
            outCliente.flush();
        } catch (IOException e) {
            log.debug("->" + offset + "/" + bytesread + " Estado:" + conectado);
            e.printStackTrace();
        }
    }
