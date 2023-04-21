    private static void handleSendVote(AgentServer as) {
        PrivateKey keyCollector = null;
        Certificate certCollector = null;
        Certificate certForwarder = null;
        Certificate certRegistrar = null;
        PublicKey pubKeyRegistrar = null;
        String response = "BOH";
        try {
            LOGGER.info("received a packet of votes");
            MessageDigest md5_algorithm = MessageDigest.getInstance("MD5");
            keyCollector = as.af.aData.key;
            certCollector = as.af.aData.certificate;
            LOGGER.trace("Receiving msg");
            byte[] msgenc = AgentCore.receiveBytes(as.s);
            LOGGER.trace("message received msg");
            LOGGER.trace("descramble msg");
            byte[] netMessageSerialized = Crypto.decrypt(msgenc, keyCollector);
            CollectorNetMessage msg = CollectorNetMessage.byte_unserialize(netMessageSerialized);
            if (msg == null) {
                LOGGER.error("Couldn't unserialize the message");
                throw new Exception("Couldn't unserialize the message");
            }
            String IDVotazione = msg.IDVotazione;
            byte[] zippedData = msg.data;
            byte[] signatureZippedData = msg.signature;
            setLogInfo(as.config, IDVotazione);
            certRegistrar = as.af.getCertificate(IDVotazione, Role4VotazioneData.C_REGISTRAR);
            if (certRegistrar == null) {
                LOGGER.warn("cert registrar not found");
                throw new Exception("cert registrar not found");
            }
            String urlRegistrar = as.af.getUrl(IDVotazione, Role4VotazioneData.C_REGISTRAR);
            certForwarder = as.af.getCertificate(IDVotazione, Role4VotazioneData.C_FORWARDER);
            if (certForwarder == null) {
                LOGGER.warn("cert forwarder not found");
                throw new Exception("cert forwarder not found");
            }
            RegistrarClient rc = new RegistrarClient(urlRegistrar, certRegistrar, keyCollector, certCollector);
            LOGGER.trace("Verifing zip signature");
            if (!Crypto.verifySign(zippedData, signatureZippedData, certForwarder.getPublicKey())) throw new Exception("Forwarder sign fault");
            pubKeyRegistrar = certRegistrar.getPublicKey();
            ByteArrayInputStream stream_in = new ByteArrayInputStream(zippedData);
            ZipInputStream zip_in = new ZipInputStream(stream_in);
            ZipEntry entry;
            LOGGER.debug("Create cypher");
            Cipher cipher = Crypto.get_generic_cypher(keyCollector);
            LOGGER.info("going to process votes");
            StringBuffer failedVoteBuffer = new StringBuffer();
            byte[] entry_data, T1, signT1, envelopeEnc, inner_entry_data, envelope, votazioneSalt, DVotazioneSaltT2, DVotazioneSalt, bvotazione;
            while ((entry = zip_in.getNextEntry()) != null) {
                entry_data = ReadZipEntry.read(zip_in);
                if (entry_data == null) throw new Exception("Error reading zip file");
                ZipInputStream inner_zip_stream = new ZipInputStream(new ByteArrayInputStream(entry_data));
                T1 = null;
                signT1 = null;
                envelopeEnc = null;
                ZipEntry innerEntry;
                while ((innerEntry = inner_zip_stream.getNextEntry()) != null) {
                    inner_entry_data = ReadZipEntry.read(inner_zip_stream);
                    String entryName = innerEntry.getName();
                    if (entryName.equals("T1")) T1 = inner_entry_data; else if (entryName.equals("signT1")) signT1 = inner_entry_data; else if (entryName.equals("envelope")) envelopeEnc = inner_entry_data; else throw new Exception("Unknown entry found in zip file");
                }
                LOGGER.trace("Verifing T1 signature");
                if (!Crypto.verifySign(T1, signT1, pubKeyRegistrar)) throw new Exception("Registrar sign fault");
                LOGGER.trace("Decrypt vote");
                envelope = Crypto.decrypt(envelopeEnc, cipher);
                LOGGER.trace("Extract vote");
                votazioneSalt = AgentCore.getS1FromMsg(envelope);
                DVotazioneSaltT2 = AgentCore.getS2FromMsg(envelope);
                md5_algorithm.reset();
                md5_algorithm.update(votazioneSalt);
                DVotazioneSalt = md5_algorithm.digest();
                LOGGER.trace("Check digest");
                boolean check = rc.checkVote(T1, DVotazioneSalt, DVotazioneSaltT2, IDVotazione);
                if (!check) {
                    LOGGER.trace("Vote signature refused!");
                    throw new Exception("Vote signature refused!");
                }
                LOGGER.trace("a vote is verified and correct");
                bvotazione = AgentCore.getS1FromMsg(votazioneSalt);
                LOGGER.trace("registering vote");
                int icheck = Collector.registerVote(IDVotazione, T1, DVotazioneSaltT2, bvotazione, as.config);
                if (icheck < 0) {
                    String sT1 = new String(Base64.encodeBase64(T1), "utf-8");
                    LOGGER.error("couldn't register the vote with T1: " + sT1);
                    failedVoteBuffer.append(sT1);
                    failedVoteBuffer.append("\n");
                } else {
                    LOGGER.trace("vote registered");
                }
            }
            response = "OK";
            if (failedVoteBuffer.length() > 0) {
                response = failedVoteBuffer.toString();
            }
        } catch (IOException e) {
            LOGGER.error("IO Exception", e);
            response = "FAIL";
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("Algorithm unkown", e);
            response = "FAIL";
        } catch (InvalidKeySpecException e) {
            LOGGER.error("Invalid key", e);
            response = "FAIL";
        } catch (Exception e) {
            LOGGER.error("Unexpected exception", e);
            response = "FAIL";
        }
        if (response.equals("OK")) {
            LOGGER.info("Votes registered");
        }
        LOGGER.debug("Send response");
        String cmd = "HTTP/1.1 200 OK\r\n";
        cmd += "Content-Type: text/plain\r\n\r\n";
        cmd += response;
        try {
            as.s.getOutputStream().write(cmd.getBytes());
        } catch (IOException e) {
            LOGGER.error("IO Exception", e);
        }
        removeLoginfo();
    }
