    public Propiedades(String nombreCompletoArchivo) {
        try {
            url = getClass().getClassLoader().getResource(nombreCompletoArchivo);
            load(url.openStream());
        } catch (Exception e) {
            logger.error(Excepcion.getStackTrace(e));
        }
    }
