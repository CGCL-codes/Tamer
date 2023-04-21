    @Test
    public void checkMultiConnections() throws QcException {
        log.info("Testing multiple connections");
        final IQcConnection lCon = QcConnectionFactory.createConnection(PROPERTIES.getProperty(IArgsConstants.SERVER));
        List<Domain> lDomains;
        try {
            lCon.login(PROPERTIES.getProperty(IArgsConstants.USER), PROPERTIES.getProperty(IArgsConstants.PASSWORD));
            lDomains = lCon.getDomainList();
        } finally {
            lCon.disconnect();
        }
        if (log.isDebugEnabled()) {
            log.debug("Now login...");
        }
        final Set<Thread> lList = new HashSet<Thread>();
        for (final Domain lDomain : lDomains) {
            for (final Project lProject : lDomain.getProjects()) {
                final Thread lThread = new Thread() {

                    @Override
                    public void run() {
                        try {
                            if (log.isDebugEnabled()) {
                                log.debug("Now login into project " + lProject.getName());
                            }
                            final IQcConnection lCon = QcConnectionFactory.createConnection(PROPERTIES.getProperty(IArgsConstants.SERVER));
                            try {
                                lCon.connect(PROPERTIES.getProperty(IArgsConstants.USER), PROPERTIES.getProperty(IArgsConstants.PASSWORD), lDomain.getName(), lProject.getName());
                                ConnectionTest.this.sleep(2);
                                final Collection<QcBug> lBugs = lCon.getBugClient().getBugs();
                                log.info(lBugs.size() + " bug(s) found in project " + lProject.getName());
                            } finally {
                                ConnectionTest.this.sleep(2);
                                lCon.disconnect();
                            }
                        } catch (final QcException e) {
                            log.warn(e.getMessage(), e);
                        }
                    }
                };
                lList.add(lThread);
                lThread.start();
            }
        }
        while (!lList.isEmpty()) {
            final Iterator<Thread> lIter = lList.iterator();
            while (lIter.hasNext()) {
                if (!lIter.next().isAlive()) {
                    lIter.remove();
                }
            }
            try {
                Thread.sleep(1000);
            } catch (final InterruptedException e) {
            }
        }
        log.info("OK");
    }
