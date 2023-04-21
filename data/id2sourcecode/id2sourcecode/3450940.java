    public void update() {
        if (Fu.DEBUG && FuLog.isDebug()) {
            FuLog.debug("FTR: update start load properties");
        }
        InputStream in = null;
        try {
            final URL url = new URL("http://www.fudaa.fr/prepro/prepro.properties");
            final URLConnection connection = url.openConnection();
            connection.setUseCaches(false);
            for (int i = 0; i < nbTry_ && in == null; i++) {
                connection.connect();
                in = url.openStream();
                try {
                    if (in == null) {
                        Thread.sleep(wait_);
                    }
                } catch (final InterruptedException _evt) {
                }
            }
            if (in == null) {
                isConnected_ = false;
            } else {
                isConnected_ = true;
                final Properties prop = new Properties();
                prop.load(in);
                final String version = prop.getProperty("@version@");
                if (Fu.DEBUG && FuLog.isDebug()) {
                    FuLog.debug("FTR: version read from site " + version);
                }
                if (version != null && version.compareTo(infos_.version) > 0) {
                    isUptoDate_ = false;
                    BuLib.invokeLater(new Runnable() {

                        public void run() {
                            afficheDialogueForUpdate(version);
                        }
                    });
                } else {
                    isUptoDate_ = true;
                }
            }
        } catch (final IOException _evt) {
        } finally {
            FuLib.safeClose(in);
        }
    }
