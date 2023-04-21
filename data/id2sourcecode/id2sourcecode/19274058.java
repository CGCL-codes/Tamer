    public String sendVote(String IDVotazione, byte[] T1Bundle, byte[] T2, byte[] votazioneBytes, byte[] saltBytes) {
        String result = "OK";
        try {
            LOGGER.info("Sending vote");
            LOGGER.debug("create envelope");
            byte[] votazioneSalt = AgentCore.createMsg(votazioneBytes, saltBytes);
            LOGGER.debug("Calc MD5 of VS");
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(votazioneSalt);
            byte[] DVotazioneSalt = algorithm.digest();
            LOGGER.debug("Calc signature of DVS with T2");
            byte[] DVotazioneSaltT2 = AgentCore.createMsg(DVotazioneSalt, T2);
            algorithm.reset();
            algorithm.update(DVotazioneSaltT2);
            byte[] signDVotazioneSaltT2 = algorithm.digest();
            LOGGER.debug("create vote envelope and encript it");
            byte[] envelope = AgentCore.createMsg(votazioneSalt, signDVotazioneSaltT2);
            byte[] envelopeEnc = Crypto.encrypt(envelope, collectorCert.getPublicKey());
            LOGGER.debug("create forward envelope");
            byte[] msg = AgentCore.createMsg(IDVotazione.getBytes(), T1Bundle, envelopeEnc);
            LOGGER.debug("cript for forwarder");
            byte[] encBytes = Crypto.encrypt(msg, forwarderCert.getPublicKey());
            LOGGER.debug("Send FEnv to forward");
            String sforwarderServer = forwarderUrl + "/forwarder/sendVote";
            String response = AgentCore.sendBytes(encBytes, sforwarderServer);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("response:" + response);
            }
            if (response.startsWith("FAIL")) {
                if (response.startsWith("FAIL::TimeTooLate")) throw new MyException(MyException.ERROR_FASTEXIT, AgentCore.ERRMSG_VOTATIONCLOSED);
                if (response.startsWith("FAIL::TimeTooEarly")) throw new MyException(MyException.ERROR_FASTEXIT, AgentCore.ERRMSG_VOTATIONNOTOPEN);
                throw new MyException(MyException.ERROR_FORWARDERREFUSEVOTE, response);
            }
        } catch (MyException e) {
            if (e.getErrType() == MyException.ERROR_FASTEXIT) return e.getErrMsg();
            LOGGER.error("MyException", e);
            result = e.getErrMsg();
        } catch (Exception e) {
            LOGGER.error("Unexpected Exception", e);
            result = "FAIL";
        }
        return result;
    }
