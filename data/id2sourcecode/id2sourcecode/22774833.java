    private void addUsers() {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] senha12345 = md.digest("12345".getBytes());
            dao.memory.UsuarioDAO u1 = new dao.memory.UsuarioDAO();
            u1.setEmail("m.r650200@gmail.com");
            u1.setLogin("m.r650200");
            u1.setNome("m.r650200");
            u1.setSenha(senha12345);
            dao.memory.UsuarioDAO u2 = new dao.memory.UsuarioDAO();
            u2.setEmail("ex1@ex.com.br");
            u2.setLogin("ex1");
            u2.setNome("ex1");
            u2.setSenha(senha12345);
            dao.memory.UsuarioDAO u3 = new dao.memory.UsuarioDAO();
            u3.setEmail("ex2");
            u3.setLogin("ex3");
            u3.setNome("ex3");
            u3.setSenha(senha12345);
            dao.memory.UsuarioDAO u4 = new dao.memory.UsuarioDAO();
            u4.setEmail("m.r650200@gmail.com");
            u4.setLogin("m.r650200");
            u4.setNome("m.r650200");
            u4.setSenha(senha12345);
            insertUsuario(u1);
            insertUsuario(u2);
            insertUsuario(u3);
            insertUsuario(u4);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
