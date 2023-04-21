    private void addProfessor() {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] senha12345 = md.digest("12345".getBytes());
            ProfessorDAO p1;
            ProfessorDAO p2;
            p1 = new ProfessorDAO();
            p1.setEmail("prof1@ex.com");
            p1.setNome("Prof Um");
            p1.setLogin("prof1");
            p1.setSenha(senha12345);
            p2 = new ProfessorDAO();
            p2.setEmail("prof2@ex.com");
            p2.setNome("Prof Dois");
            p2.setLogin("prof2");
            p2.setSenha(senha12345);
            insertProfessor(p1);
            insertProfessor(p2);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
