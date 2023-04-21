    private void addAdministrador() {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] senha12345 = md.digest("12345".getBytes());
            AdministradorDAO a1;
            AdministradorDAO a2;
            a1 = new AdministradorDAO();
            a1.setEmail("admin@ex.com");
            a1.setLogin("admin1");
            a1.setSenha(senha12345);
            a1.setNome("Admin Um");
            a2 = new AdministradorDAO();
            a2.setEmail("admin@ex.com");
            a2.setLogin("admin2");
            a2.setSenha(senha12345);
            a2.setNome("Admin Dois");
            insertAdministrador(a1);
            insertAdministrador(a2);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
