        public String getChannelName(ValueObject val) {
            return metaData2chan.get(val.getMetadataXml().getValue());
        }
