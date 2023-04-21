    public void execute(final List<Request> tasks, String testCase, String accountsXML, int threadCount, boolean shutdown, final String server, boolean addedSmtpProcessor, final List<String> usernames) throws Exception {
        String localRepo = System.getProperty("localRepository");
        String baseDir = System.getProperty("basedir");
        int count = 0;
        File tempJESDirTemp = null;
        do {
            tempJESDirTemp = new File(System.getProperty("java.io.tmpdir"), "jes" + count);
            if (tempJESDirTemp.mkdir()) {
                break;
            }
            count++;
            if (count == 1000) System.exit(1000);
        } while (true);
        final File tempJESDir = tempJESDirTemp;
        File testJESDir = new File(baseDir, "target" + File.separator + "test-classes" + File.separator + testCase);
        final File accountsDir = new File(baseDir, "target" + File.separator + "test-classes");
        TarUtils.untar(accountsDir + File.separator + "account.tar.gz", accountsDir);
        FileUtils.copyFile(new File(accountsDir, accountsXML), new File(accountsDir, "accounts" + File.separator + "mail" + File.separator + "account.xml"));
        Utils.ensureAllAcountMailDirsExist(new File(accountsDir + File.separator + "accounts" + File.separator + "mail"));
        TarUtils.untar(accountsDir + File.separator + "accounts" + File.separator + "mail" + File.separator + "101.tar.gz", new File(accountsDir + File.separator + "accounts" + File.separator + "mail"));
        Utils.copyFiles(testJESDir, tempJESDir);
        File lib = new File(tempJESDir, "lib");
        lib.mkdir();
        File forTest = new File(baseDir, "forTest");
        forTest.mkdir();
        String[] surefirePathElements = System.getProperty("surefire.test.class.path").split(File.pathSeparator);
        File aFile;
        for (int i = 0; i < surefirePathElements.length; i++) {
            aFile = new File(surefirePathElements[i]);
            if (surefirePathElements[i].contains("commons-codec-1.4-SNAPSHOT") || surefirePathElements[i].contains("commons-logging-1.1.1") || surefirePathElements[i].contains("dnsjava-2.0.6") || surefirePathElements[i].contains("log4j-1.2.15")) {
                FileUtils.copyFile(aFile, new File(forTest, aFile.getName()));
            }
        }
        Utils.copyFiles(forTest, lib);
        File testJESFile = new File(baseDir, "pom.xml");
        BufferedReader br = null;
        String line = null, name = null, version = null, pckg = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(testJESFile), System.getProperty("file.encoding")));
            count = 0;
            do {
                line = br.readLine().trim();
                if (line.startsWith("<version>")) {
                    version = line.substring(line.indexOf(">") + 1, line.lastIndexOf("<"));
                } else if (line.startsWith("<name>")) {
                    name = line.substring(line.indexOf(">") + 1, line.lastIndexOf("<"));
                } else if (line.startsWith("<packaging>")) {
                    pckg = line.substring(line.indexOf(">") + 1, line.lastIndexOf("<"));
                }
                if (version != null && name != null && pckg != null) {
                    break;
                } else if (count == 30) {
                    throw new IOException();
                }
                count++;
            } while (true);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ioe) {
                }
            }
        }
        testJESFile = new File(baseDir, "target" + File.separator + name + "-" + version + "." + pckg);
        FileUtils.copyFile(testJESFile, new File(tempJESDir, testJESFile.getName()));
        URL[] urls = new URL[] { new URL("jar:file:" + localRepo + "/org/columba/1.4/columba-1.4.jar!/"), new URL("jar:file:" + localRepo + "/org/ristretto/1.2-all/ristretto-1.2-all.jar!/"), new URL("jar:file:" + localRepo + "/lucene/lucene/1.4.3/lucene-1.4.3.jar!/"), new URL("file:" + baseDir + "/target/test-classes/com/ericdaugherty/mail/CreateAndSendColumbaSMTPMessage.class") };
        final URLClassLoader cl = new JESTestClassLoader(urls);
        System.setProperty("java.security.policy", tempJESDir.getPath() + File.separator + "jes.policy");
        System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.Log4JLogger");
        System.setProperty("log4j.configuration", "file:" + tempJESDir.getPath() + File.separator + "conf" + File.separator + "log4j.properties");
        System.setProperty("log4j.debug", "");
        com.ericdaugherty.mail.server.Mail.main(new String[] { tempJESDir.getPath(), addedSmtpProcessor ? "testing" : "" });
        final Map<String, List<String>> userMessages = new HashMap(4, 0.75f);
        userMessages.put("ernest", new CopyOnWriteArrayList());
        userMessages.put("kara", new CopyOnWriteArrayList());
        userMessages.put("perl", new CopyOnWriteArrayList());
        userMessages.put("wizard", new CopyOnWriteArrayList());
        Class clax = cl.loadClass("org.columba.core.xml.XmlIO");
        Object X = clax.getConstructor().newInstance();
        Method xMethod = clax.getMethod("load", URL.class);
        xMethod.invoke(X, new URL("file:" + accountsDir.getPath() + File.separator + "accounts" + File.separator + "mail" + File.separator + "account.xml"));
        Method xMethodR = clax.getMethod("getRoot");
        Object root = xMethodR.invoke(X);
        clax = cl.loadClass("org.columba.core.xml.XmlElement");
        xMethodR = clax.getMethod("getElement", String.class);
        root = xMethodR.invoke(root, "accountlist");
        xMethodR = clax.getMethod("getElement", int.class);
        root = xMethodR.invoke(root, 0);
        Class clazz = cl.loadClass("com.ericdaugherty.mail.CreateAndSendColumbaSMTPMessage");
        Object object = clazz.getConstructor(File.class).newInstance(accountsDir);
        Method method = clazz.getMethod("execute", String.class, cl.loadClass("org.columba.ristretto.io.Source"), cl.loadClass("org.columba.mail.config.AccountItem"), String.class);
        Method methodc = clazz.getMethod("conclude");
        List<Thread> threads = new ArrayList(threadCount);
        List<Request> requests;
        Thread aThread;
        Random random = new Random();
        for (int i = 0; i < threadCount; i++) {
            requests = new ArrayList();
            for (int j = 0; j < tasks.size(); j++) {
                Request request = new Request(tasks.get(j).getUsername(), new ByteBufferSource(((ByteBufferSource) tasks.get(j).getMessage()).arrayCopy()));
                requests.add(request);
            }
            int shufflingCount = Math.max(threadCount * 4, 8);
            for (int j = 0; j < shufflingCount; j++) {
                requests.add(requests.remove(random.nextInt(threadCount)));
            }
            aThread = getColumbaThread(cl, accountsDir, requests, tempJESDir, userMessages, threadCount, clazz, object, cl.loadClass("org.columba.mail.config.AccountItem").getConstructor(cl.loadClass("org.columba.core.xml.XmlElement")).newInstance(root), method, methodc, tasks.size() / 4 * threadCount, server);
            aThread.setContextClassLoader(cl);
            threads.add(aThread);
        }
        for (int i = 0; i < threadCount; i++) {
            threads.get(i).start();
        }
        synchronized (lock) {
            try {
                lock.wait();
            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
            }
        }
        if (shutdown) {
            try {
                Thread.sleep(2 * 1000);
            } catch (InterruptedException ex) {
            }
            com.ericdaugherty.mail.server.Mail.getInstance().shutdown();
            try {
                Thread.sleep(15 * 1000);
            } catch (InterruptedException ex) {
            }
        }
        File usersDir = new File(tempJESDir, "users");
        FileFilter ff = new FileFilter() {

            public boolean accept(File file) {
                if (file.isDirectory()) {
                    Iterator<String> iter = usernames.iterator();
                    String filename;
                    while (iter.hasNext()) {
                        filename = file.getName();
                        if (filename.startsWith(iter.next()) && filename.endsWith(server)) return true;
                    }
                    return false;
                }
                return false;
            }
        };
        File[] users = usersDir.listFiles(ff);
        ff = new FileFilter() {

            public boolean accept(File file) {
                if (!file.isDirectory() && file.getPath().toLowerCase().endsWith(".loc")) return true;
                return false;
            }
        };
        File[] messages;
        for (int i = 0; i < users.length; i++) {
            messages = users[i].listFiles(ff);
            assertEquals(messages.length, tasks.size() / 4 * threadCount);
            long messageSize = messages[0].length();
            for (int j = 1; j < messages.length; j++) {
                assertEquals(messageSize, messages[j].length());
            }
        }
        deleteAccountFiles(new File(accountsDir + File.separator + "accounts" + File.separator + "mail" + File.separator + "101"));
        deleteAccountFiles(new File(accountsDir + File.separator + "accounts" + File.separator + "mail" + File.separator + "102"));
        deleteAccountFiles(new File(accountsDir + File.separator + "accounts" + File.separator + "mail" + File.separator + "103"));
        deleteAccountFiles(new File(accountsDir + File.separator + "accounts" + File.separator + "mail" + File.separator + "104"));
        deleteAccountFiles(new File(accountsDir + File.separator + "accounts" + File.separator + "mail" + File.separator + "105"));
        deleteAccountFiles(new File(accountsDir + File.separator + "accounts" + File.separator + "mail" + File.separator + "107"));
        File toDelete = new File(accountsDir + File.separator + "accounts");
        Utils.deleteFiles(toDelete);
        toDelete.delete();
        Utils.deleteFiles(tempJESDir);
        tempJESDir.delete();
        Utils.deleteFiles(forTest);
        forTest.delete();
        org.columba.mail.pop3.POP3ServerCollection.shutdown();
        org.columba.mail.config.MailConfig.shutdown();
    }
