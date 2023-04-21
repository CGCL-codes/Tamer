    public boolean validate() {
        if (getPasswords().containsKey(id)) {
            try {
                String pass = getPasswords().get(id);
                MessageDigest messageDigest = MessageDigest.getInstance("MD5");
                byte[] pwd = messageDigest.digest(password.getBytes());
                String code = "";
                for (int i = 0; i < pwd.length; i++) {
                    code += pwd[i];
                }
                boolean result = code.equals(pass);
                if (result && id.equals("admin")) {
                    FacesContext context = FacesContext.getCurrentInstance();
                    if (context != null) {
                        SessionBean1 session = (SessionBean1) context.getExternalContext().getSessionMap().get("SessionBean1");
                        if (session == null) {
                            session = new SessionBean1();
                            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("SessionBean1", session);
                        }
                        session.setAdmin(true);
                    }
                }
                return result;
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(LoginBean.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        } else {
            return false;
        }
    }
