            private double getValue(final String handle, final Correlation correlation) {
                final String channelID = getChannel(handle).getId();
                final ChannelTimeRecord record = (ChannelTimeRecord) correlation.getRecord(channelID);
                return (record != null) ? record.doubleValue() : Double.NaN;
            }
