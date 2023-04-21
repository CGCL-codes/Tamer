    public void enviarAlCliente() {
        if (cliente == null) {
            log.info("Se nulizo el cliente!!!");
        }
        try {
            log.info("Loop parameters: " + offset + "/" + bytesread);
            outCliente.write(b, offset, bytesread);
            outCliente.flush();
        } catch (IOException e) {
            log.debug("->" + offset + "/" + bytesread + " Estado:" + conectado);
            System.err.println("Cliente desaparecido: Socket error");
        }
    }
