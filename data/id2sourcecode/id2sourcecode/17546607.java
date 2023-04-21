    @Override
    public void init(FilterConfig arg0) throws ServletException {
        da = new DAOAdministrador();
        Administrador adm = new Administrador();
        adm.setLogin("admin");
        adm.setNome("Administrador");
        String pass_temp = "admin";
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        BigInteger hash = new BigInteger(1, md.digest(pass_temp.getBytes()));
        String pass = hash.toString(16);
        adm.setSenha(pass);
        try {
            da.persist(adm);
        } catch (Exception e) {
            e.getMessage();
        }
        daoalu = new DAOAluno();
        Aluno alu = new Aluno();
        alu.setLogin("aluno");
        alu.setNome("Aluno");
        pass_temp = "aluno";
        md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        hash = new BigInteger(1, md.digest(pass_temp.getBytes()));
        pass = hash.toString(16);
        alu.setSenha(pass);
        try {
            daoalu.persist(alu);
        } catch (Exception e) {
            e.getMessage();
        }
    }
