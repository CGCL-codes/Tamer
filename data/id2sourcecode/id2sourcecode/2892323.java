    private void loguer() {
        if (tMDP.getPassword().length == 0 || tLog.getText().equals("")) {
            Alertes.afficher(Alertes.CHAMP_NON_REMPLI);
        } else {
            Main.verifyConnection();
            Main.fenetre().connect();
            byte[] login = tLog.getText().getBytes(Constantes.UTF8);
            try {
                byte[] mdp = MessageDigest.getInstance("SHA-512").digest(new String(tMDP.getPassword()).getBytes(Constantes.UTF8));
                byte[] data = new byte[login.length + 64];
                System.arraycopy(mdp, 0, data, 0, 64);
                System.arraycopy(login, 0, data, 64, login.length);
                loguer(data, Constantes.Login.VERIFY);
            } catch (NoSuchAlgorithmException e) {
                Main.fenetre().erreur(Fenetre.HASH_IMPOSSIBLE, e);
            }
        }
    }
