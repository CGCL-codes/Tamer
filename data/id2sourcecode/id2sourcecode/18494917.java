    public static byte[] createT1(byte[] keyRegistrar, byte[] cert, String IDVotazione) {
        byte[] T1 = null;
        try {
            byte[] IDVotazioneBytes = IDVotazione.getBytes("utf-8");
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(keyRegistrar);
            algorithm.update(IDVotazioneBytes);
            byte saltVotazione[] = algorithm.digest();
            algorithm.reset();
            algorithm.update(cert);
            algorithm.update(saltVotazione);
            byte K1[] = algorithm.digest();
            T1 = new byte[K1.length + IDVotazioneBytes.length];
            for (int i = 0; i < K1.length; i++) T1[i] = K1[i];
            for (int i = 0; i < IDVotazioneBytes.length; i++) T1[i + K1.length] = IDVotazioneBytes[i];
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("Encoding error", e);
            T1 = null;
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("Errore while generating T1", e);
            T1 = null;
        }
        return T1;
    }
