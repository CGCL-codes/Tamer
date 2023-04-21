        public void processRequest(ObjectOutputStream oos, ObjectInputStream ois, Socket socket, ServerRequest sr) throws Exception {
            ObjectOutputStream to_njs_oos = null;
            ObjectInputStream from_njs_ois = null;
            Object from_njs = null;
            String xlogin = null;
            Reply reply = new Reply();
            RetrieveOutcomeReply ror = new RetrieveOutcomeReply();
            if (!(sr instanceof ConsignJob)) {
                UnicoreResponse response = new UnicoreResponse(-1, "Wrong Request!");
                ror.addTraceEntry(response);
                oos.writeObject(ror);
            } else if (!(sr.getTarget().getName().startsWith("SSH"))) {
                ConsignForm.AJO cfa = new ConsignForm.AJO((ConsignJob) sr);
                AbstractJob srcAJO = cfa.getAJO();
                AbstractJob_Outcome outcome = new AbstractJob_Outcome(srcAJO, AbstractActionStatus.SUCCESSFUL);
                Enumeration actions = srcAJO.getActions();
                while (actions.hasMoreElements()) {
                    Object el = actions.nextElement();
                    if (el instanceof GetResourceDescription) {
                        logger.info("\nIA-Gateway resource description requested---> " + ((AbstractAction) el).getName() + ": " + ((AbstractAction) el).getId());
                        GetResourceDescription_Outcome grd_outcome = new GetResourceDescription_Outcome((GetResourceDescription) el, AbstractActionStatus.SUCCESSFUL);
                        ResourceSet rs = new ResourceSet();
                        rs.add(new TextInfoResource("This site provides IA SSH connection to ", "target host", target_address.getCanonicalHostName()));
                        grd_outcome.setResources(rs);
                        outcome.addOutcome(grd_outcome);
                    }
                    if (el instanceof GetJobs) {
                        logger.info("\nIA-Gateway job list requested---> " + ((AbstractAction) el).getName() + ": " + ((AbstractAction) el).getId());
                        GetJobs_Outcome gj_outcome = new GetJobs_Outcome((GetJobs) el, AbstractActionStatus.NOT_SUCCESSFUL);
                        gj_outcome.setStatus(AbstractActionStatus.NOT_SUCCESSFUL, "This site provides only IA-SSH services!");
                        outcome.addOutcome(gj_outcome);
                    }
                }
                logger.info("\nResource request finished");
                UnicoreResponse response = new UnicoreResponse(0, "This is a response to a get resource description");
                ror.addTraceEntry(response);
                outcome = ConsignForm.convertTo(outcome);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream obs = new ObjectOutputStream(bos);
                obs.writeObject(outcome);
                obs.close();
                ror.setOutcome(bos.toByteArray());
                oos.writeObject(ror);
                logger.info("\nResources returned to the client");
            } else {
                try {
                    String term_settings = sr.getTarget().getName();
                    logger.info("Read from the client: " + term_settings);
                    StringTokenizer st = new StringTokenizer(term_settings);
                    String term_type = st.nextToken();
                    term_type = st.nextToken();
                    term_settings = st.nextToken();
                    st = new StringTokenizer(term_settings, "x");
                    int term_cols = (new Integer(st.nextToken())).intValue();
                    int term_rows = (new Integer(st.nextToken())).intValue();
                    logger.info("terminal_type=" + term_type + " rows=" + term_rows + " cols=" + term_cols);
                    SshKeyPair keys = SshKeyPairFactory.newInstance("ssh-rsa");
                    keys.generate(1024);
                    SshPrivateKey privatekey = keys.getPrivateKey();
                    SshPublicKey publickey = keys.getPublicKey();
                    SshPublicKeyFile keyfile = SshPublicKeyFile.create(publickey, new OpenSSHPublicKeyFormat("unicore-tmp-key"));
                    String public_line = keyfile.toString();
                    public_line = "from=\"" + System.getProperty("gw.gateway_host_name") + "\" " + public_line;
                    to_njs_oos = new ObjectOutputStream(to_njs.getOutputStream());
                    to_njs_oos.writeObject(sr);
                    to_njs_oos.writeObject(public_line);
                    to_njs_oos.flush();
                    from_njs_ois = new ObjectInputStream(to_njs.getInputStream());
                    reply = (Reply) from_njs_ois.readObject();
                    UnicoreResponse response = reply.getLastEntry();
                    int hdr = response.getReturnCode();
                    logger.info("Read from the NJS: " + response.getComment() + " ;return code: " + hdr);
                    if (hdr == 0) {
                        String comment = response.getComment();
                        xlogin = comment.substring(comment.lastIndexOf(":") + 2);
                        SshClient ssh = new SshClient();
                        ssh.connect(target_address.getHostName(), target_port, new IgnoreHostKeyVerification());
                        logger.info("Connected to: " + target_address.getHostName() + " on port: " + target_port);
                        PublicKeyAuthenticationClient pk = new PublicKeyAuthenticationClient();
                        pk.setUsername(xlogin);
                        pk.setKey(privatekey);
                        int result = ssh.authenticate(pk);
                        if (result == AuthenticationProtocolState.COMPLETE) {
                            SessionChannelClient session = ssh.openSessionChannel();
                            if (!session.requestPseudoTerminal(term_type, term_cols, term_rows, 0, 0, "")) {
                                oos.writeObject(generateErrorReply("Couldn't start the terminal"));
                                throw new Exception("Couldn't start the terminal");
                            }
                            if (session.startShell()) {
                                oos.writeObject(reply);
                                to_njs_oos.writeObject(new String("CLEAR KEYS"));
                                to_njs_oos.flush();
                                to_njs_oos.close();
                                InputStream in = session.getInputStream();
                                OutputStream out = session.getOutputStream();
                                IOStreamConnector input = new IOStreamConnector(ois, session.getOutputStream());
                                IOStreamConnector output = new IOStreamConnector(session.getInputStream(), oos);
                                IOStreamConnector err = new IOStreamConnector(session.getStderrInputStream(), oos);
                                output.getState().waitForState(IOStreamConnectorState.CLOSED);
                                ssh.disconnect();
                            } else {
                                oos.writeObject(generateErrorReply("Couldn't start the shell"));
                                throw new Exception("Couldn't start the shell");
                            }
                        }
                        if (result == AuthenticationProtocolState.PARTIAL) {
                            oos.writeObject(generateErrorReply("Further authentication requried!"));
                        }
                        if (result == AuthenticationProtocolState.FAILED) {
                            oos.writeObject(generateErrorReply("Authentication failed!"));
                            throw new Exception("Authentication failed!");
                        }
                    } else {
                        oos.writeObject(from_njs_ois.readObject());
                    }
                } finally {
                    try {
                        to_njs_oos.close();
                        from_njs_ois.close();
                        socket.close();
                    } catch (Exception ex) {
                    }
                }
            }
        }
