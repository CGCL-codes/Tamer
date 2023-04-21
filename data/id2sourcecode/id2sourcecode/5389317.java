    private void loadJobProperties() {
        URL quartzwizardurl = CorePlugin.getDefault().getBundle().getResource("/org/mss/quartzjobs/wizards/quartzwizard.properties");
        log.debug(" Load Quarz Wizard Properties for QuartzWizardPage2: " + quartzwizardurl.toExternalForm());
        Properties props = new Properties();
        try {
            props.load(quartzwizardurl.openStream());
            for (Iterator it = props.keySet().iterator(); it.hasNext(); ) {
                String cronvalue = (String) it.next();
                String crondesc = props.getProperty(cronvalue);
                schedulecombo.add(cronvalue);
                scheduledescription.add(crondesc);
            }
        } catch (IOException e) {
            log.error("Could not load Quartz Wizard Properties " + quartzwizardurl.toExternalForm());
            e.printStackTrace();
        }
    }
