    private void searchActiveBranch(OneWireHub hub, HubChannel channel, HubPort port, Map<String, HardwareContainer> containerMap) throws OneWireException {
        logger.debug("Searching active branch for 1-Wire hardware containers");
        logger.debug("  Using hub: " + ((hub == null) ? "null" : hub.getDesc()) + ", channel: " + ((channel == null) ? "null" : channel.getChannel()) + ", port: " + ((port == null) ? "null" : port.getPortName()));
        onewireAdapter.setSearchAllDevices();
        onewireAdapter.targetAllFamilies();
        onewireAdapter.excludeFamily(0x001f);
        Enumeration containerList = onewireAdapter.getAllDeviceContainers();
        int counter = 0;
        while (containerList.hasMoreElements()) {
            OneWireContainer container = (OneWireContainer) containerList.nextElement();
            containerMap.put(container.getAddressAsString(), new OneWireContainerImpl(onewireAdapter, hub, channel, port, container));
            counter++;
        }
        logger.debug("  Found " + counter + " 1-Wire hardware containers");
    }
