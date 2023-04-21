        public void run() {
            if (ok2check) {
                while (true) {
                    try {
                        Properties updateProps = new Properties();
                        updateProps.load(url.openStream());
                        if (Double.parseDouble(updateProps.getProperty("version", "0.0")) > version) {
                            newerProperties = updateProps;
                            System.out.println("New version available: " + newerProperties.getProperty("version"));
                            System.out.println(newerProperties.getProperty("message"));
                        } else {
                            System.out.println("Version " + version + " is up to date.");
                        }
                    } catch (Exception e) {
                        System.err.println("Exception while checking for updates: ");
                        e.printStackTrace();
                    }
                    try {
                        Thread.sleep(UPDATE_CHECK_PERIOD);
                    } catch (InterruptedException ignored) {
                    }
                }
            }
        }
