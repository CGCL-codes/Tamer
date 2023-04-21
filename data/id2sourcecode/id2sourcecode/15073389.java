    private static IQcConnection createConnection(final String pServerURL, final String pUser, final String pPassword, final String pDomain, final String pProject) throws QcException {
        final IQcConnection lCon = QcConnectionFactory.createConnection(pServerURL);
        if (StringUtils.isBlank(pDomain) && StringUtils.isBlank(pProject)) {
            lCon.login(pUser, pPassword);
        } else if (StringUtils.isBlank(pDomain) || StringUtils.isBlank(pProject)) {
            throw new QcException("Domain/Project not set");
        } else {
            lCon.connect(pUser, pPassword, pDomain, pProject);
        }
        return lCon;
    }
