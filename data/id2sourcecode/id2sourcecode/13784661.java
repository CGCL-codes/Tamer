    public final Map<String, HardwareContainer> loadContainers() throws HardwareDriverException {
        logger.debug("Loading 1-Wire hardware containers");
        Map<String, HardwareContainer> containerMap = new HashMap<String, HardwareContainer>();
        try {
            onewireAdapter.beginExclusive(true);
            this.resetNetwork();
            onewireAdapter.setSearchAllDevices();
            onewireAdapter.targetAllFamilies();
            onewireAdapter.targetFamily(0x001F);
            Enumeration owdEnum;
            OneWireContainer owd;
            if (onewireAdapter.findFirstDevice()) {
                for (owdEnum = onewireAdapter.getAllDeviceContainers(); owdEnum.hasMoreElements(); ) {
                    owd = (OneWireContainer) owdEnum.nextElement();
                    logger.debug("MicroLAN Coupler Found at " + owd.getAddressAsString());
                    OneWireHub hub = this.findHub(owd.getAddressAsString());
                    HubChannel channel = hub.getChannel(owd.getAddressAsString());
                    hub.activateChannel(channel, HubPort.MAIN);
                    searchActiveBranch(hub, channel, HubPort.MAIN, containerMap);
                    hub.deactivateChannel(channel, HubPort.MAIN);
                    hub.activateChannel(channel, HubPort.AUX);
                    searchActiveBranch(hub, channel, HubPort.AUX, containerMap);
                    hub.deactivateChannel(channel, HubPort.AUX);
                }
            } else {
                searchActiveBranch(containerMap);
            }
            logger.debug("Loaded " + containerMap.size() + " 1-Wire hardware containers");
            return containerMap;
        } catch (OneWireException owe) {
            logger.error("1-Wire exception while detecting hardware: " + owe.getMessage());
            throw new HardwareDriverException(owe);
        } finally {
            onewireAdapter.endExclusive();
        }
    }
