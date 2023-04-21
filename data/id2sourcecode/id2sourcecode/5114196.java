    public void injectCommunicationChannel(PluginCommunicationRuntime comm) {
        if (fieldComChannel == null) {
            return;
        }
        try {
            this.fieldComChannel.setAccessible(true);
            this.fieldComChannel.set(plugin, comm.getChannel());
        } catch (IllegalArgumentException ex) {
            ClubmixerLogger.debugUnexpectedException(this, ex);
        } catch (IllegalAccessException ex) {
            ClubmixerLogger.debugUnexpectedException(this, ex);
        }
    }
