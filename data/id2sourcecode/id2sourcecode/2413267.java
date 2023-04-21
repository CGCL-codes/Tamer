    protected synchronized boolean loginInternal(String id, String password, boolean hidden) throws YmsgException {
        try {
            Log.v(Constants.LOG_TAG, "retrieve login server ip address: url=" + Constants.YMSG_AUTH_PRE_URL);
            HttpDownloader d = new HttpDownloader(Constants.YMSG_AUTH_PRE_URL);
            HttpResponse res = d.execute();
            ByteArrayInputStream bais = new ByteArrayInputStream(res.getBody());
            Properties p = new Properties();
            p.load(bais);
            String serverIp = p.getProperty("CS_IP_ADDRESS");
            Log.v(Constants.LOG_TAG, "serverIp: " + serverIp);
            YmsgPacket outPacket = new YmsgPacket();
            outPacket.setProtocolVersion(0x10);
            outPacket.setVendorId(0x64);
            outPacket.setService(0x57);
            outPacket.setStatus(0);
            outPacket.addValue("1", id);
            OutputStream os;
            InputStream is;
            InetAddress addr = InetAddress.getByName(serverIp);
            Socket sock = new Socket(addr, 5050);
            os = sock.getOutputStream();
            outPacket.writeTo(os);
            is = sock.getInputStream();
            YmsgPacket inPacket = new YmsgPacket(is);
            String challenge = inPacket.getValueAsString("94");
            int sessionId = inPacket.getSessionId();
            Map<String, String> params = new HashMap<String, String>();
            params.put("src", "ymsgr");
            params.put("ts", "");
            params.put("login", id);
            params.put("passwd", password);
            params.put("chal", challenge);
            HttpDownloader d2 = new HttpDownloader(Constants.YMSG_AUTH_TOKEN_URL, params);
            HttpResponse res2 = d2.execute();
            String res2BodyStr = new String(res2.getBody(), "8859_1");
            Log.v(Constants.LOG_TAG, "res2BodyStr: " + res2BodyStr);
            String line = null;
            BufferedReader br = new BufferedReader(new StringReader(res2BodyStr), 64);
            line = br.readLine();
            if (line == null || !"0".equals(line)) {
                Map<String, Object> errParams = new HashMap<String, Object>();
                errParams.put(ERROR_AUTH_CAUSE, ERROR_AUTH_INVALID_ACCOUNT);
                fireOnLoginFailuer(errParams);
                if (sock != null) {
                    try {
                        sock.shutdownInput();
                    } catch (Exception ignore) {
                    }
                    try {
                        sock.shutdownOutput();
                    } catch (Exception ignore) {
                    }
                    try {
                        sock.close();
                    } catch (Exception ignore) {
                    }
                }
                return false;
            }
            line = br.readLine();
            String ymsgr = line.substring(line.indexOf('=') + 1);
            Log.v(Constants.LOG_TAG, "ymsgr: " + ymsgr);
            line = br.readLine();
            String partnerId = line.substring(line.indexOf('=') + 1);
            Log.v(Constants.LOG_TAG, "partnerId: " + partnerId);
            br.close();
            params.clear();
            params.put("src", "ymsgr");
            params.put("ts", "");
            params.put("token", ymsgr);
            Log.v(Constants.LOG_TAG, Utils.hexDump(ymsgr.getBytes()));
            HttpDownloader d3 = new HttpDownloader(Constants.YMSG_AUTH_LOGIN_URL, params);
            HttpResponse res3 = d3.execute();
            String res3BodyStr = new String(res3.getBody(), "8859_1");
            Log.v(Constants.LOG_TAG, "res3Body: " + Utils.hexDump(res3.getBody()));
            br = new BufferedReader(new StringReader(res3BodyStr));
            line = br.readLine();
            if (line == null || !"0".equals(line)) {
                Map<String, Object> errParams = new HashMap<String, Object>();
                errParams.put(ERROR_AUTH_CAUSE, ERROR_AUTH_INVALID_TOKEN);
                fireOnLoginFailuer(errParams);
                if (sock != null) {
                    try {
                        sock.shutdownInput();
                    } catch (Exception ignore) {
                    }
                    try {
                        sock.shutdownOutput();
                    } catch (Exception ignore) {
                    }
                    try {
                        sock.close();
                    } catch (Exception ignore) {
                    }
                }
                return false;
            }
            line = br.readLine();
            String crumb = line.split("=")[1];
            Log.v(Constants.LOG_TAG, "crumb: " + crumb);
            line = br.readLine();
            String yCookie = line.substring(line.indexOf('=') + 1, line.indexOf(';'));
            Log.v(Constants.LOG_TAG, "yCookie: " + yCookie);
            line = br.readLine();
            String tCookie = line.substring(line.indexOf('=') + 1, line.indexOf(';'));
            Log.v(Constants.LOG_TAG, "tCookie: " + tCookie);
            String crypt = crumb + challenge;
            byte[] md5 = ChallengeResponseUtility.md5(crypt);
            String y64str = ChallengeResponseUtility.yahoo64(md5);
            YmsgPacket outPacket2 = new YmsgPacket();
            outPacket2.setProtocolVersion(0x10);
            outPacket2.setVendorId(0x64);
            outPacket2.setService(0x54);
            if (hidden) {
                outPacket2.setStatus(YMSG_STATUS_INVISIBLE);
            } else {
                outPacket2.setStatus(YMSG_STATUS_OFFLINE);
            }
            outPacket2.addValue("1", id);
            outPacket2.addValue("0", id);
            outPacket2.addValue("277", yCookie);
            outPacket2.addValue("278", tCookie);
            outPacket2.addValue("307", y64str);
            outPacket2.addValue("244", "4186047");
            outPacket2.addValue("2", id);
            outPacket2.addValue("2", "1");
            outPacket2.addValue("98", "jp");
            outPacket2.addValue("135", "9.0.0.1727");
            outPacket2.writeTo(os);
            os.flush();
            BuddyList buddyList = new BuddyList();
            User user = new User();
            YmsgPacket p1 = new YmsgPacket(is);
            if (p1.getService() == YMSG_SERVICE_LIST) {
                String primaryId = p1.getValueAsString("3");
                Log.v(LOG_TAG, "primaryId: " + primaryId);
                String currentId = p1.getValueAsString("2");
                Log.v(LOG_TAG, "currentId: " + currentId);
                String[] extraIds = p1.getValueAsString("89").split(",");
                List<String> extraIdList = Arrays.asList(extraIds);
                Log.v(LOG_TAG, "extraIdList: " + extraIdList);
                user.setId(primaryId);
                user.setPassword(password);
                user.setCurrentId(id);
                user.addExtraId(extraIdList);
                if (hidden) {
                    user.setStatus(YMSG_STATUS_OFFLINE);
                } else {
                    user.setStatus(YMSG_STATUS_ONLINE);
                }
                this.user = user;
            }
            YmsgPacket p2 = null;
            String groupName = null;
            while (true) {
                p2 = new YmsgPacket(is);
                Log.v(LOG_TAG, "YMSG received: p2: " + p2);
                if (p2.getService() != YMSG_SERVICE_LIST_V15) {
                    break;
                }
                for (Entry entry : p2.getEntries()) {
                    if ("65".equals(entry.getKey())) {
                        groupName = new String(entry.getValue());
                        Log.v(LOG_TAG, "group foud: " + groupName);
                    } else if ("7".equals(entry.getKey())) {
                        Buddy buddy = new Buddy();
                        buddy.setId(new String(entry.getValue()));
                        buddyList.addBuddy(groupName, buddy);
                        Log.v(LOG_TAG, "buddy ID foud: " + buddy.getId());
                    }
                }
            }
            YmsgPacket p3 = p2;
            Log.v(LOG_TAG, "YMSG use p2 as p3: " + p3);
            if (p3.getService() == YMSG_SERVICE_STATUS_V15) {
                String buddyId = null;
                int status = YMSG_STATUS_OFFLINE;
                String statusMessage = null;
                for (Entry entry : p3.getEntries()) {
                    if ("7".equals(entry.getKey())) {
                        if (buddyId != null) {
                            Buddy buddy = buddyList.findById(buddyId);
                            if (buddy != null) {
                                buddy.setStatus(status);
                                buddy.setStatusMessage(statusMessage);
                            }
                            buddyId = null;
                            status = YMSG_STATUS_OFFLINE;
                            statusMessage = null;
                        }
                        buddyId = new String(entry.getValue());
                        Log.v(LOG_TAG, "buddy id found in status_v15: " + buddyId);
                    } else if ("10".equals(entry.getKey())) {
                        status = Integer.parseInt(new String(entry.getValue()));
                        Log.v(LOG_TAG, "status found in status_v15: " + new String(entry.getValue()));
                    } else if ("19".equals(entry.getKey())) {
                        statusMessage = new String(entry.getValue());
                        Log.v(LOG_TAG, "status message found in status_v15: " + new String(entry.getValue()));
                    }
                }
                if (buddyId != null) {
                    Buddy buddy = buddyList.findById(buddyId);
                    if (buddy != null) {
                        buddy.setStatus(status);
                        buddy.setStatusMessage(statusMessage);
                    }
                    buddyId = null;
                    status = 0;
                    statusMessage = null;
                }
                this.buddyList = buddyList;
            }
            if (!user.getCurrentId().equalsIgnoreCase(getConversationManager().getCurrentId())) {
                getConversationManager().clear();
            }
            getConversationManager().setCurrentId(user.getCurrentId());
            this.sockReceiveThread = new Thread(new SocketHandler());
            this.sockReceiveThread.start();
            this.sock = sock;
            this.sessionId = sessionId;
        } catch (RuntimeException e) {
            throw e;
        } catch (YmsgException e) {
            throw e;
        } catch (Exception e) {
            throw new YmsgException("login failed", e);
        }
        return true;
    }
