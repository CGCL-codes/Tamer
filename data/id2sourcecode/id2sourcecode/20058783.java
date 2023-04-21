        @Override
        public boolean authenticate(String login, char[] pass, String server) {
            try {
                byte[] b = new String(pass).getBytes();
                try {
                    b = CriptUtil.digest(b, CriptUtil.SHA);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                this.user = Facade.getInstance().loadUser(login, CriptUtil.byteArrayToHexString(b));
                if (this.user != null) {
                    if (login.equals(this.user.getLogin())) {
                        dialog.setVisible(false);
                        MainFrame.getInstance().setUserLogged(this.user);
                        return true;
                    }
                }
            } catch (MyHibernateException ex) {
                Logger.getLogger(LoginDialogFactory.class.getName()).log(Level.SEVERE, null, ex);
                Util.errorSQLPane(MainFrame.getInstance(), ex);
            }
            return false;
        }
