    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String num = req.getParameter("numero");
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] pwdByte = md5.digest(req.getParameter("pwd").getBytes());
            PersistenceManager pm = PMF.get().getPersistenceManager();
            Query query = pm.newQuery("select from " + Membre.class.getName() + " where numeroEtudiant == mail && mdp == pwd");
            query.declareParameters("String mail, byte[] pwd");
            List<Membre> greetings = (List<Membre>) query.execute(num, pwdByte);
            if (greetings.isEmpty()) {
                pm.close();
                resp.sendRedirect("/index.jsp?noval=" + new String("false") + "");
            } else {
                SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");
                Membre co = greetings.get(0);
                HttpSession session = req.getSession(true);
                session.setAttribute("connected", true);
                session.setAttribute("email", co.getEmail());
                session.setAttribute("idMembre", co.getNumeroEtudiant());
                session.setAttribute("nom", co.getNom());
                session.setAttribute("prenom", co.getPrenom());
                session.setAttribute("promotion", co.getPromotion());
                session.setAttribute("filiere", co.getFiliere());
                session.setAttribute("dateNaissance", co.getDateNaissance());
                session.setAttribute("ecoleActuelle", co.getEcoleActuelle());
                session.setAttribute("ville", co.getVille());
                session.setAttribute("statut", co.getStatut());
                pm.close();
                resp.sendRedirect("/accueil.jsp");
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
