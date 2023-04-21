    public void run() {
        log.info(Messages.getMessage("start00", "MailServer", host + ":" + port));
        while (!stopped) {
            try {
                pop3.connect(host, port);
                pop3.login(userid, password);
                POP3MessageInfo[] messages = pop3.listMessages();
                if (messages != null && messages.length > 0) {
                    for (int i = 0; i < messages.length; i++) {
                        Reader reader = pop3.retrieveMessage(messages[i].number);
                        if (reader == null) {
                            continue;
                        }
                        StringBuffer buffer = new StringBuffer();
                        BufferedReader bufferedReader = new BufferedReader(reader);
                        int ch;
                        while ((ch = bufferedReader.read()) != -1) {
                            buffer.append((char) ch);
                        }
                        bufferedReader.close();
                        ByteArrayInputStream bais = new ByteArrayInputStream(buffer.toString().getBytes());
                        Properties prop = new Properties();
                        Session session = Session.getDefaultInstance(prop, null);
                        MimeMessage mimeMsg = new MimeMessage(session, bais);
                        pop3.deleteMessage(messages[i].number);
                        if (mimeMsg != null) {
                            MailWorker worker = new MailWorker(this, mimeMsg);
                            if (doThreads) {
                                Thread thread = new Thread(worker);
                                thread.setDaemon(true);
                                thread.start();
                            } else {
                                worker.run();
                            }
                        }
                    }
                }
            } catch (java.io.InterruptedIOException iie) {
            } catch (Exception e) {
                log.debug(Messages.getMessage("exception00"), e);
                break;
            } finally {
                try {
                    pop3.logout();
                    pop3.disconnect();
                    Thread.sleep(3000);
                } catch (Exception e) {
                    log.error(Messages.getMessage("exception00"), e);
                }
            }
        }
        log.info(Messages.getMessage("quit00", "MailServer"));
    }
