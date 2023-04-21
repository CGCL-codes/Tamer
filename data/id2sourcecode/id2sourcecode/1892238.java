    public void loop() {
        if (DEBUG) Log.info("CliqueControlLoop started for " + controlsocket);
        try {
            while (true) {
                Socket s = controlsocket.accept();
                HerbivoreConnection client = new HerbivoreConnection(s);
                boolean thereismore = true;
                boolean closesocket = false;
                while (thereismore) {
                    String command = client.readStr();
                    if (DEBUG) Log.info("Command is " + command + ", state=" + state);
                    if (command.equals("DEBUGDIE")) {
                        System.exit(0);
                    }
                    if (command.equals("QUIT")) {
                        thereismore = false;
                        closesocket = true;
                        continue;
                    }
                    if (command.equals("GETCLIQUELIST")) {
                        synchronized (clique) {
                            if (state != READY || clique.curepoch != clique.nextepoch) {
                                threads.add(new HerbivoreCliqueControlLoopThread(this));
                                while (state != READY || clique.curepoch != clique.nextepoch) {
                                    if (DEBUG) Log.info("Client going to sleep " + state + " " + clique.curepoch + " " + clique.nextepoch);
                                    clique.wait();
                                }
                            }
                            state = JOIN_IN_PROGRESS;
                            try {
                                byte[] newloc;
                                client.writeStr("200 OK\n");
                                String loc = client.readStr();
                                if (DEBUG) Log.info("Node wants to join at " + loc);
                                newloc = HerbivoreUtil.readBytesFromHexString(loc);
                                Vector ve = lookupCliqueMembers(newloc);
                                client.writeStr("200 " + ve.size() + " " + clique.curepoch + "\n");
                                Enumeration e = ve.elements();
                                while (e.hasMoreElements()) {
                                    HerbivoreSimpleHostDescriptor hde = (HerbivoreSimpleHostDescriptor) e.nextElement();
                                    byte[] ipa = hde.ipaddr.getAddress();
                                    client.writeStr("200 " + hde.hostname + " \"" + getIPdigit((int) ipa[0]) + "." + getIPdigit((int) ipa[1]) + "." + getIPdigit((int) ipa[2]) + "." + getIPdigit((int) ipa[3]) + "\" " + hde.controlport + " \"" + HerbivoreUtil.createHexStringFromBytes(hde.publicKey) + "\" \"" + HerbivoreUtil.createHexStringFromBytes(hde.puzzleKey) + "\" \"" + HerbivoreUtil.createHexStringFromBytes(hde.location) + "\"\n");
                                }
                                thereismore = false;
                            } catch (Exception e) {
                                Log.exception(e);
                                thereismore = false;
                            }
                        }
                        continue;
                    }
                    if (command.equals("INITCONNECTION")) {
                        joinername = client.readStr();
                        joinerport = client.readInt();
                        joinerkey = client.readBytes();
                        joinerpuzzle = client.readBytes();
                        byte[] challenge = client.readBytes();
                        if (DEBUG) Log.info("Node \"" + joinername + ":" + joinerport + "\" wants to connect");
                        client.writeStr("200 OK\n");
                        client.writeStr("200 ");
                        HerbivoreCliqueJoinResponse hcjr;
                        hcjr = new HerbivoreCliqueJoinResponse();
                        hcjr.publickey = clique.hc.getKeyPair().getPublicKey();
                        hcjr.puzzlekey = clique.hc.getY();
                        Signature sig = Signature.getInstance("SHA-1/RSA", "Cryptix");
                        sig.initSign(new RawRSAPrivateKey(new ByteArrayInputStream(clique.hc.getKeyPair().getPrivateKey())));
                        sig.update(challenge);
                        hcjr.signedchallenge = sig.sign();
                        hcjr.seed = new byte[HerbivoreClique.INITIAL_SEED_SIZE];
                        clique.herbivore.random.nextBytes(challenge);
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        ObjectOutputStream oos = new ObjectOutputStream(bos);
                        oos.writeObject(hcjr);
                        byte[] objbytes = bos.toByteArray();
                        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS#7", "Cryptix");
                        cipher.initEncrypt(new RawRSAPublicKey(new ByteArrayInputStream(joinerkey)));
                        byte[] encryptedresponse = cipher.crypt(objbytes);
                        HerbivoreHostDescriptor hde = new HerbivoreHostDescriptor(joinername, client.getRemoteIP(), joinerport);
                        hde.setDataConnection(client);
                        hde.setSeed(hcjr.seed);
                        hde.setPublicKey(joinerkey);
                        hde.setPuzzleKey(joinerpuzzle);
                        hde.setLocation(HerbivoreChallenge.computeLocation(joinerkey, joinerpuzzle));
                        synchronized (clique) {
                            clique.knownHostsBySocket.put(s, hde);
                            clique.knownHostsByLocation.put(new BigInteger(hde.location), hde);
                        }
                        if (DEBUG) System.out.println("Inserted element at " + hde.location);
                        client.writeBytes(encryptedresponse);
                        client.writeStr("\n");
                        continue;
                    }
                    if (command.equals("SEEDSETUP")) {
                        synchronized (clique) {
                            byte[] seedhash = client.readBytes();
                            HerbivoreHostDescriptor hde = (HerbivoreHostDescriptor) clique.knownHostsBySocket.get(s);
                            if (hde == null) {
                                throw new Exception("Host wants to join without initialization");
                            }
                            MessageDigest sha = MessageDigest.getInstance("SHA-1");
                            sha.update(hde.seedvalue);
                            byte[] hash = sha.digest();
                            if (seedhash == null || hash.length != seedhash.length) throw new Exception("Peer authentication failure - seed hash is of wrong length");
                            for (int i = 0; i < hash.length; ++i) if (hash[i] != seedhash[i]) throw new Exception("Peer authentication failure - seed hash is incorrect");
                            hde.authenticated();
                            if (DEBUG) Log.info("Peer " + hde.hostname + " authenticated");
                            client.writeStr("200 OK\n");
                            thereismore = false;
                            continue;
                        }
                    }
                    if (command.equals("PREPARETOCOMMIT")) {
                        continue;
                    }
                    if (command.equals("ABORT")) {
                        continue;
                    }
                    if (command.equals("COMMIT")) {
                        continue;
                    }
                    if (command.equals("ADDME")) {
                        byte[] location = client.readBytes();
                        synchronized (clique) {
                            HerbivoreHostDescriptor hde = (HerbivoreHostDescriptor) clique.knownHostsByLocation.get(new BigInteger(location));
                            if (hde != null && hde.isAuthenticated()) {
                                if (DEBUG) Log.info("Adding to clique " + hde.hostname + ":" + hde.controlport + "");
                                synchronized (clique) {
                                    clique.nextepoch++;
                                    Vector oldhostlist = (Vector) clique.members.elementAt(clique.curepoch);
                                    Vector newhostlist = (Vector) oldhostlist.clone();
                                    newhostlist.add(hde);
                                    HerbivoreClique.computeCliqueMembers(newhostlist);
                                    clique.members.add(clique.nextepoch, newhostlist);
                                    state = READY;
                                    clique.notifyAll();
                                }
                                client.writeStr("200 OK " + clique.nextepoch + "\n");
                                thereismore = false;
                                closesocket = true;
                                continue;
                            }
                        }
                    }
                }
                if (closesocket) {
                    Log.info("Closing socket to " + client);
                    client.close();
                }
            }
        } catch (SocketException se) {
            Log.exception(se);
        } catch (Exception e) {
            Log.exception(e);
            System.exit(-1);
        }
    }
