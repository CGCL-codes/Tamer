    protected byte[] generateResponseValue(String authMethod, String digestUriValue, String qopValue, String usernameValue, String realmValue, char[] passwdValue, byte[] nonceValue, byte[] cNonceValue, int nonceCount, byte[] authzidValue) throws NoSuchAlgorithmException, UnsupportedEncodingException, IOException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] hexA1, hexA2;
        ByteArrayOutputStream A2, beginA1, A1, KD;
        A2 = new ByteArrayOutputStream();
        A2.write((authMethod + ":" + digestUriValue).getBytes(encoding));
        if (qopValue.equals("auth-conf") || qopValue.equals("auth-int")) {
            logger.log(Level.FINE, "DIGEST04:QOP: {0}", qopValue);
            A2.write(SECURITY_LAYER_MARKER.getBytes(encoding));
        }
        if (logger.isLoggable(Level.FINE)) {
            logger.log(Level.FINE, "DIGEST05:A2: {0}", A2.toString());
        }
        md5.update(A2.toByteArray());
        byte[] digest = md5.digest();
        hexA2 = binaryToHex(digest);
        if (logger.isLoggable(Level.FINE)) {
            logger.log(Level.FINE, "DIGEST06:HEX(H(A2)): {0}", new String(hexA2));
        }
        beginA1 = new ByteArrayOutputStream();
        beginA1.write(stringToByte_8859_1(usernameValue));
        beginA1.write(':');
        beginA1.write(stringToByte_8859_1(realmValue));
        beginA1.write(':');
        beginA1.write(stringToByte_8859_1(new String(passwdValue)));
        md5.update(beginA1.toByteArray());
        digest = md5.digest();
        if (logger.isLoggable(Level.FINE)) {
            logger.log(Level.FINE, "DIGEST07:H({0}) = {1}", new Object[] { beginA1.toString(), new String(binaryToHex(digest)) });
        }
        A1 = new ByteArrayOutputStream();
        A1.write(digest);
        A1.write(':');
        A1.write(nonceValue);
        A1.write(':');
        A1.write(cNonceValue);
        if (authzidValue != null) {
            A1.write(':');
            A1.write(authzidValue);
        }
        md5.update(A1.toByteArray());
        digest = md5.digest();
        H_A1 = digest;
        hexA1 = binaryToHex(digest);
        if (logger.isLoggable(Level.FINE)) {
            logger.log(Level.FINE, "DIGEST08:H(A1) = {0}", new String(hexA1));
        }
        KD = new ByteArrayOutputStream();
        KD.write(hexA1);
        KD.write(':');
        KD.write(nonceValue);
        KD.write(':');
        KD.write(nonceCountToHex(nonceCount).getBytes(encoding));
        KD.write(':');
        KD.write(cNonceValue);
        KD.write(':');
        KD.write(qopValue.getBytes(encoding));
        KD.write(':');
        KD.write(hexA2);
        if (logger.isLoggable(Level.FINE)) {
            logger.log(Level.FINE, "DIGEST09:KD: {0}", KD.toString());
        }
        md5.update(KD.toByteArray());
        digest = md5.digest();
        byte[] answer = binaryToHex(digest);
        if (logger.isLoggable(Level.FINE)) {
            logger.log(Level.FINE, "DIGEST10:response-value: {0}", new String(answer));
        }
        return (answer);
    }
