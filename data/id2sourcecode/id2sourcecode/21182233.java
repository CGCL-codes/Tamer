    public static byte[] toEncode(String algo, String chaine) {
        try {
            byte[] hash = MessageDigest.getInstance(algo).digest(chaine.getBytes());
            return hash;
        } catch (NoSuchAlgorithmException exception) {
            logger.error("Erreur : l'algo " + algo + " du MessageDigest est indisponible");
        }
        return null;
    }
