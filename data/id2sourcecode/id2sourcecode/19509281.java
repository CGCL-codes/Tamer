    public static String gerarCodigoNumerico(String xml) {
        MessageDigest sha = null;
        try {
            sha = MessageDigest.getInstance(ALGORITMO_DIGEST);
        } catch (NoSuchAlgorithmException e) {
        }
        byte[] nfeHash = sha.digest(((String) XMLHelper.getTagConteudo(xml, HSConstants.ID_NFE, false).get(0)).getBytes());
        long codigoNumerico = 0L;
        int hashIndex = 0;
        for (int i = 0; i < CODIGO_NUMERICO_ARRAY.length; i++) {
            byte[] algarismoBytes = Arrays.copyOfRange(nfeHash, hashIndex, hashIndex + CODIGO_NUMERICO_ARRAY[i]);
            int somaBytes = somarBytes(algarismoBytes);
            int algarismo = somarInteiro(somaBytes);
            codigoNumerico = (long) ((double) codigoNumerico + (double) algarismo * Math.pow(10.0D, i));
            hashIndex += CODIGO_NUMERICO_ARRAY[i];
        }
        return CODIGO_NUMERICO_FORMAT.format(codigoNumerico);
    }
