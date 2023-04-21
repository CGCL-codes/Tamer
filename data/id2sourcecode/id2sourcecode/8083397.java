    public void cadastrar() {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        BigInteger hash = new BigInteger(1, md.digest(senha.getBytes()));
        String pass = hash.toString(16);
        msg = "Usu�rio n�o Cadastrado - Login j� existe!";
        DAOUsuario daouser = new DAOUsuario();
        Usuario teste = daouser.findByLogin(login);
        if (teste == null) {
            if (tipo.equals("Aluno")) {
                DAOAluno daoAlu = new DAOAluno();
                try {
                    daoAlu.begin();
                    Aluno a = new Aluno();
                    a.setNome(nome);
                    a.setLogin(login);
                    a.setSenha(pass);
                    daoAlu.persist(a);
                    daoAlu.commit();
                    msg = new String("Aluno cadastrado!");
                } catch (Exception e) {
                    e.getMessage();
                }
            }
            if (tipo.equals("Professor")) {
                DAOProfessor daoPro = new DAOProfessor();
                try {
                    daoPro.begin();
                    Professor p = new Professor();
                    p.setNome(nome);
                    p.setLogin(login);
                    p.setSenha(pass);
                    daoPro.persist(p);
                    daoPro.commit();
                    msg = new String("Professor cadastrado!");
                } catch (Exception e) {
                    e.getMessage();
                }
            }
            nome = null;
            login = null;
            senha = null;
        }
    }
