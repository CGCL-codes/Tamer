    public String imgGuardar_action() {
        ubicarRegistroSolicitado();
        String claveActual = usuariosDataProvider.getValue("Clave").toString();
        String strClaveActualPropuesta = txtClaveActual.getValue().toString();
        byte[] ca = strClaveActualPropuesta.getBytes();
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(ca);
            ca = m.digest(ca);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Usuarios_CambioDeClave.class.getName()).log(Level.SEVERE, null, ex);
        }
        strClaveActualPropuesta = new String(ca);
        if (!claveActual.equals(strClaveActualPropuesta)) {
            error("La clave actual suministrada no coincide con la clave del usuario");
            return null;
        }
        if (!txtNuevaClave1.getValue().toString().equals(txtNuevaClave2.getValue().toString())) {
            error("La nueva clave no coincide.");
            return null;
        }
        if (txtNuevaClave1.getValue().toString().isEmpty()) {
            error("La nueva clave es un campo requerido y está vacío...");
            return null;
        }
        usuariosDataProvider.setValue("Clave", txtNuevaClave1.getValue().toString());
        try {
            usuariosDataProvider.commitChanges();
        } catch (Exception ex) {
            error(PatronesDeExcepcion.adaptarMensaje(ex));
            return null;
        }
        getRequestBean1().setMensajeSiguientePagina("La clave fue cambiada satsifactoriamente.");
        return "Guardar";
    }
