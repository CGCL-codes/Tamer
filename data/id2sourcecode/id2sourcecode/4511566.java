    public String gravar() {
        String senha = this.infoBean.getPassword();
        if (!senha.equals(this.confirmaPassword)) {
            JsfUtil.addErrorMessage(null, ResourceBundle.getBundle("resources.mensagens").getString("authUtilizadorConfirmarPasswordRequiredMessage"));
            return null;
        }
        try {
            byte[] b = EncriptacaoUtil.digest(infoBean.getPassword().getBytes(), "md5");
            infoBean.setPassword(EncriptacaoUtil.byteArrayToHexString(b));
            Date dt = new Date();
            infoBean.setDataAlteracao((Date) dt.clone());
            if (this.isEstadoAdicionar()) {
                infoBean.setDataRegisto((Date) dt.clone());
                this.dao.gravar(this.getInfoBean());
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle("resources.mensagens").getString("geralRegistoCriado"));
                actualizaTela();
                return this.preparaAdicionar();
            } else if (this.isEstadoActualizar()) {
                this.dao.gravar(this.getInfoBean());
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle("resources.mensagens").getString("geralRegistoAlterado"));
                this.setEstadoCorrente(JsfUtil.ESTADO_FORMVAZIO);
                return null;
            }
            return null;
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("resources.mensagens").getString("geralErroDePersistencia"));
            return null;
        }
    }
