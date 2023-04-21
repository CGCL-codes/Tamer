    public String editar() {
        String senha = this.infoBean.getPassword();
        if (!senha.equals(this.confirmaPassword)) {
            JsfUtil.addErrorMessage(null, ResourceBundle.getBundle("siac.com.idioma.mensagens_pt_PT").getString("authUtilizadorConfirmarPasswordRequiredMessage"));
            return null;
        }
        try {
            byte[] b = EncriptacaoUtil.digest(infoBean.getPassword().getBytes(), "md5");
            infoBean.setPassword(EncriptacaoUtil.byteArrayToHexString(b));
            Date dt = new Date();
            infoBean.setDataAlteracao((Date) dt.clone());
            this.dao.edit(infoBean);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("siac.com.idioma.mensagens_pt_PT").getString("geralRegistoAlterado"));
            this.setEstadoCorrente(JsfUtil.ESTADO_FORMVAZIO);
            return null;
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("siac.com.idioma.mensagens_pt_PT").getString("geralErroDePersistencia"));
            return null;
        }
    }
