    private Runnable getJavaMailRunnable(final List<Request> tasks, final File tempJESDir, final Map<String, List<String>> userMessages, final int threadCount, final String server, final int messageCountPerUser, final Properties properties, final Properties configurationProperties, final PasswordAuthenticator senderCredentials) {
        return new Runnable() {

            @SuppressWarnings("CallToThreadDumpStack")
            public void run() {
                InputStream is = null;
                Store store = null;
                try {
                    int previousCount = 0;
                    while (tasks.size() > 0) {
                        final Request request = tasks.remove(0);
                        System.out.println("Checking out " + request.getUsername() + "'s message");
                        Session session = Session.getInstance(properties, senderCredentials);
                        is = new SharedFileInputStream(request.getMessage());
                        SMTPMessage messageSMTP = new SMTPMessage(session, is);
                        messageSMTP.setFrom(new InternetAddress(senderCredentials.getEmailAddress()));
                        messageSMTP.setRecipient(Message.RecipientType.TO, new InternetAddress(request.getUsername() + '@' + server));
                        Transport.send(messageSMTP);
                        File userDirJES = new File(tempJESDir, "users" + File.separator + request.getUsername() + "@" + server);
                        int count = 0;
                        while (!userDirJES.exists()) {
                            System.out.println(userDirJES + " not yet created, sleeping...");
                            try {
                                Thread.sleep(5 * 1000);
                            } catch (InterruptedException ex) {
                            }
                            count++;
                            if (count == 10) {
                                assertTrue(false);
                            }
                        }
                        FilenameFilter ff = new FilenameFilter() {

                            public boolean accept(File directory, String filename) {
                                if (filename.toLowerCase().endsWith(".loc")) {
                                    return userMessages.get(request.getUsername()).add(filename);
                                }
                                return false;
                            }
                        };
                        count = 0;
                        while (userDirJES.listFiles(ff).length == previousCount) {
                            count++;
                            if (count == 10) {
                                assertTrue(false);
                            }
                            System.out.println(request.getUsername() + "'s mail not yet received, sleeping...");
                            try {
                                Thread.sleep(5 * 1000);
                            } catch (InterruptedException ex) {
                            }
                        }
                        previousCount++;
                        if (tasks.isEmpty()) {
                            while (userDirJES.listFiles(ff).length < messageCountPerUser) {
                                System.out.println(userDirJES + " not all user messages received, sleeping...");
                                try {
                                    Thread.sleep(5 * 1000);
                                } catch (InterruptedException ex) {
                                }
                            }
                        }
                    }
                    System.out.println("Going to sleep once more");
                    try {
                        Thread.sleep(5 * 1000);
                    } catch (InterruptedException ex) {
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException ex) {
                        }
                    }
                    if (store != null) {
                        try {
                            store.close();
                        } catch (MessagingException ex) {
                        }
                    }
                }
                if (ai.incrementAndGet() == threadCount) {
                    synchronized (lock) {
                        lock.notify();
                    }
                }
            }
        };
    }
