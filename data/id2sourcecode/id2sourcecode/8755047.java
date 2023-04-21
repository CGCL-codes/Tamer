    public String openSession(String IDVotazione) {
        System.out.println("Start session for " + IDVotazione);
        S1 = null;
        keyPairTmp = null;
        String result = "OK";
        try {
            System.out.println("Generate a key pair for cripting");
            keyPairTmp = Crypto.generateKeyPair(512);
            PublicKey pp = keyPairTmp.getPublic();
            System.out.println("Generate S1");
            KeyGen kg = new KeyGen();
            S1 = kg.getRandomKey(Registrar.SessionTokenSize);
            if (Debug.debuglevel > 2) System.out.println("Prepare 1st msg of challenge");
            byte[] bundle_auth = null;
            if (tyAuth.equals("X509")) {
                byte[] certBytes = cert.getEncoded();
                bundle_auth = AgentCore.createMsg("X509".getBytes(), certBytes);
            } else if (tyAuth.equals(Registrar.C_AUTHUSERPASS)) {
                bundle_auth = AgentCore.createMsg(Registrar.C_AUTHUSERPASS.getBytes(), this.authMan.toString().getBytes());
            }
            byte[] bundle = AgentCore.createMsg(S1.getBytes(), bundle_auth, pp.getEncoded(), IDVotazione.getBytes());
            byte[] signBundle = null;
            if (Debug.debuglevel > 2) System.out.println("Sign bundle");
            if (tyAuth.equals("X509")) {
                signBundle = Crypto.sign(bundle, key);
            } else {
                signBundle = "NONE".getBytes();
            }
            if (Debug.debuglevel > 2) System.out.println("prepare 1st msg");
            byte[] msg = AgentCore.createMsg(bundle, signBundle);
            byte[] encBytes = Crypto.encrypt(msg, registrarServerCert.getPublicKey());
            System.out.println("Send 1st msg to registrar");
            String sauthServer = registrarServerUrl + "/registrar/openSession";
            String response = AgentCore.sendBytes(encBytes, sauthServer);
            System.out.println("response:" + response);
            if (response.startsWith("FAIL")) {
                if (response.equals(AgentCore.ERRMSG_VOTATIONLOCKED)) throw new MyException(MyException.ERROR_FASTEXIT, response);
                System.out.println("CHECKTHIS[" + response + "]");
                throw new Exception("Registrar refuse 1st msg");
            }
            System.out.println("Send Ok");
            byte[] msgR = Base64.decodeBase64(response.getBytes("utf-8"));
            if (Debug.debuglevel > 2) System.out.println("Descramble msgR");
            msg = Crypto.decrypt(msgR, keyPairTmp.getPrivate(), "RSA", 50, 64);
            byte[] bS1R = AgentCore.getS1FromMsg(msg);
            byte[] signS1R = AgentCore.getS2FromMsg(msg);
            System.out.println("Check S1R");
            if (!Arrays.equals(S1.getBytes(), bS1R)) throw new Exception("S1R differs from S1");
            boolean check = Crypto.verifySign(S1.getBytes(), signS1R, registrarServerCert.getPublicKey());
            if (!check) throw new Exception("S1R sign verification filed");
        } catch (IOException e) {
            result = AgentCore.ERRMSG_IO;
        } catch (MyException e) {
            if (e.getErrType() == MyException.ERROR_FASTEXIT) result = e.getErrMsg(); else result = "FAIL::" + e.getErrAsString();
        } catch (Exception e) {
            LOGGER.error("Error opening session", e);
            result = "FAIL";
        }
        System.out.println("End OpenSession: " + result);
        return result;
    }
