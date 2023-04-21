    public static String encode(ESDO esdo) throws Exception {
        StringBuffer sb = new StringBuffer();
        sb.append("\t");
        append(sb, Setup.qareVersion);
        append(sb, esdo.contentNbr);
        DBSystem.Row toSystem = Setup.dbSystem.get(esdo.toSystem);
        DBRegisterInfo.Row info = Setup.dbRegisterInfo.get(toSystem);
        String dhAYS = "";
        if (info != null) {
            dhAYS = info.dhPublicKey;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        GZIPOutputStream gos = new GZIPOutputStream(baos);
        byte[] bESDO = esdo.toXML().getBytes();
        gos.write(bESDO, 0, bESDO.length);
        gos.close();
        byte[] cESDO = baos.toByteArray();
        append(sb, QConfig.returnAddress());
        DBKeyPair.Row keyPair = Setup.dbKeyPair.get();
        byte[] msgData;
        if ("".equals(dhAYS)) {
            if (!"none".equals(esdo.password)) {
                System.out.println("Bad message: " + esdo.toXML());
                throw new Exception("Missing DHPK for " + esdo.toSystem);
            }
            append(sb, "");
            msgData = cESDO;
            append(sb, msgData);
        } else {
            append(sb, Setup.dhPublicKey);
            BigInteger dhAY = new BigInteger(Base64.ascToBin(dhAYS.getBytes()));
            String dhXS = keyPair.getDHPrivateKey();
            BigInteger dhX = new BigInteger(Base64.ascToBin(dhXS.getBytes()));
            BigInteger dhP = DBKeyPair.dhP;
            BigInteger dhAXY = dhAY.modPow(dhX, dhP);
            byte[] secretKey = dhAXY.toByteArray();
            DESKeySpec desKeySpec = new DESKeySpec(secretKey);
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("DES", "CryptixCrypto");
            SecretKey secrectDesKey = secretKeyFactory.generateSecret(desKeySpec);
            String message = esdo.toXML();
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding", "CryptixCrypto");
            cipher.init(Cipher.ENCRYPT_MODE, secrectDesKey);
            msgData = cipher.doFinal(cESDO);
            append(sb, msgData);
        }
        append(sb, "" + bESDO.length);
        if (!esdo.signed) {
            append(sb, "");
            append(sb, "");
        } else {
            String dsaB64Pub = keyPair.getDSAPublicKey();
            append(sb, "" + dsaB64Pub);
            String dsaB64Pri = keyPair.getDSAPrivateKey();
            byte[] dsaPri = Base64.ascToBin(dsaB64Pri.getBytes());
            PKCS8EncodedKeySpec priKeySpec = new PKCS8EncodedKeySpec(dsaPri);
            KeyFactory keyFactory = KeyFactory.getInstance("DSA");
            PrivateKey dsaPriKey = keyFactory.generatePrivate(priKeySpec);
            Signature dsa = Signature.getInstance("SHA1withDSA");
            dsa.initSign(dsaPriKey);
            dsa.update(secureHash(msgData));
            byte[] sig = dsa.sign();
            append(sb, sig);
        }
        return sb.toString();
    }
