    public BusControls createFxBusControls(String name, ChannelFormat format) {
        if (!canAddBusses) {
            throw new IllegalStateException("Can't add busses after adding strips");
        }
        ChannelFormat mainFormat = mainBusControls.getChannelFormat();
        if (format == null) {
            format = mainFormat;
        } else if (format.getCount() > mainFormat.getCount()) {
            format = mainFormat;
            System.err.println(name + " Bus limited to Main Bus channel format");
        }
        BusControls busControls = new BusControls(FX_BUS, name, format);
        fxBusControls.add(busControls);
        return busControls;
    }
