    @Override
    protected IStatus run(IProgressMonitor monitor) {
        final int BUFFER_SIZE = 16;
        StringBuffer urlExceptions = new StringBuffer();
        String[] urlRepos = SpiderPlugin.getDefault().getRepositoryPreference();
        int timeout = SpiderPlugin.getDefault().getRepositoryTimeoutPreference();
        try {
            monitor.beginTask(PropertiesUtil.getMessage("dialog.progressLoadRepository.connecting", "spider"), 800);
            for (String repo : urlRepos) {
                if (monitor.isCanceled()) {
                    return Status.CANCEL_STATUS;
                }
                monitor.subTask(PropertiesUtil.getMessage("dialog.progressLoadRepository.status", new String[] { repo }, "spider"));
                BufferedInputStream buffInputStream = null;
                try {
                    URL url = new URL(repo);
                    URLConnection connection = url.openConnection();
                    connection.setConnectTimeout(timeout);
                    connection.setReadTimeout(timeout);
                    buffInputStream = new BufferedInputStream(connection.getInputStream(), BUFFER_SIZE);
                } catch (IOException e) {
                    urlExceptions.append(repo).append("\n");
                    continue;
                }
                monitor.setTaskName(PropertiesUtil.getMessage("dialog.progressLoadRepository.loading", "spider"));
                StringBuffer repositoyXml = new StringBuffer();
                try {
                    byte[] buf = new byte[BUFFER_SIZE];
                    int readcount = 0;
                    while ((readcount = buffInputStream.read(buf)) != -1) {
                        repositoyXml.append(new String(buf, 0, readcount));
                        monitor.worked(readcount);
                    }
                } finally {
                    buffInputStream.close();
                }
                Repository repository = (Repository) XmlManager.loadXmlFromString(repositoyXml.toString(), XML_TYPE.REPOSITORY);
                repositories.add(repository);
            }
        } catch (Exception e) {
            logger.error(e);
            return new Status(Status.ERROR, SpiderPlugin.ID, e.getMessage());
        } finally {
            if (urlExceptions != null && urlExceptions.length() > 0) {
                this.urlErrorConnection = urlExceptions.toString();
                return new Status(Status.ERROR, SpiderPlugin.ID, urlExceptions.toString());
            }
            monitor.done();
        }
        return Status.OK_STATUS;
    }
