        public ViewUpdater(final long period, final Channel[] channels, final CueValueSet fadeUpCue, final CueValueSet fadeDownCue) {
            threadTimer = new Timer();
            threadTimer.scheduleAtFixedRate(new TimerTask() {

                public void run() {
                    connector.updateChannels(channelValues.getChannels(channels));
                    connector.updateFadeUpProgress((int) (100 * fadeUpCue.getFadeLevel() + 0.5));
                    connector.updateFadeDownProgress((int) (100 * fadeDownCue.getFadeLevel() + 0.5));
                }
            }, 0, period);
        }
