    private static IQcConnection createConnection(final String pServerURL, final String pUser, final String pPassword, final String pDomain, final String pProject, final IProgressMonitor pMonitor) throws QcException {
        try {
            pMonitor.worked(5);
            if (log.isDebugEnabled()) {
                log.debug("Creating connection for repository " + pServerURL);
            }
            pMonitor.subTask(Messages.QcConnectionManager_Creating_Connection_To + pServerURL);
            final IQcConnection lCon = QcConnectionFactory.createConnection(pServerURL);
            pMonitor.worked(5);
            if (StringUtils.isBlank(pDomain) && StringUtils.isBlank(pProject)) {
                lCon.login(pUser, pPassword);
            } else if (StringUtils.isBlank(pDomain) || StringUtils.isBlank(pProject)) {
                throw new QcException("Domain/Project not set");
            } else {
                lCon.connect(pUser, pPassword, pDomain, pProject);
            }
            pMonitor.worked(10);
            pMonitor.subTask(Messages.QcConnectionManager_Connection_Gained);
            return lCon;
        } finally {
            pMonitor.done();
        }
    }
