    public String adminLogin() {
        char[] HEXADECIMAL = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
        try {
            if (this.user == null) {
                getServletRequest().setAttribute("admin", "1");
                return SUCCESS;
            } else {
                MessageDigest md = MessageDigest.getInstance("MD5");
                byte[] bytes = md.digest(this.password.getBytes());
                StringBuilder sb = new StringBuilder(2 * bytes.length);
                for (int i = 0; i < bytes.length; i++) {
                    int low = (int) (bytes[i] & 0x0f);
                    int high = (int) ((bytes[i] & 0xf0) >> 4);
                    sb.append(HEXADECIMAL[high]);
                    sb.append(HEXADECIMAL[low]);
                }
                this.password = sb.toString();
                IUsuarioDto usuarioModel = new UsuarioModel();
                usuarioModel.setUsuario(this.user);
                usuarioModel.setPassword(this.password);
                Integer usuarios = this.getIAdminFacade().checkUsuario(usuarioModel);
                if (usuarios == 1) {
                    Integer rol = this.getIAdminFacade().getRol(usuarioModel);
                    if (rol == 255) {
                        getServletRequest().getSession().setAttribute("admin", "admin");
                        return "index";
                    } else {
                        addActionError("No tienes suficientes permisos");
                        getServletRequest().setAttribute("admin", "1");
                        return SUCCESS;
                    }
                } else {
                    addActionError("Login incorrecto");
                    getServletRequest().setAttribute("admin", "1");
                    return SUCCESS;
                }
            }
        } catch (Exception ex) {
            this.log.error("Error en adminLogin:\n" + this.getStacktrace(ex));
            return "error";
        }
    }
