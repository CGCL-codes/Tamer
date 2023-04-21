    @SuppressWarnings("deprecation")
    private void testConstructeurSet() {
        Membre M1;
        Date dat = new Date(1992, 06, 15);
        byte[] pwdByte = null;
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
            pwdByte = md5.digest("mdp1".getBytes());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        M1 = new Membre("ba9004", "Boursier", "Alexandre", "S4T", "Informatique", dat, "IUT Nice", "Nice", "etudiant", pwdByte, "alexthebry@hotmail.fr");
        assertNotNull(M1);
        M1.setNumeroEtudiant("bk900424");
        M1.setNom("Bogo");
        M1.setPrenom("Kevin");
        M1.setFiliere("Info");
        M1.setPromotion("S5");
        M1.setEcoleActuelle("IUP");
        M1.setDateNaissance(new Date(1991, 9, 05));
        try {
            md5 = MessageDigest.getInstance("MD5");
            pwdByte = md5.digest("goulougoulou".getBytes());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        M1.setMdp(pwdByte);
        M1.setStatut("dieu");
        M1.setVille("Grasse");
        M1.setEmail("KBP@hotmail.fr");
        assertEquals("bk900424", M1.getNumeroEtudiant());
        assertEquals(new String("Kevin"), M1.getPrenom());
        assertEquals("Bogo", M1.getNom());
        assertEquals("S5", M1.getPromotion());
        assertEquals("Info", M1.getFiliere());
        assertEquals(new Date(1991, 9, 5), M1.getDateNaissanceSimple());
        assertEquals("IUP", M1.getEcoleActuelle());
        assertEquals("Grasse", M1.getVille());
        assertEquals("dieu", M1.getStatut());
        assertEquals(pwdByte, M1.getMdp());
        assertEquals("KBP@hotmail.fr", M1.getEmail());
    }
