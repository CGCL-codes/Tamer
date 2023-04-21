    public LightController() {
        model = new LightModel(new ILightModelConnector() {

            /**
       * This method will update the given channel on the view, with the given value and source.
       * 
       * @param channel The address, value pair of the channel to be updated.
       * @param source The sources of the given channel.
       */
            public void updateChannel(Channel channel, float[] source) {
                view.updateValue(channel, source);
            }

            /**
       * This method will update the channel with the given channel.
       * 
       * @param channel The address, value pair to update.
       */
            public void updateChannel(Channel channel) {
                view.updateValue(channel);
            }

            /**
       * This method will update the given channels on the view with the given values and sources.
       * 
       * @param channels The address, value pairs of the channels to be updated.
       * @param sources The sources for the given channels.
       */
            public void updateChannels(Channel[] channels, float[][] sources) {
                if (view != null) view.updateValues(channels, sources);
            }

            /**
       * This method will update the channels with the given channels.
       *
       * @param channels The address, value pairs to update.
       */
            public void updateChannels(Channel[] channels) {
                view.updateValues(channels);
            }

            /**
       * This method will tell the view to update the cue list. 
       * 
       * <p>TODO: This metohd should probably just pass the current cues to the view,
       * instead of making the view then call back to the model, which then passes
       * the values on.
       */
            public void updateCueList() {
                if (view != null) view.updateCueList();
            }

            /**
       * This method will make the view select a cue, without triggering a transition to that cue.
       * 
       * @param cueNumber The number of the cue to select.
       */
            public void selectCueWithoutTransition(String cue) {
                if (view != null) view.selectCueWithoutTransition(cue);
            }

            /**
       * This method will toggle all data entry on the form.  This is used to prevent data entry during
       * a fade.
       * 
       * @param enabled Passing in true will allow input, and pasing in false is disable input.
       */
            public void toggleDataEntry(boolean enabled) {
                view.toggleDataEntry(enabled);
            }

            /**
       * This method will update the fade up progress bar.
       * 
       * @param precentDone This parameter should vary between 0 and 100.
       */
            public void updateFadeUpProgress(int percentDone) {
                view.updateFadeUpProgress(percentDone);
            }

            /**
       * This method will update the fade down progress bar.
       * 
       * @param precentDone This parameter should vary between 0 and 100.
       */
            public void updateFadeDownProgress(int percentDone) {
                view.updateFadeDownProgress(percentDone);
            }

            /**
       * This method sets the source for the given address.
       * 
       * @param address The address to set the source for.
       * @param source The source to set.
       */
            public void setChannelSource(short address, float[] source) {
                view.setChannelSource(address, source);
            }

            /**
       * This method will set the source for the given channel.  The channel's value will be ignored.
       * 
       * @param channel The channel to set the source on.  The channel's value will be ignored.
       * @param source The source to set.
       */
            public void setChannelSource(Channel channel, float[] source) {
                view.setChannelSource(channel, source);
            }

            /**
       * This method will set the channel sources for the given addresses.
       * 
       * @param addresses The addresses to set the sources for.
       * @param sources The sources to set.
       */
            public void setChannelSources(short[] addresses, float[][] sources) {
                view.setChannelSources(addresses, sources);
            }

            /**
       * This method will set the sources for the given channels to the given sources.
       * 
       * @param channels The channels to set the source on.  The channel's value will be ignored.
       * @param sources The sources to set.
       */
            public void setChannelSources(Channel[] channels, float[][] sources) {
                view.setChannelSources(channels, sources);
            }

            /**
       * This method will notify the user of the given error.
       * 
       * @param error The error, usually including the stack trace.
       * @param desc The description of the error: why this might have occured, what to do about it, 
       * etc.  This will be displayed above the error message to the user.
       */
            public void raiseError(String error, String desc) {
                view.raiseError(error, desc);
            }

            /**
       * This method will notify the user of the given error.  It will use the stack trace from the given exception 
       * as the error message.
       * 
       * @param error The throwable error to use the stack trace from for the error message.
       * @param desc The description of the error: why this might have occured, what to do about it, 
       * etc.  This will be displayed above the error message to the user.
       */
            public void raiseError(Throwable error, String desc) {
                view.raiseError(error, desc);
            }

            /**
       * This method will notify the view if the device has disconnected without the view specifically
       * telling it to disconnect.
       */
            public void deviceDisconnected() {
                view.deviceDisconnected();
            }

            public void updateCueSetList(String[] cueSetNames) {
                view.updateCueSetList(cueSetNames);
            }

            public float getSelectedCueNumber() {
                return view.getSelectedCueNumber();
            }
        });
        view = new LightView(new ILightViewConnector() {

            /**
       * This method will update the given channel from the given source.
       * 
       * @param channel The address, value pair to update.
       * @param source The source of the new value.
       */
            public void setChannelValue(Channel channel, int source) {
                model.setChannelValue(channel, source);
            }

            /**
       * This method will update the series of channels from the given source.
       * 
       * @param channels The address, value pairs to update.
       * @param source The source of the new values.
       */
            public void setChannelValues(Channel[] channels, int source) {
                model.setChannelValues(channels, source);
            }

            /**
       * This method will return the value for the channel with the given address.
       * 
       * @param address The address of the channel value to return.
       */
            public short getChannelValue(short address) {
                return model.getChannelValue(address);
            }

            /**
       * This method will return the values of the given channel addresses.
       * 
       * @param addresses The addresses to return the channel value for.
       * @return The array of values corresponding to the given addresses.
       */
            public short[] getChannelValues(short[] addresses) {
                return model.getChannelValues(addresses);
            }

            /**
       * This method will return all current channels with non-zero values. 
       * 
       * @return The array of all channels with non-zero values.
       */
            public Channel[] getChannels() {
                return model.getChannels();
            }

            /**
       * This method will return all current channels with non-zero values or a fader value of -100.
       * 
       * @return The array of all channels with non-zero or fadervalue = -100 values.
       */
            public Channel[] getChannelsForCue() {
                return model.getChannelsForCue();
            }

            public void createCueFromLive(float oldCueNumber, float newCueNumber, String name, String desc, long fadeUpMillis, long fadeDownMillis) {
                model.createCueFromLive(oldCueNumber, newCueNumber, name, desc, fadeUpMillis, fadeDownMillis);
            }

            /**
       * This method will return an array containing the summaries of all the cues
       * in the current cueset.
       * 
       * <P>TODO: This method should be named getCueSummaries();
       * 
       * @return The array containing the cue summary for every cue in the cueset.
       */
            public String[] getCueNames() {
                return model.getCueNames();
            }

            /**
       * This method will return the highest cue number in the cueset.
       * 
       * @return The highest cue number that currently exists.
       */
            public float getHighestCueNumber() {
                return model.getHighestCueNumber();
            }

            /**
       * This method will start a transition from the old cue to the new cue.
       * 
       * @param oldCueNumber The number of the cue to transition away from.
       * @param newCueNumber The number of the cue to transition to.
       */
            public void cueTransition(float oldCueNumber, float newCueNumber) {
                model.cueTransition(oldCueNumber, newCueNumber);
            }

            public void cueTransition(float cueNumber) {
                model.cueTransition(cueNumber);
            }

            /**
       * This method will go to the given cue without a transition.
       * 
       * @param cueNumber The number of the cue to go to.
       */
            public void goToCue(float cueNumber) {
                model.goToCue(cueNumber);
            }

            /**
       * This method will initiate a show save.
       * 
       * @param filename The file name and path to save the show to.
       */
            public void saveShow(String filename) {
                model.saveShow(filename);
            }

            /**
       * This method will initiate a show load.
       * 
       * @param filename The file name and path to load the show from.
       */
            public void loadShow(String filename) {
                model.loadShow(filename);
            }

            public void saveShow() {
                model.saveShow();
            }

            public void newShow() {
                model.newShow();
            }

            public String getFileName() {
                return model.getFileName();
            }

            /**
       * This method will tell the view to the connect to the USBDMX.com device.
       * 
       * <p>TODO: This method needs to be redone so that the user can select what device they want to connect to.
       * 
       * @return This method will return true if the model sucsessfully connected to the device, and false otherwise.
       */
            public boolean connect(String deviceName) {
                return model.connect(deviceName);
            }

            /**
       * This method will tell the model to disconnect from the current device, and switch to the
       * dummy DMX device.
       */
            public void disconnect() {
                model.disconnect();
            }

            public void addCueSet(String name, String desc, int priority) {
                model.addCueSet(name, desc, priority);
            }

            public void selectCueSet(String cueSetName) {
                model.selectCueSet(cueSetName);
            }

            public String[] getCueSetNames() {
                return model.getCueSetNames();
            }

            public void stopTransition() {
                model.stopTransition();
            }

            public boolean enableNetworkInput() {
                return model.enableNetworkInput();
            }

            public boolean disableNetworkInput() {
                return model.disableNetworkInput();
            }

            public String[] getDevices() {
                return model.getDevices();
            }
        });
        view.makeVisible();
    }
