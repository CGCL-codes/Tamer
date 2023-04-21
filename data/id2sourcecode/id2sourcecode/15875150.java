        public ViewUpdater(final long period, final Channel[] channels, final CueValueSet cue) {
            threadTimer = new Timer();
            threadTimer.scheduleAtFixedRate(new TimerTask() {

                public void run() {
                    connector.updateChannels(channelValues.getChannels(channels));
                    connector.updateFadeUpProgress((int) (100 * cue.getFadeLevel() + 0.5));
                }
            }, 0, period);
        }
