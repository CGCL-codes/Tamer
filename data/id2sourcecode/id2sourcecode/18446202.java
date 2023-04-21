    private void nameScanForDevicesToStart(String[] args, ArrayList<String> driversToStart) throws IOException {
        Enumeration<URL> driverresources = Thread.currentThread().getContextClassLoader().getResources("META-INF/indi");
        while (driverresources.hasMoreElements()) {
            URL url = driverresources.nextElement();
            PropertyResourceBundle bundle = new PropertyResourceBundle(url.openStream());
            Enumeration<String> keys = bundle.getKeys();
            while (keys.hasMoreElements()) {
                String driverClassName = keys.nextElement();
                String driverName = bundle.getString(driverClassName);
                boolean driverActivated = false;
                for (int index = 0; index < args.length; index++) {
                    if (args[index].equals(driverName)) {
                        driversToStart.add(driverClassName);
                        driverActivated = true;
                        args[index] = "";
                    }
                }
                if (!driverActivated) {
                    this.log.info("detected deacivated driver for " + driverName + " (" + driverClassName + ")");
                } else {
                    this.log.info("detected acivated driver for " + driverName + " (" + driverClassName + ")");
                }
            }
        }
    }
