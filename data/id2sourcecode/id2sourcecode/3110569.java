    private void logRotate() {
        try {
            if ("slave".equals(HAConfiguration.getStatus())) {
                return;
            }
            RuleManager _rm = new RuleManager();
            for (Map<String, String> _listener : _rm.getAllListeners()) {
                File _log = RepositoryListener.getListenerFile(_listener.get("repository"), "log");
                if (_log.exists()) {
                    if (new FileSystemFile(_log).getSize() > 10000) {
                        for (int i = 1; i <= 5; i++) {
                            File _f = new File(_log.getAbsolutePath() + "." + i + ".gz");
                            if (_f.exists()) {
                                FileUtils.copyFile(_f, new File(_log.getAbsolutePath() + "." + (i + 1) + ".gz"));
                            }
                        }
                        FileUtils.copyFile(_log, new File(_log.getAbsolutePath() + ".1"));
                        FileUtils.gzip(new File(_log.getAbsolutePath() + ".1"));
                        FileUtils.empty(_log);
                    }
                }
            }
        } catch (Exception _ex) {
        }
    }
