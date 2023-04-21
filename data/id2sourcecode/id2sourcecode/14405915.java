    private void testClone() {
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
        Membre clone = M1.clone();
        assertEquals(new String("Alexandre"), clone.getPrenom());
        assertEquals("Boursier", clone.getNom());
        assertEquals("ba900450", clone.getNumeroEtudiant());
        assertEquals("S4T", clone.getPromotion());
        assertEquals("Informatique", clone.getFiliere());
        assertEquals(new Date(1992, 06, 15), clone.getDateNaissanceSimple());
        assertEquals("IUT Nice", clone.getEcoleActuelle());
        assertEquals("Nice", clone.getVille());
        assertEquals("etudiant", clone.getStatut());
        assertEquals(pwdByte, clone.getMdp());
        assertEquals("alexthebry@hotmail.fr", M1.getEmail());
    }
