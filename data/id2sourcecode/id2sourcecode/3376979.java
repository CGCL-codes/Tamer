        public boolean onDisconnect(INonBlockingConnection conn) throws IOException {
            try {
                Log.info("client dc @ " + conn.getAttachment());
                final ClientHandle cs = remove(conn);
                String identity;
                if (cs.getEmail() == null || cs.getEmail().length() <= 0) {
                    identity = "" + conn.getAttachment();
                } else {
                    identity = cs.getEmail() + ", " + conn.getAttachment();
                }
                if (cs != null) {
                    Log.info("removed client " + identity);
                    KillPeerMessage killPeer = new KillPeerMessage();
                    killPeer.id = cs.getId();
                    for (Integer id : cs.getIds()) {
                        ClientHandle ses = get(id);
                        ses.getIds().remove(Integer.valueOf(cs.getId()));
                        send(ses.connection(), killPeer);
                    }
                    cs.getIds().clear();
                    Log.info("kill peer msg sent to the peers of " + identity);
                    if (cs.getEmail() != null && cs.getEmail().length() > 0) {
                        Session s = null;
                        try {
                            s = DB.begin();
                            Account acc = (Account) DB.queryOne(s, Account.class, Account.F_ACC_EMAIL, cs.getEmail());
                            if (acc != null) {
                                acc.setAccOnline(false);
                                s.update(acc);
                            }
                            DB.commit(s);
                            Log.info("mark " + identity + " as offline on database");
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            if (s != null) {
                                try {
                                    DB.rollback(s);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        Persister.getInstance().execute(new Offline(cs.getChannel(), cs.getEmail()));
                        Log.info("mark " + identity + " as offline on persistent store");
                    }
                }
                fireOnDisconnected(conn);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return true;
        }
