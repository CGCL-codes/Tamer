    @Override
    public void logIn(PresenceType presenceType, String verboseStatus) {
        final org.jivesoftware.smack.packet.Presence presence = new org.jivesoftware.smack.packet.Presence(org.jivesoftware.smack.packet.Presence.Type.available);
        if (JiveGlobals.getBooleanProperty("plugin.gateway." + getTransport().getType() + ".avatars", true) && getAvatar() != null) {
            Avatar avatar = getAvatar();
            avatar.setLegacyIdentifier(avatar.getXmppHash());
            VCardUpdateExtension ext = new VCardUpdateExtension();
            ext.setPhotoHash(avatar.getLegacyIdentifier());
            presence.addExtension(ext);
        }
        final Presence.Mode pMode = ((XMPPTransport) getTransport()).convertGatewayStatusToXMPP(presenceType);
        if (pMode != null) {
            presence.setMode(pMode);
        }
        if (verboseStatus != null && verboseStatus.trim().length() > 0) {
            presence.setStatus(verboseStatus);
        }
        setPendingPresenceAndStatus(presenceType, verboseStatus);
        if (!this.isLoggedIn()) {
            listener = new XMPPListener(this);
            presenceHandler = new XMPPPresenceHandler(this);
            runThread = new Thread() {

                @Override
                public void run() {
                    String userName = generateUsername(registration.getUsername());
                    conn = new XMPPConnection(config);
                    try {
                        conn.getSASLAuthentication().registerSASLMechanism("DIGEST-MD5", MySASLDigestMD5Mechanism.class);
                        if (getTransport().getType().equals(TransportType.facebook) && registration.getUsername().equals("{PLATFORM}")) {
                            conn.getSASLAuthentication().registerSASLMechanism("X-FACEBOOK-PLATFORM", FacebookConnectSASLMechanism.class);
                            conn.getSASLAuthentication().supportSASLMechanism("X-FACEBOOK-PLATFORM", 0);
                        }
                        Roster.setDefaultSubscriptionMode(SubscriptionMode.manual);
                        conn.connect();
                        conn.addConnectionListener(listener);
                        try {
                            conn.addPacketListener(presenceHandler, new PacketTypeFilter(org.jivesoftware.smack.packet.Presence.class));
                            conn.addPacketListener(listener, new OrFilter(new PacketTypeFilter(GoogleMailBoxPacket.class), new PacketExtensionFilter(GoogleNewMailExtension.ELEMENT_NAME, GoogleNewMailExtension.NAMESPACE)));
                            conn.login(userName, registration.getPassword(), xmppResource);
                            conn.sendPacket(presence);
                            conn.getChatManager().addChatListener(listener);
                            conn.getRoster().addRosterListener(listener);
                            if (JiveGlobals.getBooleanProperty("plugin.gateway." + getTransport().getType() + ".avatars", !TransportType.facebook.equals(getTransport().getType())) && getAvatar() != null) {
                                new Thread() {

                                    @Override
                                    public void run() {
                                        Avatar avatar = getAvatar();
                                        VCard vCard = new VCard();
                                        try {
                                            vCard.load(conn);
                                            vCard.setAvatar(Base64.decode(avatar.getImageData()), avatar.getMimeType());
                                            vCard.save(conn);
                                        } catch (XMPPException e) {
                                            Log.debug("XMPP: Error while updating vcard for avatar change.", e);
                                        } catch (NotFoundException e) {
                                            Log.debug("XMPP: Unable to find avatar while setting initial.", e);
                                        }
                                    }
                                }.start();
                            }
                            setLoginStatus(TransportLoginStatus.LOGGED_IN);
                            syncUsers();
                            if (getTransport().getType().equals(TransportType.gtalk) && JiveGlobals.getBooleanProperty("plugin.gateway.gtalk.mailnotifications", true)) {
                                conn.sendPacket(new IQWithPacketExtension(generateFullJID(getRegistration().getUsername()), new GoogleUserSettingExtension(null, true, null), IQ.Type.SET));
                                conn.sendPacket(new IQWithPacketExtension(generateFullJID(getRegistration().getUsername()), new GoogleMailNotifyExtension()));
                                mailCheck = new MailCheck();
                                timer.schedule(mailCheck, timerInterval, timerInterval);
                            }
                        } catch (XMPPException e) {
                            Log.debug(getTransport().getType() + " user's login/password does not appear to be correct: " + getRegistration().getUsername(), e);
                            setFailureStatus(ConnectionFailureReason.USERNAME_OR_PASSWORD_INCORRECT);
                            sessionDisconnectedNoReconnect(LocaleUtils.getLocalizedString("gateway.xmpp.passwordincorrect", "kraken"));
                        }
                    } catch (XMPPException e) {
                        Log.debug(getTransport().getType() + " user is not able to connect: " + getRegistration().getUsername(), e);
                        setFailureStatus(ConnectionFailureReason.CAN_NOT_CONNECT);
                        sessionDisconnected(LocaleUtils.getLocalizedString("gateway.xmpp.connectionfailed", "kraken"));
                    }
                }
            };
            runThread.start();
        }
    }
