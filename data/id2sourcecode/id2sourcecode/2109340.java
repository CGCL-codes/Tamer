    public String validate_action() {
        if (getOldPassword() == null) {
            errorMessage.setText("ancien mot de passe erroné");
            return null;
        }
        if (getNewPassword() == null) {
            errorMessage.setText("le nouveau mot de passe ne peut pas être vide");
            return null;
        }
        LoginBean login = (LoginBean) getBean("LoginBean");
        login.setPassword(oldPassword);
        Map<String, String> passwords = login.getPasswords();
        if (login.validate()) {
            if (getNewPassword().equals(getConfirmationPassword())) {
                try {
                    MessageDigest messageDigest = MessageDigest.getInstance("MD5");
                    byte[] encoded = messageDigest.digest(newPassword.getBytes());
                    String code = "";
                    for (int i = 0; i < encoded.length; i++) {
                        code += encoded[i];
                    }
                    passwords.put(login.getId(), code);
                    errorMessage.setText("Mot de passe changé avec succès");
                    login.setPasswords(passwords);
                    new Password().savePasswords(passwords);
                } catch (NoSuchAlgorithmException ex) {
                    Logger.getLogger(LoginBean.class.getName()).log(Level.SEVERE, null, ex);
                    return "null";
                }
            } else {
                errorMessage.setText("Les deux mots de passe ne sont pas identiques");
            }
        } else {
            errorMessage.setText("ancien mot de passe erroné");
        }
        return "null";
    }
