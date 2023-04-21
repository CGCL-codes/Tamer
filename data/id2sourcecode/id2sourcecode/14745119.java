    @Override
    public Collection<ConfigurationOption> getConfigurationOptions() {
        Vector<ConfigurationOption> options = new Vector<ConfigurationOption>();
        options.add(new ConfigurationOption("testing.temp_dir", "Directory for temporary files for the testing framework.  Must be read/write/execute for the Tomcat user.", "/var/lib/boss2/testing"));
        return options;
    }
