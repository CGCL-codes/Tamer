    public void notify(final Object sender, final int msg, final Object arg) {
        switch(msg) {
            case APP_INIT:
                System.out.println("APP_INIT");
                context = (IApplicationContext) arg;
                dataDir = new File(context.getDataDirectory());
                break;
            case APP_START:
                System.out.println("APP_START");
                try {
                    String host = context.getChannelURL().getHost();
                    if (!dataDir.exists()) {
                        dataDir.mkdirs();
                    } else if (new File(dataDir, "webapps").exists()) {
                        delete(new File(dataDir, "webapps"));
                    }
                    tunerConfig = (IConfig) context.getFeature("config");
                    Utils.installChannelDLLs(tunerConfig, context);
                    copyChannelFiles("webapps");
                    Config c = null;
                    if (!new File(dataDir, SERVER_CONFIG_FILE).exists()) {
                        c = new Config();
                        c.initFrom(context.getConfiguration());
                    }
                    service = new BackupService(dataDir, c);
                    service.start();
                } catch (IOException ex) {
                    System.out.println("Exception starting service");
                    ex.printStackTrace();
                    context.stop();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    context.stop();
                }
                break;
            case APP_ARGV:
                System.out.println("APP_ARGV");
                break;
            case APP_DATA_AVAILABLE:
                System.out.println("APP_DATA_AVAILABLE");
                break;
            case APP_DATA_NONE_AVAILABLE:
                break;
            case APP_DATA_INSTALLED:
                break;
            case APP_STOP:
                stop();
                break;
        }
    }
