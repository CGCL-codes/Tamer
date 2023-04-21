    public void doIt() {
        String in = "";
        boolean header = true;
        if (!connected) return;
        String[] units = new String[0];
        try {
            parseHeaders();
            String[] channelNameArray = new String[getChannelCount()];
            ChannelMap map = new ChannelMap();
            populateChannelMap(map, channelNameArray);
            source.Register(map);
            String[] st = null;
            String tsString;
            while (((in = rd.readLine()) != null) && connected) {
                int index = 0;
                st = in.split(delimiter);
                while (st[index].length() == 0) index++;
                tsString = st[index++];
                ISOtoRbnbTime timestamp = new ISOtoRbnbTime(tsString);
                if (!timestamp.is_valid) {
                    if (log.isWarnEnabled()) {
                        log.warn("Warning: timestamp not valid: " + tsString);
                    }
                }
                double time = timestamp.getValue();
                map.PutTime(time + timeOffset, 0.0);
                if (log.isDebugEnabled()) {
                }
                double[] data = new double[1];
                int chanIndex;
                String item;
                for (int j = 0; index < st.length; j++, index++) {
                    data = new double[1];
                    item = st[index].trim();
                    data[0] = Double.parseDouble(item);
                    try {
                        chanIndex = map.GetIndex(channelNameArray[j]);
                        map.PutDataAsFloat64(chanIndex, data);
                    } catch (SAPIException se) {
                        log.error("SAPI exception: " + se.getMessage());
                    } catch (NumberFormatException ignore) {
                        log.error("NumberFormatException " + ignore.getMessage());
                    } catch (Exception ex) {
                        log.error("Exception " + ex.getMessage());
                    }
                }
                source.Flush(map, true);
                Thread.sleep(0);
                try {
                    if (replayInterval > 0) Thread.sleep(replayInterval);
                } catch (Exception ignore) {
                }
            }
            String unitsArray[];
            unitsArray = getUnitsArray(channelNameArray.length);
            postUnits(channelNameArray, unitsArray);
        } catch (Exception e) {
            log.error("Genral error parsing the file : " + e.getMessage());
        }
    }
