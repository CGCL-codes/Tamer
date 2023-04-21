    protected HerbivoreClique(Herbivore herb, String name, String bootstrapHost, int bootstrapPort, int bootstrapControlPort, int controlport, short transmissionLength, int backOffStep) throws IOException {
        boolean notamember = true;
        this.herbivore = herb;
        this.name = name;
        this.transmissionLength = transmissionLength;
        this.backOffStep = backOffStep;
        this.localHostName = InetAddress.getLocalHost().getHostName();
        this.cliqueID = new HerbivoreCliqueID(name);
        this.knownHostsBySocket = new Hashtable();
        this.knownHostsByLocation = new Hashtable();
        this.outgoingPackets = new HerbivoreQueue(EXPECTEDCLIQUESIZE, false);
        this.serverSockets = new Hashtable();
        this.members = new Vector();
        Vector hosts = new Vector();
        hc = HerbivoreChallenge.compute(localHostName + ":" + controlport + ":" + name, PUZZLE_DIFFICULTY);
        boolean firstnode = false;
        int pastrybootstrapbackoff = 1000;
        while (true) {
            try {
                Socket s = new Socket(bootstrapHost, bootstrapPort);
                s.close();
                break;
            } catch (Exception e) {
                if (localHostName.equals(bootstrapHost)) {
                    firstnode = true;
                    break;
                } else {
                    try {
                        Log.info("Waiting for bootstrapHost " + bootstrapHost + ":" + bootstrapPort + " to boot up.");
                        Thread.sleep(pastrybootstrapbackoff);
                        if (pastrybootstrapbackoff < 5000) pastrybootstrapbackoff += 1000;
                    } catch (InterruptedException ie) {
                        Log.exception(ie);
                    }
                }
            }
        }
        if (firstnode) {
            if (DEBUG) Log.info("The first clique is " + HerbivoreUtil.createHexStringFromBytes(hc.location()));
            hpn = new HerbivorePastryNode(bootstrapHost, bootstrapPort, hc);
            hosts.add(getMe(controlport, hc));
            members.add(curepoch, hosts);
            hccl = new HerbivoreCliqueControlLoop(hpn.pn, this, controlport);
            (new cliqueLoop(this)).start();
            return;
        }
        while (notamember) {
            int phase = 1;
            try {
                getCliqueMembershipList(bootstrapHost, bootstrapControlPort, hosts);
                phase++;
                for (int i = 0; i < hosts.size(); ++i) {
                    HerbivoreHostDescriptor hde = (HerbivoreHostDescriptor) hosts.elementAt(i);
                    if (DEBUG) Log.info("Connecting to " + hde.hostname + " " + hde.controlport);
                    HerbivoreConnection newconn = null;
                    ;
                    while (true) {
                        try {
                            newconn = new HerbivoreConnection(hde);
                            break;
                        } catch (ConnectException ce) {
                            if (newconn != null) {
                                newconn.close();
                                newconn = null;
                            }
                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException ie) {
                            }
                        }
                    }
                    hde.setControlConnection(newconn);
                    newconn = null;
                    while (true) {
                        try {
                            newconn = new HerbivoreConnection(hde);
                            break;
                        } catch (ConnectException ce) {
                            if (newconn != null) {
                                newconn.close();
                                newconn = null;
                            }
                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException ie) {
                            }
                        }
                    }
                    hde.setDataConnection(newconn);
                }
                phase++;
                byte[] challenge = new byte[CHALLENGE_LENGTH];
                herbivore.random.nextBytes(challenge);
                writeAll(hosts, false, "INITCONNECTION \"" + InetAddress.getLocalHost().getHostName() + "\" " + controlport + " \"" + HerbivoreUtil.createHexStringFromBytes(hc.getKeyPair().getPublicKey()) + "\" \"" + HerbivoreUtil.createHexStringFromBytes(hc.getY()) + "\" \"" + HerbivoreUtil.createHexStringFromBytes(challenge) + "\" " + "\n");
                for (int i = 0; i < hosts.size(); ++i) {
                    HerbivoreHostDescriptor hde = (HerbivoreHostDescriptor) hosts.elementAt(i);
                    HerbivoreConnection conn = hde.dataconn;
                    eat200OK(conn);
                    eat200(conn);
                    byte[] encryptedresponse = conn.readBytes();
                    Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS#7", "Cryptix");
                    cipher.initDecrypt(new RawRSAPrivateKey(new ByteArrayInputStream(hc.getKeyPair().getPrivateKey())));
                    byte[] decryptedresponse = cipher.crypt(encryptedresponse);
                    ByteArrayInputStream bis = new ByteArrayInputStream(decryptedresponse);
                    ObjectInputStream ois = new ObjectInputStream(bis);
                    HerbivoreCliqueJoinResponse hcjr = (HerbivoreCliqueJoinResponse) ois.readObject();
                    Signature sig = Signature.getInstance("SHA-1/RSA", "Cryptix");
                    sig.initVerify(new RawRSAPublicKey(new ByteArrayInputStream(hde.publicKey)));
                    sig.update(challenge);
                    if (!sig.verify(hcjr.signedchallenge)) {
                        throw new Exception("Peer authentication failure - caught an impostor peer who could not meet my challenge");
                    } else {
                        if (DEBUG) Log.info("Peer " + hde.hostname + " authenticated");
                    }
                    if (!HerbivoreChallenge.check(hde.location, hcjr.publickey, hcjr.puzzlekey, PUZZLE_DIFFICULTY)) throw new Exception("Peer authentication failure - caught an impostor peer connected at an address for which it lacks credentials");
                    MessageDigest sha = MessageDigest.getInstance("SHA-1");
                    sha.update(hcjr.seed);
                    byte[] hash = sha.digest();
                    hde.setSeed(hcjr.seed);
                    conn.writeStr("SEEDSETUP \"" + HerbivoreUtil.createHexStringFromBytes(hash) + "\"");
                    eat200OK(conn);
                }
                phase++;
                writeAll(hosts, true, "ADDME \"" + HerbivoreUtil.createHexStringFromBytes(hc.location()) + "\" " + "\n");
                readAll(hosts, true);
                phase++;
                int newepoch = -1;
                for (int i = 0; i < hosts.size(); ++i) {
                    HerbivoreHostDescriptor hde = (HerbivoreHostDescriptor) hosts.elementAt(i);
                    HerbivoreConnection conn = hde.controlconn;
                    int suggestedepoch = conn.readInt();
                    if (newepoch < 0) newepoch = suggestedepoch;
                    if (newepoch != suggestedepoch) throw new Exception("Join failure - clique disagrees about the current epoch" + newepoch + " " + suggestedepoch);
                }
                if (newepoch < 0) throw new Exception("Join failure - no epoch found");
                curepoch = newepoch;
                if (DEBUG) Log.info("Clique epoch is ..." + curepoch);
                hosts.add(getMe(controlport, hc));
                computeCliqueMembers(hosts);
                Log.info("New clique membership is...");
                dumpMembers(hosts);
                if (members.size() < curepoch) {
                    for (int i = 0; i < curepoch; ++i) members.add(i, null);
                }
                members.add(curepoch, hosts);
                notamember = false;
            } catch (Exception e) {
                Log.exception(e);
                System.exit(1);
                if (phase == 1) {
                    Log.error("Cannot connect to the bootstrap node\n");
                    Log.exception(e);
                    System.exit(-1);
                }
                if (phase > 1) {
                    Log.error("Cannot connect to all members of the clique\n");
                    Log.exception(e);
                    try {
                        for (int i = 0; i < hosts.size(); ++i) {
                            HerbivoreHostDescriptor hde = (HerbivoreHostDescriptor) hosts.elementAt(i);
                            if (hde != null && hde.dataconn != null) {
                                hde.dataconn.close();
                                hde.dataconn = null;
                            }
                            if (hde != null && hde.controlconn != null) {
                                hde.controlconn.close();
                                hde.controlconn = null;
                            }
                        }
                        hosts.clear();
                    } catch (Exception e2) {
                    }
                }
            }
        }
        hpn = new HerbivorePastryNode(bootstrapHost, bootstrapPort, hc);
        hccl = new HerbivoreCliqueControlLoop(hpn.pn, this, controlport);
        (new cliqueLoop(this)).start();
    }
