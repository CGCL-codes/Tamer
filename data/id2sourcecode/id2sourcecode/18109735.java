    protected boolean isCompatibleDescriptor(ServiceDescriptor d) {
        if (d.getDescription().equals(getString("Reverb")) && getName().indexOf("FX") < 0) {
            return false;
        }
        if (constraintChannelFormat == null) return true;
        if (d instanceof AudioControlServiceDescriptor) {
            AudioControlServiceDescriptor acsd = (AudioControlServiceDescriptor) d;
            ChannelFormat descriptorFormat = acsd.getChannelFormat();
            if (descriptorFormat == null) return true;
            if (descriptorFormat.getCount() > constraintChannelFormat.getCount()) {
                return false;
            }
        }
        return true;
    }
