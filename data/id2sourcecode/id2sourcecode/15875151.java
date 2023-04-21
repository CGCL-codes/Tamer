                public void run() {
                    connector.updateChannels(channelValues.getChannels(channels));
                    connector.updateFadeUpProgress((int) (100 * cue.getFadeLevel() + 0.5));
                }
