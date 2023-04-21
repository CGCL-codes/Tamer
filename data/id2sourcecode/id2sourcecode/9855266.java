        public void run() {
            setName("MXA Writer Thread");
            Looper.prepare();
            mHandler = new Handler() {

                public void handleMessage(Message msg) {
                    Message msg2 = Message.obtain(msg);
                    switch(msg.what) {
                        case ConstMXA.MSG_CONNECT:
                            mReconnect = true;
                            if (mConn != null && mConn.isConnected() && mConn.isAuthenticated()) {
                                msg2.arg1 = ConstMXA.MSG_STATUS_SUCCESS;
                                xmppResults.sendMessage(msg2);
                                break;
                            }
                            String host = mPreferences.getString("pref_host", null);
                            int port = Integer.parseInt(mPreferences.getString("pref_port", "5222"));
                            String serviceName = mPreferences.getString("pref_service", "");
                            boolean useEncryption = mPreferences.getBoolean("pref_xmpp_encryption", true);
                            boolean useCompression = mPreferences.getBoolean("pref_xmpp_compression", false);
                            ConnectionConfiguration config = new ConnectionConfiguration(host, port, serviceName);
                            if (!useEncryption) config.setSecurityMode(SecurityMode.disabled); else config.setSecurityMode(SecurityMode.enabled);
                            mConn = new XMPPConnection(config);
                            String username = mPreferences.getString("pref_xmpp_user", "");
                            String password = mPreferences.getString("pref_xmpp_password", "");
                            String resource = mPreferences.getString("pref_resource", "MXA");
                            if (mPreferences.getBoolean("pref_xmpp_debug_enabled", false)) {
                                Debugger.setEnabled(true);
                                Debugger.setDirectory(mPreferences.getString("pref_xmpp_debug_directory", null));
                            }
                            try {
                                mConn.connect();
                                mIQQueue.setMaxRetryTime(Integer.valueOf(mPreferences.getString("pref_xmpp_lost_timeout", "60")));
                                mIQQueue.setMaxRetryCount(Integer.valueOf(mPreferences.getString("pref_xmpp_retry_count", "10")));
                                mIQQueue.setRetryInterval(Integer.valueOf(mPreferences.getString("pref_xmpp_retry_timeout", "10")));
                                ServiceDiscoveryManager sdm = ServiceDiscoveryManager.getInstanceFor(mConn);
                                sdm.addFeature("http://jabber.org/protocol/chatstates");
                                PACKET_TIMEOUT = (Integer.valueOf(mPreferences.getString("pref_xmpp_interval_packet", "5"))) * 1000;
                                ChatStateManager.getInstance(mConn);
                                ProviderManager pm = ProviderManager.getInstance();
                                configureProviderManager(pm);
                                MXAIdentExtensionProvider.install(pm);
                                mConn.addPacketListener(xmppReadWorker, new OrFilter(new OrFilter(new PacketTypeFilter(org.jivesoftware.smack.packet.Message.class), new PacketTypeFilter(Presence.class)), new PacketTypeFilter(IQ.class)));
                                mConn.addConnectionListener(xmppReadWorker);
                                mConn.login(username, password, resource);
                                getContentResolver().delete(ConstMXA.RosterItems.CONTENT_URI, "1", new String[] {});
                                final Roster r = XMPPRemoteService.this.mConn.getRoster();
                                r.addRosterListener(xmppReadWorker);
                                Collection<RosterEntry> rosterEntries = r.getEntries();
                                List<String> entries = new ArrayList<String>(rosterEntries.size());
                                for (RosterEntry re : rosterEntries) entries.add(re.getUser());
                                xmppReadWorker.entriesAdded(entries);
                                NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                Notification status = new Notification(R.drawable.stat_notify_chat, getString(R.string.sb_txt_text), System.currentTimeMillis());
                                status.setLatestEventInfo(XMPPRemoteService.this, getString(R.string.sb_txt_title), getString(R.string.sb_txt_text), PendingIntent.getActivity(XMPPRemoteService.this, 0, new Intent(ConstMXA.INTENT_SERVICEMONITOR), 0));
                                status.flags |= Notification.FLAG_ONGOING_EVENT;
                                status.icon = R.drawable.stat_notify_chat;
                                nm.notify(XMPPSERVICE_STATUS, status);
                                mFileTransferService = new FileTransferService(XMPPRemoteService.this);
                                mMultiUserChatService = new MultiUserChatService(XMPPRemoteService.this);
                                mPubSubService = new PubSubService(XMPPRemoteService.this);
                                mServiceDiscoveryService = new ServiceDiscoveryService(XMPPRemoteService.this);
                                mCollabEditingService = new CollabEditingService(XMPPRemoteService.this);
                                msg2.arg1 = ConstMXA.MSG_STATUS_SUCCESS;
                                mIQQueueTimer = new Timer();
                                mIQQueueTimer.schedule(new IQQueueCheckBackgroundRunner(), 0);
                            } catch (XMPPException e) {
                                msg2.arg1 = ConstMXA.MSG_STATUS_ERROR;
                                Bundle b = msg2.getData();
                                String errorMessage = e.getMessage();
                                if (e.getCause() != null && e.getCause().getMessage() != null) errorMessage += e.getCause().getMessage();
                                b.putString(ConstMXA.EXTRA_ERROR_MESSAGE, errorMessage);
                                msg2.setData(b);
                            }
                            xmppResults.sendMessage(msg2);
                            break;
                        case ConstMXA.MSG_RECONNECT:
                            if (!mReconnect) break;
                            Log.v(TAG, "Reconnection wish");
                            try {
                                if (mConn != null) {
                                    Log.v(TAG, "Trying to reconnect");
                                    String host2 = mPreferences.getString("pref_host", null);
                                    int port2 = Integer.parseInt(mPreferences.getString("pref_port", "5222"));
                                    String serviceName2 = mPreferences.getString("pref_service", "");
                                    boolean useEncryption2 = mPreferences.getBoolean("pref_xmpp_encryption", true);
                                    boolean useCompressio2n = mPreferences.getBoolean("pref_xmpp_compression", false);
                                    ConnectionConfiguration config2 = new ConnectionConfiguration(host2, port2, serviceName2);
                                    if (!useEncryption2) config2.setSecurityMode(SecurityMode.disabled); else config2.setSecurityMode(SecurityMode.enabled);
                                    mConn = new XMPPConnection(config2);
                                    mConn.connect();
                                    String username2 = mPreferences.getString("pref_xmpp_user", "");
                                    String password2 = mPreferences.getString("pref_xmpp_password", "");
                                    String resource2 = mPreferences.getString("pref_resource", "MXA");
                                    mConn.login(username2, password2, resource2);
                                } else break;
                            } catch (Exception e) {
                                if (!(e instanceof IllegalStateException)) {
                                    Message reconnect = new Message();
                                    reconnect.what = ConstMXA.MSG_CONNECT;
                                    xmppWriteWorker.mHandler.sendMessage(reconnect);
                                    Log.e(TAG, "hard reconnect, reason: " + e.getMessage());
                                }
                            }
                            if (mConn != null && mConn.isAuthenticated()) {
                                Log.v(TAG, "Connection established to " + mConn.getServiceName());
                                Message msgResend = new Message();
                                msgResend.what = ConstMXA.MSG_IQ_RESEND;
                                xmppWriteWorker.mHandler.sendMessage(msgResend);
                            } else {
                                Log.v(TAG, "Connection still broken " + mConn.getServiceName());
                                if (mNetworkMonitor.isConnected()) {
                                    Message reconnect = new Message();
                                    reconnect.what = ConstMXA.MSG_CONNECT;
                                    xmppWriteWorker.mHandler.sendMessage(reconnect);
                                    Log.e(TAG, "hard reconnect because of failure");
                                }
                            }
                            break;
                        case ConstMXA.MSG_DISCONNECT:
                            if (mConn == null || !mConn.isConnected()) {
                                msg2.arg1 = ConstMXA.MSG_STATUS_SUCCESS;
                                xmppResults.sendMessage(msg2);
                                break;
                            }
                            mReconnect = false;
                            mIQQueueTimer.cancel();
                            NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                            nm.cancel(XMPPSERVICE_STATUS);
                            mConn.disconnect();
                            msg2.arg1 = ConstMXA.MSG_STATUS_SUCCESS;
                            xmppResults.sendMessage(msg2);
                            break;
                        case ConstMXA.MSG_SEND_IQ:
                            Bundle data = msg.getData();
                            XMPPIQ payloadIQ = data.getParcelable("PAYLOAD");
                            IQImpl iq = new IQImpl(payloadIQ.payload);
                            iq.setTo(payloadIQ.to);
                            switch(payloadIQ.type) {
                                case XMPPIQ.TYPE_GET:
                                    iq.setType(Type.GET);
                                    break;
                                case XMPPIQ.TYPE_SET:
                                    iq.setType(Type.SET);
                                    break;
                                case XMPPIQ.TYPE_RESULT:
                                    iq.setType(Type.RESULT);
                                    break;
                                case XMPPIQ.TYPE_ERROR:
                                    iq.setType(Type.ERROR);
                                    break;
                                default:
                                    iq.setType(Type.GET);
                            }
                            iq.setPacketID(payloadIQ.packetID);
                            if ((payloadIQ.namespace != null) || (payloadIQ.token != null)) {
                                MXAIdentExtension mie = new MXAIdentExtension(payloadIQ.namespace, payloadIQ.token);
                                iq.addExtension(mie);
                            }
                            mConn.sendPacket(iq);
                            break;
                        case ConstMXA.MSG_IQ_RESEND:
                            if (mIQQueue.getCount() == 0 || !mConn.isAuthenticated()) break;
                            mIQQueueTimer.cancel();
                            mIQQueueTimer = new Timer();
                            mIQQueueTimer.schedule(new IQQueueCheckBackgroundRunner(), mIQQueue.getRetryInterval() * 1000, mIQQueue.getRetryInterval() * 1000);
                            resendIQ();
                            break;
                        case ConstMXA.MSG_SEND_PRESENCE:
                            Bundle dataPresence = msg.getData();
                            XMPPPresence payloadPresence = dataPresence.getParcelable("PAYLOAD");
                            Presence presence = new Presence(Presence.Type.available);
                            presence.setStatus(payloadPresence.status);
                            presence.setPriority(payloadPresence.priority);
                            switch(payloadPresence.mode) {
                                case XMPPPresence.MODE_AVAILABLE:
                                    presence.setMode(Mode.available);
                                    break;
                                case XMPPPresence.MODE_AWAY:
                                    presence.setMode(Mode.away);
                                    break;
                                case XMPPPresence.MODE_CHAT:
                                    presence.setMode(Mode.chat);
                                    break;
                                case XMPPPresence.MODE_DND:
                                    presence.setMode(Mode.dnd);
                                    break;
                                case XMPPPresence.MODE_XA:
                                    presence.setMode(Mode.xa);
                                    break;
                                default:
                                    presence.setMode(Mode.available);
                            }
                            try {
                                mConn.sendPacket(presence);
                                msg2.arg1 = ConstMXA.MSG_STATUS_DELIVERED;
                            } catch (IllegalStateException e) {
                                msg2.arg1 = ConstMXA.MSG_STATUS_ERROR;
                            }
                            xmppResults.sendMessage(msg2);
                            Intent i = new Intent(ConstMXA.BROADCAST_PRESENCE);
                            i.putExtra("STATUS_TEXT", payloadPresence.status);
                            sendBroadcast(i);
                            break;
                    }
                }
            };
            Looper.loop();
        }
