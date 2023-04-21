    @SuppressWarnings("deprecation")
    private void testDeleteBDD() {
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
        M1 = new Membre("ba900458", "Boursier", "Alexandre", "S4T", "Informatique", dat, "IUT Nice", "Nice", "etudiant", pwdByte, "alexthebry@hotmail.fr");
        Membre M4 = Membre.readMembre("ba900458");
        assertNotNull(M4);
        M1.deleteMembre();
        M4 = Membre.readMembre("ba900458");
        assertNull(M4);
    }
