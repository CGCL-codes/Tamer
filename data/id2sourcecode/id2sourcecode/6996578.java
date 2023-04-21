    protected void monitorSignals(double binTimespan) {
        if (_correlator != null) {
            _correlator.dispose();
        }
        _correlator = new ChannelCorrelator(binTimespan);
        _channelTable = new HashMap<String, Channel>(3);
        _xAvgChannel = monitorChannel(BPM.X_AVG_HANDLE);
        _yAvgChannel = monitorChannel(BPM.Y_AVG_HANDLE);
        _ampAvgChannel = monitorChannel(BPM.AMP_AVG_HANDLE, null);
        _correlator.addListener(new CorrelationNotice() {

            final String X_AVG_ID = _xAvgChannel.getId();

            final String Y_AVG_ID = _xAvgChannel.getId();

            final String AMP_AVG_ID = _ampAvgChannel.getId();

            /**
				 * Handle the correlation event. This method gets called when a correlation was posted.
				 * @param sender       The poster of the correlation event.
				 * @param correlation  The correlation that was posted.
				 */
            public void newCorrelation(Object sender, Correlation correlation) {
                final Date timestamp = correlation.meanDate();
                final double xAvg = getValue(BPM.X_AVG_HANDLE, correlation);
                if (xAvg == Double.NaN) {
                    return;
                }
                final double yAvg = getValue(BPM.Y_AVG_HANDLE, correlation);
                if (yAvg == Double.NaN) {
                    return;
                }
                final double ampAvg = getValue(BPM.AMP_AVG_HANDLE, correlation);
                if (ampAvg == Double.NaN) {
                    return;
                }
                final BpmRecord record = new BpmRecord(BpmAgent.this, timestamp, xAvg, yAvg, ampAvg);
                synchronized (_eventLock) {
                    _lastRecord = record;
                    EVENT_PROXY.stateChanged(BpmAgent.this, record);
                }
            }

            /**
				 * Handle the no correlation event. This method gets called when no correlation was found within some prescribed time period.
				 * @param sender  The poster of the "no correlation" event.
				 */
            public void noCorrelationCaught(Object sender) {
                System.out.println("No BPM event.");
            }

            /**
				 * Get the value for the specified field from the correlation.
				 * @param handle       the handle of the BPM field
				 * @param correlation  the correlation with the correlated data for the BPM event
				 * @return             the correlation's BPM field value corresponding to the handle
				 */
            private double getValue(final String handle, final Correlation correlation) {
                final String channelID = getChannel(handle).getId();
                final ChannelTimeRecord record = (ChannelTimeRecord) correlation.getRecord(channelID);
                return (record != null) ? record.doubleValue() : Double.NaN;
            }
        });
        _correlator.startMonitoring();
    }
