    public String adicionar() {
        String senha = this.infoBean.getPassword();
        if (!senha.equals(this.confirmaPassword)) {
            JsfUtil.addErrorMessage(null, ResourceBundle.getBundle("siac.com.idioma.mensagens_pt_PT").getString("authUtilizadorConfirmarPasswordRequiredMessage"));
            return null;
        }
        try {
            byte[] b = EncriptacaoUtil.digest(infoBean.getPassword().getBytes(), "md5");
            infoBean.setPassword(EncriptacaoUtil.byteArrayToHexString(b));
            Date dt = new Date();
            infoBean.setDataRegisto((Date) dt.clone());
            infoBean.setDataAlteracao((Date) dt.clone());
            this.dao.create(infoBean);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("siac.com.idioma.mensagens_pt_PT").getString("geralRegistoCriado"));
            return this.preparaAdicionar();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("siac.com.idioma.mensagens_pt_PT").getString("geralErroDePersistencia"));
            return null;
        }
    }
