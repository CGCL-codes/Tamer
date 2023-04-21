    public static void comprobarAccesoExclusivo() throws Exception {
        if (cerrojo == null) {
            File archivoCerrojo = new File(nombreBDtemp + ".lock");
            cerrojo = new FileOutputStream(archivoCerrojo);
            if (cerrojo.getChannel().tryLock() == null) {
                cerrojo.close();
                throw new IOException();
            }
        }
    }
