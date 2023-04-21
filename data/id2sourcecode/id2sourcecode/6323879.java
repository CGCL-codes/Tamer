    public void setupVMEmap(final VME_Map vmeMap) {
        final StringBuffer temp = new StringBuffer();
        final List<VME_Channel> eventParams = vmeMap.getEventParameters();
        final Map<Integer, Byte> hRanges = vmeMap.getV775Ranges();
        if (eventParams.isEmpty()) {
            throw new IllegalStateException("No event parameters in map.");
        }
        final int totalParams = eventParams.size();
        final char endl = '\n';
        final char space = ' ';
        final String hex = "0x";
        temp.append(totalParams).append(endl);
        for (VME_Channel channel : eventParams) {
            temp.append(channel.getSlot()).append(space).append(hex).append(Integer.toHexString(channel.getBaseAddress())).append(space).append(channel.getChannel()).append(space).append(channel.getThreshold()).append(endl);
        }
        final int numRanges = hRanges.size();
        temp.append(numRanges).append(endl);
        if (numRanges > 0) {
            for (Map.Entry<Integer, Byte> entry : hRanges.entrySet()) {
                final int base = entry.getKey();
                temp.append(hex).append(Integer.toHexString(base)).append(space).append(entry.getValue()).append(endl);
            }
        }
        temp.append('\0');
        this.vme.sendToVME(PacketTypes.VME_ADDRESS, temp.toString());
    }
