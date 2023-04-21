    private static void handleCheckVote(AgentServer as) {
        PrivateKey keyRegistrar = null;
        Certificate certCollector;
        try {
            LOGGER.info("Checking vote");
            keyRegistrar = AgentCore.getKey(as.af.aData.ksd);
            LOGGER.trace("Receive msg");
            byte[] datadec = AgentCore.receiveBytes(as.s);
            LOGGER.trace("Decript msg");
            byte[] msg = Crypto.decrypt(datadec, keyRegistrar);
            LOGGER.trace("Extract R, signR");
            byte[] R = AgentCore.getS1FromMsg(msg);
            byte[] signR = AgentCore.getS2FromMsg(msg);
            byte[] bIDVotazione = AgentCore.getS3FromMsg(msg);
            String IDVotazione = Basic.byte2String(bIDVotazione);
            certCollector = as.af.getCertificate(IDVotazione, Role4VotazioneData.C_COLLECTOR);
            if (certCollector == null) {
                throw new Exception("cert registrar not found");
            }
            String response = "BOH";
            LOGGER.debug("check signature registrar");
            if (!Crypto.verifySign(R, signR, certCollector.getPublicKey())) {
                LOGGER.error("Signature registrar not correct");
                response = "FAIL";
            } else {
                byte[] T1 = AgentCore.getS1FromMsg(R);
                byte[] DVotazioneSalt = AgentCore.getS2FromMsg(R);
                byte[] DVotazioneSaltT2 = AgentCore.getS3FromMsg(R);
                byte[] T2 = Registrar.getT2FromT1(T1, as.config);
                if (T2 == null) {
                    LOGGER.error("T2 not found");
                    response = "FAIL";
                } else {
                    byte[] m = AgentCore.createMsg(DVotazioneSalt, T2);
                    MessageDigest algorithm = MessageDigest.getInstance("MD5");
                    algorithm.reset();
                    algorithm.update(m);
                    byte[] MyDVotazioneSaltT2 = algorithm.digest();
                    if (Arrays.equals(MyDVotazioneSaltT2, DVotazioneSaltT2)) {
                        LOGGER.info("... vote check is successfull");
                        response = "OK";
                    } else {
                        LOGGER.error("DVotazioneSaltT2 s differs");
                        response = "FAIL";
                    }
                }
            }
            LOGGER.info("Send response");
            String cmd = "HTTP/1.1 200 OK\n";
            cmd += "Content-Type: text/plain\n\n";
            cmd += response;
            as.s.getOutputStream().write(cmd.getBytes());
        } catch (IOException e) {
            LOGGER.error("IO Error", e);
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("Algorithm error", e);
        } catch (InvalidKeySpecException e) {
            LOGGER.error("Invalid key", e);
        } catch (Exception e) {
            LOGGER.error("Unexpected error", e);
        }
    }
