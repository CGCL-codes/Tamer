        public void putValue(ValueObject val) throws SAPIException {
            if (!metaData2chan.containsKey(val.getMetadataXml().getValue())) {
                registerNewChannel(val);
            }
            tmpTime[0] = val.getTimeStamp().getTime() / 1000.0;
            tmpVal[0] = val.getValue();
            cmap.PutTimes(tmpTime);
            cmap.PutDataAsFloat64(cmap.GetIndex(getChannelName(val)), tmpVal);
            source.Flush(cmap);
        }
