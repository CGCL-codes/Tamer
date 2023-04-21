        public OutgoingBatchMapper(boolean includeDisabledChannels, boolean limitBasedOnMaxBatchToSend) {
            this.includeDisabledChannels = includeDisabledChannels;
            this.limitBasedOnMaxBatchToSend = limitBasedOnMaxBatchToSend;
            this.channels = configurationService.getChannels(false);
            this.countByChannel = new HashMap<String, Integer>();
        }
