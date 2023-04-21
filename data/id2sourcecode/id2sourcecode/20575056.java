    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String classe = request.getParameter("classe");
        if (classe.equals("usuario")) {
            String login = request.getParameter("login");
            String pass_temp = request.getParameter("pass");
            String nome = request.getParameter("nome");
            MessageDigest md = null;
            try {
                md = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            BigInteger hash = new BigInteger(1, md.digest(pass_temp.getBytes()));
            String pass = hash.toString(16);
            DAOUsuario daouser = new DAOUsuario();
            Usuario u = daouser.findByLogin(login);
            u.setNome(nome);
            u.setSenha(pass);
            daouser.begin();
            daouser.merge(u);
            daouser.commit();
        } else if (classe.equals("curso")) {
            String nome = request.getParameter("nome");
            String descricao = request.getParameter("desc");
            int cargahoraria = Integer.parseInt(request.getParameter("ch"));
            DAOCurso daocurso = new DAOCurso();
            Curso c = daocurso.find(nome);
            c.setCargahoraria(cargahoraria);
            c.setDescricao(descricao);
            daocurso.begin();
            daocurso.merge(c);
            daocurso.commit();
        }
        RequestDispatcher rqd = request.getRequestDispatcher("includes/retorno.jsp");
        rqd.forward(request, response);
    }
