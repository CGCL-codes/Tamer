    protected final void displayHeader() {
        displayLicense();
        URL url = AbstractLauncher.class.getResource("/sat4j.version");
        if (url == null) {
            log("no version file found!!!");
        } else {
            BufferedReader in = null;
            try {
                in = new BufferedReader(new InputStreamReader(url.openStream()));
                log("version " + in.readLine());
            } catch (IOException e) {
                log("c ERROR: " + e.getMessage());
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        log("c ERROR: " + e.getMessage());
                    }
                }
            }
        }
        Properties prop = System.getProperties();
        String[] infoskeys = { "java.runtime.name", "java.vm.name", "java.vm.version", "java.vm.vendor", "sun.arch.data.model", "java.version", "os.name", "os.version", "os.arch" };
        for (int i = 0; i < infoskeys.length; i++) {
            String key = infoskeys[i];
            log(key + ((key.length() < 14) ? "\t\t" : "\t") + prop.getProperty(key));
        }
        Runtime runtime = Runtime.getRuntime();
        log("Free memory \t\t" + runtime.freeMemory());
        log("Max memory \t\t" + runtime.maxMemory());
        log("Total memory \t\t" + runtime.totalMemory());
        log("Number of processors \t" + runtime.availableProcessors());
    }
