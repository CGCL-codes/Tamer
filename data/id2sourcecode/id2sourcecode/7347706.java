    @Override
    protected void connectedLoop() throws Exception {
        lastNullPacket = System.currentTimeMillis();
        lastNormalJoin = 0;
        profile.lastAntiIdle = lastNullPacket;
        if (botnet != null) botnet.sendStatusUpdate();
        while (isConnected() && !socket.isClosed() && !disposed) {
            long timeNow = System.currentTimeMillis();
            if (true) {
                long timeSinceNullPacket = (timeNow - lastNullPacket) / 1000;
                if (timeSinceNullPacket > 5) {
                    lastNullPacket = timeNow;
                    BNCSPacket p = new BNCSPacket(this, BNCSPacketId.SID_NULL);
                    p.sendPacket(bncsOutputStream);
                }
            }
            if ((channelName != null) && cs.enableAntiIdle) {
                synchronized (profile) {
                    long timeSinceAntiIdle = timeNow - profile.lastAntiIdle;
                    timeSinceAntiIdle /= 1000;
                    timeSinceAntiIdle /= 60;
                    if (timeSinceAntiIdle >= cs.antiIdleTimer) {
                        profile.lastAntiIdle = timeNow;
                        sendChatInternal(getAntiIdle());
                    }
                }
            }
            if (bncsInputStream.available() <= 0) {
                sleep(200);
            } else {
                BNCSPacketReader pr = new BNCSPacketReader(bncsInputStream);
                BNetInputStream is = pr.getData();
                switch(pr.packetId) {
                    case SID_NULL:
                        {
                            lastNullPacket = timeNow;
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
                    case SID_NEWS_INFO:
                        {
                            int numEntries = is.readByte();
                            is.skip(12);
                            for (int i = 0; i < numEntries; i++) {
                                int timeStamp = is.readDWord();
                                String news = is.readNTStringUTF8().trim();
                                if (timeStamp == 0) dispatchRecieveServerInfo(news);
                            }
                            break;
                        }
                    case SID_GETCHANNELLIST:
                        {
                            recieveGetChannelList(is);
                            break;
                        }
                    case SID_CHATEVENT:
                        {
                            BNCSChatEventId eid = BNCSChatEventId.values()[is.readDWord()];
                            int flags = is.readDWord();
                            int ping = is.readDWord();
                            is.skip(12);
                            String username = is.readNTString();
                            ByteArray data = null;
                            StatString statstr = null;
                            switch(eid) {
                                case EID_SHOWUSER:
                                case EID_JOIN:
                                    statstr = is.readStatString();
                                    break;
                                case EID_USERFLAGS:
                                    statstr = is.readStatString();
                                    if (statstr.toString().length() == 0) statstr = null;
                                    break;
                                default:
                                    data = new ByteArray(is.readNTBytes());
                                    break;
                            }
                            BNetUser user = null;
                            switch(eid) {
                                case EID_SHOWUSER:
                                case EID_USERFLAGS:
                                case EID_JOIN:
                                case EID_LEAVE:
                                case EID_TALK:
                                case EID_EMOTE:
                                case EID_WHISPERSENT:
                                case EID_WHISPER:
                                    switch(productID) {
                                        case D2DV:
                                        case D2XP:
                                            int asterisk = username.indexOf('*');
                                            if (asterisk >= 0) username = username.substring(asterisk + 1);
                                            break;
                                    }
                                    if (myUser.equals(username)) user = myUser; else user = getCreateBNetUser(username, myUser);
                                    user.setFlags(flags);
                                    user.setPing(ping);
                                    if (statstr != null) user.setStatString(statstr);
                                    break;
                            }
                            switch(eid) {
                                case EID_SHOWUSER:
                                case EID_USERFLAGS:
                                    dispatchChannelUser(user);
                                    break;
                                case EID_JOIN:
                                    dispatchChannelJoin(user);
                                    break;
                                case EID_LEAVE:
                                    dispatchChannelLeave(user);
                                    break;
                                case EID_TALK:
                                    dispatchRecieveChat(user, data);
                                    break;
                                case EID_BROADCAST:
                                    dispatchRecieveBroadcast(username, flags, data.toString());
                                    break;
                                case EID_EMOTE:
                                    dispatchRecieveEmote(user, data.toString());
                                    break;
                                case EID_INFO:
                                    dispatchRecieveServerInfo(data.toString());
                                    break;
                                case EID_ERROR:
                                    dispatchRecieveServerError(data.toString());
                                    break;
                                case EID_CHANNEL:
                                    String newChannel = data.toString();
                                    if ((channelName != null) && !channelName.equals(newChannel)) clearQueue();
                                    channelName = newChannel;
                                    dispatchJoinedChannel(newChannel, flags);
                                    dispatchTitleChanged();
                                    if (botnet != null) botnet.sendStatusUpdate();
                                    break;
                                case EID_WHISPERSENT:
                                    dispatchWhisperSent(user, data.toString());
                                    break;
                                case EID_WHISPER:
                                    dispatchWhisperRecieved(user, data.toString());
                                    break;
                                case EID_CHANNELDOESNOTEXIST:
                                    dispatchRecieveError("Channel does not exist; creating");
                                    sendJoinChannel2(data.toString());
                                    break;
                                case EID_CHANNELRESTRICTED:
                                    long timeSinceNormalJoin = timeNow - lastNormalJoin;
                                    if ((lastNormalJoin != 0) && (timeSinceNormalJoin < 5000)) {
                                        dispatchRecieveError("Channel is restricted; forcing entry");
                                        sendJoinChannel2(data.toString());
                                    } else {
                                        dispatchRecieveError("Channel " + data.toString() + " is restricted");
                                    }
                                    break;
                                case EID_CHANNELFULL:
                                    dispatchRecieveError("Channel " + data.toString() + " is full");
                                    break;
                                default:
                                    dispatchRecieveError("Unknown SID_CHATEVENT " + eid + ": " + data.toString());
                                    break;
                            }
                            break;
                        }
                    case SID_MESSAGEBOX:
                        {
                            is.readDWord();
                            String text = is.readNTStringUTF8();
                            String caption = is.readNTStringUTF8();
                            dispatchRecieveInfo("<" + caption + "> " + text);
                            break;
                        }
                    case SID_FLOODDETECTED:
                        {
                            dispatchRecieveError("You have been disconnected for flooding.");
                            disconnect(ConnectionState.LONG_PAUSE_BEFORE_CONNECT);
                            break;
                        }
                    case SID_QUERYREALMS2:
                        {
                            is.readDWord();
                            int numRealms = is.readDWord();
                            String realms[] = new String[numRealms];
                            for (int i = 0; i < numRealms; i++) {
                                is.readDWord();
                                realms[i] = is.readNTStringUTF8();
                                is.readNTStringUTF8();
                            }
                            dispatchQueryRealms2(realms);
                            break;
                        }
                    case SID_LOGONREALMEX:
                        {
                            if (pr.packetLength < 12) throw new Exception("pr.packetLength < 12"); else if (pr.packetLength == 12) {
                                is.readDWord();
                                int status = is.readDWord();
                                switch(status) {
                                    case 0x80000001:
                                        dispatchRecieveError("Realm is unavailable.");
                                        break;
                                    case 0x80000002:
                                        dispatchRecieveError("Realm logon failed");
                                        break;
                                    default:
                                        throw new Exception("Unknown status code 0x" + Integer.toHexString(status));
                                }
                            } else {
                                int MCPChunk1[] = new int[4];
                                MCPChunk1[0] = is.readDWord();
                                MCPChunk1[1] = is.readDWord();
                                MCPChunk1[2] = is.readDWord();
                                MCPChunk1[3] = is.readDWord();
                                int ip = is.readDWord();
                                int port = is.readDWord();
                                port = ((port & 0xFF00) >> 8) | ((port & 0x00FF) << 8);
                                int MCPChunk2[] = new int[12];
                                MCPChunk2[0] = is.readDWord();
                                MCPChunk2[1] = is.readDWord();
                                MCPChunk2[2] = is.readDWord();
                                MCPChunk2[3] = is.readDWord();
                                MCPChunk2[4] = is.readDWord();
                                MCPChunk2[5] = is.readDWord();
                                MCPChunk2[6] = is.readDWord();
                                MCPChunk2[7] = is.readDWord();
                                MCPChunk2[8] = is.readDWord();
                                MCPChunk2[9] = is.readDWord();
                                MCPChunk2[10] = is.readDWord();
                                MCPChunk2[11] = is.readDWord();
                                String uniqueName = is.readNTString();
                                dispatchLogonRealmEx(MCPChunk1, ip, port, MCPChunk2, uniqueName);
                            }
                            break;
                        }
                    case SID_READUSERDATA:
                        {
                            int numAccounts = is.readDWord();
                            int numKeys = is.readDWord();
                            @SuppressWarnings("unchecked") List<Object> keys = (List<Object>) CookieUtility.destroyCookie(is.readDWord());
                            if (numAccounts != 1) throw new IllegalStateException("SID_READUSERDATA with numAccounts != 1");
                            UserProfile up = new UserProfile((String) keys.remove(0));
                            dispatchRecieveInfo("Profile for " + up.getUser());
                            for (int i = 0; i < numKeys; i++) {
                                String key = (String) keys.get(i);
                                String value = is.readNTStringUTF8();
                                if ((key == null) || (key.length() == 0)) continue;
                                value = prettyProfileValue(key, value);
                                if (value.length() != 0) {
                                    dispatchRecieveInfo(key + " = " + value);
                                } else if (key.equals(UserProfile.PROFILE_DESCRIPTION) || key.equals(UserProfile.PROFILE_LOCATION) || key.equals(UserProfile.PROFILE_SEX)) {
                                } else {
                                    continue;
                                }
                                up.put(key, value);
                            }
                            if (PluginManager.getEnableGui()) new ProfileEditor(up, this);
                            break;
                        }
                    case SID_FRIENDSLIST:
                        {
                            byte numEntries = is.readByte();
                            FriendEntry[] entries = new FriendEntry[numEntries];
                            for (int i = 0; i < numEntries; i++) {
                                String uAccount = is.readNTString();
                                byte uStatus = is.readByte();
                                byte uLocation = is.readByte();
                                int uProduct = is.readDWord();
                                String uLocationName = is.readNTStringUTF8();
                                entries[i] = new FriendEntry(uAccount, uStatus, uLocation, uProduct, uLocationName);
                            }
                            dispatchFriendsList(entries);
                            break;
                        }
                    case SID_FRIENDSUPDATE:
                        {
                            byte fEntry = is.readByte();
                            byte fLocation = is.readByte();
                            byte fStatus = is.readByte();
                            int fProduct = is.readDWord();
                            String fLocationName = is.readNTStringUTF8();
                            dispatchFriendsUpdate(new FriendEntry(fEntry, fStatus, fLocation, fProduct, fLocationName));
                            break;
                        }
                    case SID_FRIENDSADD:
                        {
                            String fAccount = is.readNTString();
                            byte fLocation = is.readByte();
                            byte fStatus = is.readByte();
                            int fProduct = is.readDWord();
                            String fLocationName = is.readNTStringUTF8();
                            dispatchFriendsAdd(new FriendEntry(fAccount, fStatus, fLocation, fProduct, fLocationName));
                            break;
                        }
                    case SID_FRIENDSREMOVE:
                        {
                            byte entry = is.readByte();
                            dispatchFriendsRemove(entry);
                            break;
                        }
                    case SID_FRIENDSPOSITION:
                        {
                            byte oldPosition = is.readByte();
                            byte newPosition = is.readByte();
                            dispatchFriendsPosition(oldPosition, newPosition);
                            break;
                        }
                    case SID_CLANINFO:
                        {
                            recvClanInfo(is);
                            break;
                        }
                    case SID_CLANFINDCANDIDATES:
                        {
                            Object cookie = CookieUtility.destroyCookie(is.readDWord());
                            byte status = is.readByte();
                            byte numCandidates = is.readByte();
                            List<String> candidates = new ArrayList<String>(numCandidates);
                            for (int i = 0; i < numCandidates; i++) candidates.add(is.readNTString());
                            switch(status) {
                                case 0x00:
                                    if (numCandidates < 9) dispatchRecieveError("Insufficient elegible W3 players (" + numCandidates + "/9)."); else dispatchClanFindCandidates(cookie, candidates);
                                    break;
                                case 0x01:
                                    dispatchRecieveError("Clan tag already taken");
                                    break;
                                case 0x08:
                                    dispatchRecieveError("Already in a clan");
                                    break;
                                case 0x0a:
                                    dispatchRecieveError("Invalid clan tag");
                                    break;
                                default:
                                    dispatchRecieveError("Unknown response 0x" + Integer.toHexString(status));
                                    break;
                            }
                            break;
                        }
                    case SID_CLANCREATIONINVITATION:
                        {
                            int cookie = is.readDWord();
                            int clanTag = is.readDWord();
                            String clanName = is.readNTString();
                            String inviter = is.readNTString();
                            ClanCreationInvitationCookie c = new ClanCreationInvitationCookie(this, cookie, clanTag, clanName, inviter);
                            dispatchClanCreationInvitation(c);
                            break;
                        }
                    case SID_CLANINVITATION:
                        {
                            Object cookie = CookieUtility.destroyCookie(is.readDWord());
                            byte status = is.readByte();
                            String result;
                            switch(status) {
                                case 0x00:
                                    result = "Invitation accepted";
                                    break;
                                case 0x04:
                                    result = "Invitation declined";
                                    break;
                                case 0x05:
                                    result = "Failed to invite user";
                                    break;
                                case 0x09:
                                    result = "Clan is full";
                                    break;
                                default:
                                    result = "Unknown response 0x" + Integer.toHexString(status);
                                    break;
                            }
                            if (cookie instanceof CommandResponseCookie) ((CommandResponseCookie) cookie).sendChat(result); else Out.info(getClass(), result);
                            break;
                        }
                    case SID_CLANINVITATIONRESPONSE:
                        {
                            int cookie = is.readDWord();
                            int clanTag = is.readDWord();
                            String clanName = is.readNTString();
                            String inviter = is.readNTString();
                            ClanInvitationCookie c = new ClanInvitationCookie(this, cookie, clanTag, clanName, inviter);
                            dispatchClanInvitation(c);
                            break;
                        }
                    case SID_CLANRANKCHANGE:
                        {
                            int cookie = is.readDWord();
                            byte status = is.readByte();
                            Object obj = CookieUtility.destroyCookie(cookie);
                            String statusCode = null;
                            switch(status) {
                                case ClanStatusIDs.CLANSTATUS_SUCCESS:
                                    statusCode = "Successfully changed rank";
                                    break;
                                case 0x01:
                                    statusCode = "Failed to change rank";
                                    break;
                                case ClanStatusIDs.CLANSTATUS_TOO_SOON:
                                    statusCode = "Cannot change user'socket rank yet";
                                    break;
                                case ClanStatusIDs.CLANSTATUS_NOT_AUTHORIZED:
                                    statusCode = "Not authorized to change user rank*";
                                    break;
                                case 0x08:
                                    statusCode = "Not allowed to change user rank**";
                                    break;
                                default:
                                    statusCode = "Unknown ClanStatusID 0x" + Integer.toHexString(status);
                            }
                            dispatchRecieveInfo(statusCode + "\n" + obj.toString());
                            break;
                        }
                    case SID_CLANMOTD:
                        {
                            int cookieId = is.readDWord();
                            is.readDWord();
                            String text = is.readNTStringUTF8();
                            Object cookie = CookieUtility.destroyCookie(cookieId);
                            dispatchClanMOTD(cookie, text);
                            break;
                        }
                    case SID_CLANMEMBERLIST:
                        {
                            is.readDWord();
                            byte numMembers = is.readByte();
                            ClanMember[] members = new ClanMember[numMembers];
                            for (int i = 0; i < numMembers; i++) {
                                String uName = is.readNTString();
                                byte uRank = is.readByte();
                                byte uOnline = is.readByte();
                                String uLocation = is.readNTStringUTF8();
                                members[i] = new ClanMember(uName, uRank, uOnline, uLocation);
                            }
                            dispatchClanMemberList(members);
                            break;
                        }
                    case SID_CLANMEMBERREMOVED:
                        {
                            String username = is.readNTString();
                            dispatchClanMemberRemoved(username);
                            break;
                        }
                    case SID_CLANMEMBERSTATUSCHANGE:
                        {
                            String username = is.readNTString();
                            byte rank = is.readByte();
                            byte status = is.readByte();
                            String location = is.readNTStringUTF8();
                            dispatchClanMemberStatusChange(new ClanMember(username, rank, status, location));
                            break;
                        }
                    case SID_CLANMEMBERRANKCHANGE:
                        {
                            byte oldRank = is.readByte();
                            byte newRank = is.readByte();
                            String user = is.readNTString();
                            dispatchRecieveInfo("Rank changed from " + ClanRankIDs.ClanRank[oldRank] + " to " + ClanRankIDs.ClanRank[newRank] + " by " + user);
                            dispatchClanMemberRankChange(oldRank, newRank, user);
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
    }
