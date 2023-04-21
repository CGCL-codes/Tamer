    private void testEquals() {
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
        Membre clone = M1.clone();
        assertNotNull(clone);
        assertTrue(M1.equals(clone));
        clone.setNom("Bogo");
        assertFalse(M1.equals(clone));
    }
