    @Override
    public Object getValor() {
        try {
            return this.getCabecera().getChannels();
        } catch (Exception ex) {
            Logger.getLogger(CampoTAG.class.getName()).log(Level.WARNING, java.util.ResourceBundle.getBundle("resources/textos").getString("**_No_se_ha_podido_obtener_el_campo_") + this.getClass().getName());
        }
        return "";
    }
