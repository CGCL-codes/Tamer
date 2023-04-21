    public void performClientExchange(String clientId, String serverId, byte[] clientKexInit, byte[] serverKexInit, boolean firstPacketFollows, boolean useFirstPacket) throws IOException {
        try {
            log.info("Starting client side key exchange.");
            transport.getMessageStore().registerMessage(SshMsgKexGssInit.SSH_MSG_KEXGSS_INIT, SshMsgKexGssInit.class);
            transport.getMessageStore().registerMessage(SshMsgKexGssContinue.SSH_MSG_KEXGSS_CONTINUE, SshMsgKexGssContinue.class);
            transport.getMessageStore().registerMessage(SshMsgKexGssComplete.SSH_MSG_KEXGSS_COMPLETE, SshMsgKexGssComplete.class);
            transport.getMessageStore().registerMessage(SshMsgKexGssHostKey.SSH_MSG_KEXGSS_HOSTKEY, SshMsgKexGssHostKey.class);
            transport.getMessageStore().registerMessage(SshMsgKexGssError.SSH_MSG_KEXGSS_ERROR, SshMsgKexGssError.class);
            this.clientId = clientId;
            this.serverId = serverId;
            this.clientKexInit = clientKexInit;
            this.serverKexInit = serverKexInit;
            try {
                DHParameterSpec dhSkipParamSpec = new DHParameterSpec(p, g);
                dhKeyPairGen.initialize(dhSkipParamSpec);
                KeyPair dhKeyPair = dhKeyPairGen.generateKeyPair();
                dhKeyAgreement.init(dhKeyPair.getPrivate());
                x = ((DHPrivateKey) dhKeyPair.getPrivate()).getX();
                e = ((DHPublicKey) dhKeyPair.getPublic()).getY();
            } catch (InvalidKeyException ex) {
                throw new AlgorithmOperationException("Failed to generate DH value");
            } catch (InvalidAlgorithmParameterException ex) {
                throw new AlgorithmOperationException("Failed to generate DH value");
            }
            log.info("Generating shared context with server...");
            GlobusGSSManagerImpl globusgssmanagerimpl = new GlobusGSSManagerImpl();
            HostAuthorization gssAuth = new HostAuthorization(null);
            GSSName targetName = gssAuth.getExpectedName(null, hostname);
            GSSCredential gsscredential = null;
            if (theCredential == null) {
                gsscredential = UserGridCredential.getUserCredential(properties);
                theCredential = gsscredential;
            } else {
                gsscredential = theCredential;
                try {
                    ((GlobusGSSCredentialImpl) gsscredential).getGlobusCredential().verify();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                } catch (GlobusCredentialException e) {
                    e.printStackTrace();
                    javax.swing.JOptionPane.showMessageDialog(properties.getWindow(), "The credentials that you authenticated with have expired, please re-authenticate.", "GSI-SSH Terminal", javax.swing.JOptionPane.WARNING_MESSAGE);
                    gsscredential = UserGridCredential.getUserCredential(properties);
                    theCredential = gsscredential;
                }
            }
            GSSContext gsscontext = globusgssmanagerimpl.createContext(targetName, GSSConstants.MECH_OID, gsscredential, GSSCredential.DEFAULT_LIFETIME);
            gsscontext.requestCredDeleg(true);
            gsscontext.requestMutualAuth(true);
            gsscontext.requestInteg(true);
            Object type = GSIConstants.DELEGATION_TYPE_LIMITED;
            String cur = "None";
            if (properties instanceof SshToolsConnectionProfile) {
                cur = ((SshToolsConnectionProfile) properties).getApplicationProperty(SshTerminalPanel.PREF_DELEGATION_TYPE, "Full");
                if (cur.equals("Full")) {
                    type = GSIConstants.DELEGATION_TYPE_FULL;
                } else if (cur.equals("Limited")) {
                    type = GSIConstants.DELEGATION_TYPE_LIMITED;
                } else if (cur.equals("None")) {
                    type = GSIConstants.DELEGATION_TYPE_LIMITED;
                    gsscontext.requestCredDeleg(false);
                }
            }
            log.debug("Enabling delegation setting: " + cur);
            ((ExtendedGSSContext) gsscontext).setOption(GSSConstants.DELEGATION_TYPE, type);
            log.debug("Starting GSS token exchange.");
            byte abyte2[] = new byte[0];
            Object obj = null;
            boolean firsttime = true;
            hostKey = null;
            do {
                if (gsscontext.isEstablished()) break;
                byte abyte3[] = gsscontext.initSecContext(abyte2, 0, abyte2.length);
                if (gsscontext.isEstablished() && !gsscontext.getMutualAuthState()) {
                    throw new KeyExchangeException("Context established without mutual authentication in gss-group1-sha1-* key exchange.");
                }
                if (gsscontext.isEstablished() && !gsscontext.getIntegState()) {
                    throw new KeyExchangeException("Context established without integrety protection in gss-group1-sha1-* key exchange.");
                }
                if (abyte3 != null) {
                    if (firsttime) {
                        SshMsgKexGssInit msg = new SshMsgKexGssInit(e, abyte3);
                        transport.sendMessage(msg, this);
                    } else {
                        SshMsgKexGssContinue msg = new SshMsgKexGssContinue(abyte3);
                        transport.sendMessage(msg, this);
                    }
                } else {
                    throw new KeyExchangeException("Expecting a non-zero length token from GSS_Init_sec_context.");
                }
                if (!gsscontext.isEstablished()) {
                    int[] messageId = new int[3];
                    messageId[0] = SshMsgKexGssHostKey.SSH_MSG_KEXGSS_HOSTKEY;
                    messageId[1] = SshMsgKexGssContinue.SSH_MSG_KEXGSS_CONTINUE;
                    messageId[2] = SshMsgKexGssError.SSH_MSG_KEXGSS_ERROR;
                    SshMessage msg = transport.readMessage(messageId);
                    if (msg.getMessageId() == SshMsgKexGssHostKey.SSH_MSG_KEXGSS_HOSTKEY) {
                        if (!firsttime) {
                            throw new KeyExchangeException("Not expecting a SSH_MSG_KEXGS_HOSTKEY message at this time.");
                        }
                        SshMsgKexGssHostKey reply = (SshMsgKexGssHostKey) msg;
                        hostKey = reply.getHostKey();
                        messageId = new int[2];
                        messageId[0] = SshMsgKexGssContinue.SSH_MSG_KEXGSS_CONTINUE;
                        messageId[1] = SshMsgKexGssError.SSH_MSG_KEXGSS_ERROR;
                        msg = transport.readMessage(messageId);
                        if (msg.getMessageId() == SshMsgKexGssError.SSH_MSG_KEXGSS_ERROR) errormsg(msg);
                    } else if (msg.getMessageId() == SshMsgKexGssError.SSH_MSG_KEXGSS_ERROR) {
                        errormsg(msg);
                    }
                    SshMsgKexGssContinue reply = (SshMsgKexGssContinue) msg;
                    abyte2 = reply.getToken();
                }
                firsttime = false;
            } while (true);
            log.debug("Sending gssapi exchange complete.");
            int[] messageId = new int[2];
            messageId[0] = SshMsgKexGssComplete.SSH_MSG_KEXGSS_COMPLETE;
            messageId[1] = SshMsgKexGssError.SSH_MSG_KEXGSS_ERROR;
            SshMessage msg = transport.readMessage(messageId);
            if (msg.getMessageId() == SshMsgKexGssError.SSH_MSG_KEXGSS_ERROR) errormsg(msg);
            SshMsgKexGssComplete reply = (SshMsgKexGssComplete) msg;
            if (reply.hasToken()) {
                ByteArrayReader bytearrayreader1 = new ByteArrayReader(reply.getToken());
                abyte2 = bytearrayreader1.readBinaryString();
                byte abyte3[] = gsscontext.initSecContext(abyte2, 0, abyte2.length);
                if (abyte3 != null) {
                    throw new KeyExchangeException("Expecting zero length token.");
                }
                if (gsscontext.isEstablished() && !gsscontext.getMutualAuthState()) {
                    throw new KeyExchangeException("Context established without mutual authentication in gss-group1-sha1-* key exchange.");
                }
                if (gsscontext.isEstablished() && !gsscontext.getIntegState()) {
                    throw new KeyExchangeException("Context established without integrety protection in gss-group1-sha1-* key exchange.");
                }
            }
            byte per_msg_token[] = reply.getMIC();
            f = reply.getF();
            secret = f.modPow(x, p);
            calculateExchangeHash();
            gsscontext.verifyMIC(per_msg_token, 0, per_msg_token.length, exchangeHash, 0, exchangeHash.length, null);
            gssContext = gsscontext;
        } catch (GSSException g) {
            String desc = g.toString();
            if (desc.startsWith("GSSException: Failure unspecified at GSS-API level (Mechanism level: GSS Major Status: Authentication Failed") && desc.indexOf("an unknown error occurred") >= 0) {
                throw new KeyExchangeException("Error from GSS layer: Probably due to your proxy credential being expired or signed by a CA unknown by the server or your clock being set wrong.\n(" + g.toString().replaceAll("\n", " ") + ")");
            } else {
                throw new KeyExchangeException("Error from GSS layer: \n" + g.toString().replaceAll("\n", " "));
            }
        }
    }
