    @SuppressWarnings("deprecation")
    private void testUpdateBDD() {
        Membre M6;
        Date dat = new Date(1992, 06, 15);
        byte[] pwdByte = null;
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
            pwdByte = md5.digest("mdp1".getBytes());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        M6 = new Membre("ba90045", "Boursie", "Andre", "S4", "Iique", dat, "IT ", "ce", "eant", pwdByte, "alexhbyhmi.r");
        assertNotNull(M6);
        try {
            md5 = MessageDigest.getInstance("MD5");
            pwdByte = md5.digest("goulougoulou".getBytes());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        M6.updateMembre("nom", "prenom", "email", "ecole", new Date(1925, 6, 15), "filiere", "promotion", "statut", "ville", pwdByte);
        Membre M2 = Membre.readMembre("ba90045");
        assertEquals("ba90045", M2.getNumeroEtudiant());
        assertEquals(new String("prenom"), M2.getPrenom());
        assertEquals("nom", M2.getNom());
        assertEquals("promotion", M2.getPromotion());
        assertEquals("filiere", M2.getFiliere());
        assertEquals(new Date(1925, 6, 15), M2.getDateNaissanceSimple());
        assertEquals("ecole", M2.getEcoleActuelle());
        assertEquals("ville", M2.getVille());
        assertEquals("statut", M2.getStatut());
        assertEquals("email", M2.getEmail());
    }
