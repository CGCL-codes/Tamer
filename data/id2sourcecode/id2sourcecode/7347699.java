    @Override
    protected boolean sendLoginPackets(Task connect) throws Exception {
        while (isConnected() && !socket.isClosed() && !disposed) {
            if (bncsInputStream.available() <= 0) {
                sleep(200);
            } else {
                BNCSPacketReader pr = new BNCSPacketReader(bncsInputStream);
                BNetInputStream is = pr.getData();
                switch(pr.packetId) {
                    case SID_OPTIONALWORK:
                    case SID_EXTRAWORK:
                    case SID_REQUIREDWORK:
                        break;
                    case SID_NULL:
                        {
                            BNCSPacket p = new BNCSPacket(this, BNCSPacketId.SID_NULL);
                            p.sendPacket(bncsOutputStream);
                            break;
                        }
                    case SID_PING:
                        {
                            BNCSPacket p = new BNCSPacket(this, BNCSPacketId.SID_PING);
                            p.writeDWord(is.readDWord());
                            p.sendPacket(bncsOutputStream);
                            break;
                        }
                    case SID_AUTH_INFO:
                    case SID_STARTVERSIONING:
                        {
                            if (pr.packetId == BNCSPacketId.SID_AUTH_INFO) {
                                nlsRevision = is.readDWord();
                                serverToken = is.readDWord();
                                is.skip(4);
                            }
                            long mpqFileTime = is.readQWord();
                            String mpqFileName = is.readNTString();
                            byte[] valueStr = is.readNTBytes();
                            Out.debug(getClass(), "MPQ: " + mpqFileName);
                            byte extraData[] = null;
                            if (is.available() == 0x80) {
                                extraData = new byte[0x80];
                                is.read(extraData, 0, 0x80);
                            }
                            assert (is.available() == 0);
                            byte keyHash[] = null;
                            byte keyHash2[] = null;
                            if (nlsRevision != null) {
                                keyHash = HashMain.hashKey(clientToken, serverToken, cs.cdkey).getBuffer();
                                if ((productID == ProductIDs.D2XP) || (productID == ProductIDs.W3XP)) keyHash2 = HashMain.hashKey(clientToken, serverToken, cs.cdkey2).getBuffer();
                                warden = null;
                                warden_seed = new byte[4];
                                System.arraycopy(keyHash, 16, warden_seed, 0, 4);
                            }
                            Task task = createTask("BNLS_VERSIONCHECKEX2", "...");
                            VersionCheckResult vcr = BNLSManager.sendVersionCheckEx2(task, productID, mpqFileTime, mpqFileName, valueStr);
                            completeTask(task);
                            if (vcr == null) {
                                dispatchRecieveError("CheckRevision failed.");
                                disconnect(ConnectionState.LONG_PAUSE_BEFORE_CONNECT);
                                break;
                            }
                            if (nlsRevision != null) {
                                connect.updateProgress("CheckRevision/CD Key challenge");
                                BNCSPacket p = new BNCSPacket(this, BNCSPacketId.SID_AUTH_CHECK);
                                p.writeDWord(clientToken);
                                p.writeDWord(vcr.exeVersion);
                                p.writeDWord(vcr.exeHash);
                                if (keyHash2 == null) p.writeDWord(1); else p.writeDWord(2);
                                p.writeDWord(0);
                                if (keyHash.length != 36) throw new Exception("Invalid keyHash length");
                                p.write(keyHash);
                                if (keyHash2 != null) {
                                    if (keyHash2.length != 36) throw new Exception("Invalid keyHash2 length");
                                    p.write(keyHash2);
                                }
                                p.writeNTString(vcr.exeInfo);
                                p.writeNTString(cs.username);
                                p.sendPacket(bncsOutputStream);
                            } else {
                                connect.updateProgress("CheckRevision");
                                BNCSPacket p = new BNCSPacket(this, BNCSPacketId.SID_REPORTVERSION);
                                p.writeDWord(PlatformIDs.PLATFORM_IX86);
                                p.writeDWord(productID.getDword());
                                p.writeDWord(verByte);
                                p.writeDWord(vcr.exeVersion);
                                p.writeDWord(vcr.exeHash);
                                p.writeNTString(vcr.exeInfo);
                                p.sendPacket(bncsOutputStream);
                            }
                            break;
                        }
                    case SID_REPORTVERSION:
                    case SID_AUTH_CHECK:
                        {
                            int result = is.readDWord();
                            String extraInfo = is.readNTString();
                            assert (is.available() == 0);
                            if (pr.packetId == BNCSPacketId.SID_AUTH_CHECK) {
                                if (result != 0) {
                                    switch(result) {
                                        case 0x0100:
                                            dispatchRecieveError("Update required: " + extraInfo);
                                            BNFTPConnection.downloadFile(extraInfo);
                                            break;
                                        case 0x0101:
                                            dispatchRecieveError("Invalid version.");
                                            break;
                                        case 0x102:
                                            dispatchRecieveError("Game version must be downgraded: " + extraInfo);
                                            break;
                                        case 0x200:
                                            dispatchRecieveError("Invalid CD key.");
                                            break;
                                        case 0x201:
                                            dispatchRecieveError("CD key in use by " + extraInfo);
                                            break;
                                        case 0x202:
                                            dispatchRecieveError("Banned key.");
                                            break;
                                        case 0x203:
                                            dispatchRecieveError("Wrong product for CD key.");
                                            break;
                                        case 0x210:
                                            dispatchRecieveError("Invalid second CD key.");
                                            break;
                                        case 0x211:
                                            dispatchRecieveError("Second CD key in use by " + extraInfo);
                                            break;
                                        case 0x212:
                                            dispatchRecieveError("Banned second key.");
                                            break;
                                        case 0x213:
                                            dispatchRecieveError("Wrong product for second CD key.");
                                            break;
                                        default:
                                            dispatchRecieveError("Unknown SID_AUTH_CHECK result 0x" + Integer.toHexString(result));
                                            break;
                                    }
                                    disconnect(ConnectionState.LONG_PAUSE_BEFORE_CONNECT);
                                    break;
                                }
                                dispatchRecieveInfo("Passed CD key challenge and CheckRevision.");
                            } else {
                                if (result != 2) {
                                    switch(result) {
                                        case 0:
                                            dispatchRecieveError("Failed version check.");
                                            break;
                                        case 1:
                                            dispatchRecieveError("Old game version.");
                                            break;
                                        case 3:
                                            dispatchRecieveError("Reinstall required.");
                                            break;
                                        default:
                                            dispatchRecieveError("Unknown SID_REPORTVERSION result 0x" + Integer.toHexString(result));
                                            break;
                                    }
                                    disconnect(ConnectionState.LONG_PAUSE_BEFORE_CONNECT);
                                    break;
                                }
                                dispatchRecieveInfo("Passed CheckRevision.");
                            }
                            connect.updateProgress("Logging in");
                            sendKeyOrPassword();
                            break;
                        }
                    case SID_CDKEY:
                    case SID_CDKEY2:
                        {
                            int result = is.readDWord();
                            String keyOwner = is.readNTString();
                            if (result != 1) {
                                switch(result) {
                                    case 0x02:
                                        dispatchRecieveError("Invalid CD key.");
                                        break;
                                    case 0x03:
                                        dispatchRecieveError("Bad CD key product.");
                                        break;
                                    case 0x04:
                                        dispatchRecieveError("CD key banned.");
                                        break;
                                    case 0x05:
                                        dispatchRecieveError("CD key in use by " + keyOwner);
                                        break;
                                    default:
                                        dispatchRecieveError("Unknown SID_CDKEY response 0x" + Integer.toHexString(result));
                                        break;
                                }
                                disconnect(ConnectionState.LONG_PAUSE_BEFORE_CONNECT);
                                break;
                            }
                            dispatchRecieveInfo("CD key accepted.");
                            connect.updateProgress("Logging in");
                            sendPassword();
                            break;
                        }
                    case SID_AUTH_ACCOUNTLOGON:
                        {
                            int status = is.readDWord();
                            switch(status) {
                                case 0x00:
                                    dispatchRecieveInfo("Login accepted; requires proof.");
                                    connect.updateProgress("Login accepted; proving");
                                    break;
                                case 0x01:
                                    dispatchRecieveError("Account doesn't exist; creating...");
                                    connect.updateProgress("Creating account");
                                    if (srp == null) {
                                        dispatchRecieveError("SRP is not initialized!");
                                        disconnect(ConnectionState.LONG_PAUSE_BEFORE_CONNECT);
                                        break;
                                    }
                                    byte[] salt = new byte[32];
                                    new Random().nextBytes(salt);
                                    byte[] verifier = srp.get_v(salt).toByteArray();
                                    if (salt.length != 32) throw new Exception("Salt length wasn't 32!");
                                    if (verifier.length != 32) throw new Exception("Verifier length wasn't 32!");
                                    BNCSPacket p = new BNCSPacket(this, BNCSPacketId.SID_AUTH_ACCOUNTCREATE);
                                    p.write(salt);
                                    p.write(verifier);
                                    p.writeNTString(cs.username);
                                    p.sendPacket(bncsOutputStream);
                                    break;
                                case 0x05:
                                    dispatchRecieveError("Account requires upgrade");
                                    disconnect(ConnectionState.LONG_PAUSE_BEFORE_CONNECT);
                                    break;
                                default:
                                    dispatchRecieveError("Unknown SID_AUTH_ACCOUNTLOGON status 0x" + Integer.toHexString(status));
                                    disconnect(ConnectionState.LONG_PAUSE_BEFORE_CONNECT);
                                    break;
                            }
                            if (status != 0) break;
                            if (srp == null) {
                                dispatchRecieveError("SRP is not initialized!");
                                disconnect(ConnectionState.LONG_PAUSE_BEFORE_CONNECT);
                                break;
                            }
                            byte s[] = new byte[32];
                            byte B[] = new byte[32];
                            is.read(s, 0, 32);
                            is.read(B, 0, 32);
                            byte M1[] = srp.getM1(s, B);
                            proof_M2 = srp.getM2(s, B);
                            if (M1.length != 20) throw new Exception("Invalid M1 length");
                            BNCSPacket p = new BNCSPacket(this, BNCSPacketId.SID_AUTH_ACCOUNTLOGONPROOF);
                            p.write(M1);
                            p.sendPacket(bncsOutputStream);
                            break;
                        }
                    case SID_AUTH_ACCOUNTCREATE:
                        {
                            int status = is.readDWord();
                            switch(status) {
                                case 0x00:
                                    dispatchRecieveInfo("Account created; logging in.");
                                    connect.updateProgress("Logging in");
                                    sendKeyOrPassword();
                                    break;
                                default:
                                    dispatchRecieveError("Create account failed with error code 0x" + Integer.toHexString(status));
                                    break;
                            }
                            break;
                        }
                    case SID_AUTH_ACCOUNTLOGONPROOF:
                        {
                            int status = is.readDWord();
                            byte server_M2[] = new byte[20];
                            is.read(server_M2, 0, 20);
                            String additionalInfo = null;
                            if (is.available() != 0) additionalInfo = is.readNTStringUTF8();
                            switch(status) {
                                case 0x00:
                                    break;
                                case 0x02:
                                    dispatchRecieveError("Incorrect password.");
                                    disconnect(ConnectionState.LONG_PAUSE_BEFORE_CONNECT);
                                    break;
                                case 0x0E:
                                    dispatchRecieveError("An email address should be registered for this account.");
                                    connect.updateProgress("Registering email address");
                                    sendSetEmail();
                                    break;
                                case 0x0F:
                                    dispatchRecieveError("Custom bnet error: " + additionalInfo);
                                    disconnect(ConnectionState.LONG_PAUSE_BEFORE_CONNECT);
                                    break;
                                default:
                                    dispatchRecieveError("Unknown SID_AUTH_ACCOUNTLOGONPROOF status: 0x" + Integer.toHexString(status));
                                    disconnect(ConnectionState.LONG_PAUSE_BEFORE_CONNECT);
                                    break;
                            }
                            if (!isConnected()) break;
                            for (int i = 0; i < 20; i++) {
                                if (server_M2[i] != proof_M2[i]) throw new Exception("Server couldn't prove password");
                            }
                            dispatchRecieveInfo("Login successful; entering chat.");
                            connect.updateProgress("Entering chat");
                            sendEnterChat();
                            break;
                        }
                    case SID_LOGONRESPONSE2:
                        {
                            int result = is.readDWord();
                            switch(result) {
                                case 0x00:
                                    dispatchRecieveInfo("Login successful; entering chat.");
                                    connect.updateProgress("Entering chat");
                                    sendEnterChat();
                                    sendGetChannelList();
                                    sendJoinChannel(cs.channel);
                                    break;
                                case 0x01:
                                    dispatchRecieveInfo("Account doesn't exist; creating...");
                                    connect.updateProgress("Creating account");
                                    int[] passwordHash = BrokenSHA1.calcHashBuffer(cs.password.toLowerCase().getBytes());
                                    BNCSPacket p = new BNCSPacket(this, BNCSPacketId.SID_CREATEACCOUNT2);
                                    p.writeDWord(passwordHash[0]);
                                    p.writeDWord(passwordHash[1]);
                                    p.writeDWord(passwordHash[2]);
                                    p.writeDWord(passwordHash[3]);
                                    p.writeDWord(passwordHash[4]);
                                    p.writeNTString(cs.username);
                                    p.sendPacket(bncsOutputStream);
                                    break;
                                case 0x02:
                                    dispatchRecieveError("Incorrect password.");
                                    disconnect(ConnectionState.LONG_PAUSE_BEFORE_CONNECT);
                                    break;
                                case 0x06:
                                    dispatchRecieveError("Your account is closed.");
                                    disconnect(ConnectionState.LONG_PAUSE_BEFORE_CONNECT);
                                    break;
                                default:
                                    dispatchRecieveError("Unknown SID_LOGONRESPONSE2 result 0x" + Integer.toHexString(result));
                                    disconnect(ConnectionState.LONG_PAUSE_BEFORE_CONNECT);
                                    break;
                            }
                            break;
                        }
                    case SID_CLIENTID:
                        {
                            break;
                        }
                    case SID_LOGONCHALLENGE:
                        {
                            serverToken = is.readDWord();
                            break;
                        }
                    case SID_LOGONCHALLENGEEX:
                        {
                            is.readDWord();
                            serverToken = is.readDWord();
                            break;
                        }
                    case SID_CREATEACCOUNT2:
                        {
                            int status = is.readDWord();
                            is.readNTString();
                            switch(status) {
                                case 0x00:
                                    dispatchRecieveInfo("Account created");
                                    connect.updateProgress("Logging in");
                                    sendKeyOrPassword();
                                    break;
                                case 0x02:
                                    dispatchRecieveError("Name contained invalid characters");
                                    disconnect(ConnectionState.LONG_PAUSE_BEFORE_CONNECT);
                                    break;
                                case 0x03:
                                    dispatchRecieveError("Name contained a banned word");
                                    disconnect(ConnectionState.LONG_PAUSE_BEFORE_CONNECT);
                                    break;
                                case 0x04:
                                    dispatchRecieveError("Account already exists");
                                    disconnect(ConnectionState.LONG_PAUSE_BEFORE_CONNECT);
                                    break;
                                case 0x06:
                                    dispatchRecieveError("Name did not contain enough alphanumeric characters");
                                    disconnect(ConnectionState.LONG_PAUSE_BEFORE_CONNECT);
                                    break;
                                default:
                                    dispatchRecieveError("Unknown SID_CREATEACCOUNT2 status 0x" + Integer.toHexString(status));
                                    disconnect(ConnectionState.LONG_PAUSE_BEFORE_CONNECT);
                                    break;
                            }
                            break;
                        }
                    case SID_SETEMAIL:
                        {
                            dispatchRecieveError("An email address should be registered for this account.");
                            connect.updateProgress("Registering email address");
                            sendSetEmail();
                            break;
                        }
                    case SID_ENTERCHAT:
                        {
                            String uniqueUserName = is.readNTString();
                            StatString myStatString = new StatString(is.readNTString());
                            is.readNTString();
                            myUser = new BNetUser(this, uniqueUserName, cs.myRealm);
                            myUser.setStatString(myStatString);
                            dispatchEnterChat(myUser);
                            dispatchTitleChanged();
                            if (GlobalSettings.displayBattleNetMOTD) {
                                BNCSPacket p = new BNCSPacket(this, BNCSPacketId.SID_NEWS_INFO);
                                p.writeDWord((int) (new java.util.Date().getTime() / 1000));
                                p.sendPacket(bncsOutputStream);
                            }
                            sendFriendsList();
                            if (nlsRevision != null) {
                                sendGetChannelList();
                                sendJoinChannel(cs.channel);
                            }
                            return true;
                        }
                    case SID_GETCHANNELLIST:
                        {
                            recieveGetChannelList(is);
                            break;
                        }
                    case SID_CLANINFO:
                        {
                            recvClanInfo(is);
                            break;
                        }
                    case SID_WARDEN:
                        {
                            recieveWarden(is);
                            break;
                        }
                    default:
                        Out.debugAlways(getClass(), "Unexpected packet " + pr.packetId.name() + "\n" + HexDump.hexDump(pr.data));
                        break;
                }
            }
        }
        return false;
    }
