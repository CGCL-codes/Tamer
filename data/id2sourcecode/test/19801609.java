    public void atualizar() {
        DAOUsuario daouser = new DAOUsuario();
        Usuario u = daouser.findByLogin(atual.getLogin());
        u.setNome(nome);
        if (novaSenha) {
            MessageDigest md = null;
            try {
                md = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            BigInteger hash = new BigInteger(1, md.digest(senha.getBytes()));
            String pass = hash.toString(16);
            u.setSenha(pass);
        }
        daouser.begin();
        daouser.merge(u);
        daouser.commit();
        senha = null;
    }
