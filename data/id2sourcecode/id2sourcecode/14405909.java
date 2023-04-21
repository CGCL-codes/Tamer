    @SuppressWarnings("deprecation")
    private void testConstructeurGet() {
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
        M1 = new Membre("ba900450", "Boursier", "Alexandre", "S4T", "Informatique", dat, "IUT Nice", "Nice", "etudiant", pwdByte, "alexthebry@hotmail.fr");
        assertNotNull(M1);
        assertEquals(new String("Alexandre"), M1.getPrenom());
        assertEquals("Boursier", M1.getNom());
        assertEquals("ba900450", M1.getNumeroEtudiant());
        assertEquals("S4T", M1.getPromotion());
        assertEquals("Informatique", M1.getFiliere());
        assertEquals(new Date(1992, 06, 15), M1.getDateNaissanceSimple());
        assertEquals("IUT Nice", M1.getEcoleActuelle());
        assertEquals("Nice", M1.getVille());
        assertEquals("etudiant", M1.getStatut());
        assertEquals(pwdByte, M1.getMdp());
        assertEquals("alexthebry@hotmail.fr", M1.getEmail());
    }
