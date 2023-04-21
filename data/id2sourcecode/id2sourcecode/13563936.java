    public void configure() throws ConfigurationException {
        log.info("configuring eaiframework ...");
        channelsConfigHelper = new ChannelsConfigHelper();
        filtersDescriptorsConfigHelper = new FiltersDescriptorsConfigHelper();
        filtersConfigHelper = new FiltersConfigHelper();
        checkDependencies();
        channelsConfigHelper.setChannelManager(channelManager);
        filtersDescriptorsConfigHelper.setFilterDescriptorRegistry(filterDescriptorRegistry);
        filtersConfigHelper.setFilterManager(filterManager);
        configureIncludedFilterDescriptors();
        try {
            InputStream[] channelsConfigFiles = this.getChannelsInputStreams();
            log.debug("configuring " + channelsConfigFiles.length + " channel config files ... ");
            for (InputStream is : channelsConfigFiles) {
                channelsConfigHelper.configure(is);
            }
        } catch (IOException e) {
            channelManager.destroyAllChannels();
            throw new ConfigurationException(e);
        }
        try {
            InputStream[] filtersDescriptorsConfigFiles = this.getFiltersDescriptorsInputStreams();
            log.debug("configuring " + filtersDescriptorsConfigFiles.length + " filter descriptors config files ... ");
            for (InputStream is : filtersDescriptorsConfigFiles) {
                filtersDescriptorsConfigHelper.configure(is);
            }
        } catch (IOException e) {
            channelManager.destroyAllChannels();
            throw new ConfigurationException(e);
        }
        try {
            InputStream[] filtersConfigFiles = this.getFiltersInputStreams();
            log.debug("configuring " + filtersConfigFiles.length + " filters config files ... ");
            for (InputStream is : filtersConfigFiles) {
                filtersConfigHelper.configure(is);
            }
        } catch (IOException e) {
            channelManager.destroyAllChannels();
            filterManager.destroyAllFilters();
            throw new ConfigurationException(e);
        }
        log.info("<< eaiframework configured >>");
    }
