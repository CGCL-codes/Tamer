    private void testGetmembre() {
        Date dat = new Date(1992, 06, 15);
        byte[] pwdByte = null;
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
            pwdByte = md5.digest("mdp1".getBytes());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        Membre M1 = new Membre("bk900424", "Boursier", "Alexandre", "S4T", "Informatique", dat, "IUT Nice", "Nice", "etudiant", pwdByte, "alexthebry@hotmail.fr");
        String idMembre = M1.getNumeroEtudiant();
        Key key = new Builder("Commentaire", 4).getKey();
        Commentaire C4 = new Commentaire("bk900424", new Long(1), "Tests 4 sur la classe commentaire");
        assertEquals(idMembre, C4.getMembre().getNumeroEtudiant());
        assertEquals(M1.getNom(), C4.getMembre().getNom());
        assertEquals(M1.getPrenom(), C4.getMembre().getPrenom());
    }
