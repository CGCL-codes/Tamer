    private void flush() {
        setInit(true);
        SessionFactory hfactory = null;
        Session hsession = null;
        javax.mail.Session msession = null;
        try {
            hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);
            hsession = hfactory.openSession();
            msession = (javax.mail.Session) ctx.lookup(durotyMailFactory);
            String pop3Host = msession.getProperty("mail.pop3.host");
            int port = 0;
            try {
                port = Integer.parseInt(msession.getProperty("mail.pop3.port"));
            } catch (Exception ex) {
                port = 0;
            }
            Query query = hsession.getNamedQuery("users-mail");
            query.setBoolean("active", true);
            query.setString("role", "mail");
            ScrollableResults scroll = query.scroll();
            while (scroll.next()) {
                POP3Client client = new POP3Client();
                try {
                    if (port > 0) {
                        client.connect(pop3Host, port);
                    } else {
                        client.connect(pop3Host);
                    }
                    client.setState(POP3Client.AUTHORIZATION_STATE);
                    Users user = (Users) scroll.get(0);
                    String repositoryName = user.getUseUsername();
                    if (client.login(repositoryName, user.getUsePassword())) {
                        POP3MessageInfo[] info = client.listUniqueIdentifiers();
                        if ((info != null) && (info.length > 0)) {
                            for (int i = 0; i < info.length; i++) {
                                if (pool.size() >= poolSize) {
                                    break;
                                }
                                Reader reader = client.retrieveMessage(info[i].number);
                                boolean existMessage = existMessageName(hfactory.openSession(), user, info[i].identifier);
                                String key = info[i].identifier + "--" + repositoryName;
                                if (existMessage) {
                                    client.deleteMessage(info[i].number);
                                } else {
                                    if (!poolContains(key)) {
                                        addPool(key);
                                        MimeMessage mime = buildMimeMessage(info[i].identifier, reader, user);
                                        if (!isSpam(user, mime)) {
                                            client.deleteMessage(info[i].number);
                                            Mailet mailet = new Mailet(this, info[i].identifier, repositoryName, mime);
                                            Thread thread = new Thread(mailet, key);
                                            thread.start();
                                        } else {
                                            client.deleteMessage(info[i].number);
                                        }
                                    }
                                }
                                Thread.sleep(100);
                            }
                        }
                    } else {
                    }
                } catch (Exception e) {
                } finally {
                    System.gc();
                    try {
                        client.logout();
                        client.disconnect();
                    } catch (Exception e) {
                    }
                }
            }
        } catch (Exception e) {
            System.gc();
            pool.clear();
            DLog.log(DLog.ERROR, this.getClass(), e.getMessage());
        } catch (OutOfMemoryError e) {
            System.gc();
            pool.clear();
            DLog.log(DLog.ERROR, this.getClass(), e.getMessage());
        } catch (Throwable e) {
            System.gc();
            pool.clear();
            DLog.log(DLog.ERROR, this.getClass(), e.getMessage());
        } finally {
            System.gc();
            GeneralOperations.closeHibernateSession(hsession);
            setInit(false);
        }
    }
