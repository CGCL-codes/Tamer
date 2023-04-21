    public IStringSpectrum getChannelsAttributeModel() {
        return (IStringSpectrum) eventAttributes.get(getDeviceName() + "/" + DevicePoolUtils.MEASUREMENT_GROUP_ATTR_CHANNELS);
    }
