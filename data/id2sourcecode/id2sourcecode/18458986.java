    public MemoryGateway(LogicalMemoryPort resource, int reads, int writes, int maxAddressWidth) {
        super(resource);
        assert (writes + reads) > 0 : "Illegal number of accesses: " + Integer.toString(writes + reads) + " for memory gateway";
        assert (resource != null) : "Can't create a gateway for a null LogicalMemoryPort";
        final StructuralMemory structMem = resource.getStructuralMemory();
        final int dataWidth = structMem.getDataWidth();
        final int sizeWidth = LogicalMemory.SIZE_WIDTH;
        final String baseName = ID.showLogical(resource);
        Bus internalReadEnable = null;
        Bus internalAddress = null;
        Bus internalSize = null;
        Bus internalWriteEnable = null;
        Bus internalWriteData = null;
        Exit exit = makeExit(0);
        exit.setLatency(Latency.ZERO);
        final Bus gatedDataBus;
        final Bus gatedDoneBus = exit.getDoneBus();
        Mux addressMux = null;
        Mux sizeMux = null;
        Mux dataMux = null;
        Or readOr = null;
        Or writeOr = null;
        if (reads > 0) {
            gatedDataBus = exit.makeDataBus();
            gatedDataBus.setSize(dataWidth, false);
            gatedDataBus.setIDLogical(baseName + "_DATA");
        } else {
            gatedDataBus = null;
        }
        if (writes > 1) {
            dataMux = new Mux(writes);
            dataMux.setIDLogical(baseName + "_dmux");
            Bus dataMuxBus = dataMux.getResultBus();
            dataMuxBus.setIDLogical(baseName + "_dmux");
            writeOr = new Or(writes);
            Bus writeOrBus = writeOr.getResultBus();
            writeOrBus.setIDLogical(baseName + "_wor");
            addComponent(dataMux);
            addComponent(writeOr);
            internalWriteEnable = writeOrBus;
            internalWriteData = dataMuxBus;
        }
        if (reads > 1) {
            readOr = new Or(reads);
            Bus readOrBus = readOr.getResultBus();
            readOrBus.setIDLogical(baseName + "_ror");
            addComponent(readOr);
            internalReadEnable = readOrBus;
        }
        if ((reads + writes) > 1) {
            addressMux = new Mux(reads + writes);
            addressMux.setIDLogical(baseName + "_amux");
            Bus addressMuxBus = addressMux.getResultBus();
            addressMuxBus.setIDLogical(baseName + "_amux");
            addComponent(addressMux);
            internalAddress = addressMuxBus;
            sizeMux = new Mux(reads + writes);
            sizeMux.setIDLogical(baseName + "_smux");
            internalSize = sizeMux.getResultBus();
            internalSize.setIDLogical(baseName + "_smux");
            addComponent(sizeMux);
        }
        readSlots = new ArrayList(reads);
        writeSlots = new ArrayList(writes);
        Iterator addressMuxPorts = (addressMux != null) ? addressMux.getGoPorts().iterator() : null;
        Iterator sizeMuxPorts = (sizeMux != null) ? sizeMux.getGoPorts().iterator() : null;
        if (writes > 0) {
            Iterator dataMuxPorts = (dataMux != null) ? dataMux.getGoPorts().iterator() : null;
            Iterator writeOrPorts = (writeOr != null) ? writeOr.getDataPorts().iterator() : null;
            for (int i = 0; i < writes; i++) {
                Port localWriteEnablePort = makeDataPort();
                Port localAddressPort = makeDataPort();
                Port localDataPort = makeDataPort();
                Port localSizePort = makeDataPort();
                writeSlots.add(new WriteSlot(localWriteEnablePort, localAddressPort, localDataPort, localSizePort, gatedDoneBus));
                if (addressMux != null) {
                    Port addressMuxGoPort = (Port) addressMuxPorts.next();
                    addressMuxGoPort.setBus(localWriteEnablePort.getPeer());
                    Port addressMuxDataPort = addressMux.getDataPort(addressMuxGoPort);
                    addressMuxDataPort.setBus(localAddressPort.getPeer());
                } else {
                    internalAddress = localAddressPort.getPeer();
                }
                if (sizeMux != null) {
                    Port p = (Port) sizeMuxPorts.next();
                    p.setBus(localWriteEnablePort.getPeer());
                    p = sizeMux.getDataPort(p);
                    p.setBus(localSizePort.getPeer());
                } else {
                    internalSize = localSizePort.getPeer();
                }
                final CastOp castOp = new CastOp(dataWidth, false);
                castOp.getDataPort().setBus(localDataPort.getPeer());
                addComponent(castOp);
                if (dataMux != null) {
                    Port dataMuxGoPort = (Port) dataMuxPorts.next();
                    dataMuxGoPort.setBus(localWriteEnablePort.getPeer());
                    Port dataMuxDataPort = dataMux.getDataPort(dataMuxGoPort);
                    dataMuxDataPort.setBus(castOp.getResultBus());
                } else {
                    internalWriteData = castOp.getResultBus();
                }
                if (writeOr != null) {
                    Port orDataPort = (Port) writeOrPorts.next();
                    orDataPort.setBus(localWriteEnablePort.getPeer());
                } else {
                    internalWriteEnable = localWriteEnablePort.getPeer();
                }
            }
        }
        if (reads > 0) {
            Iterator readOrPorts = (readOr != null) ? readOr.getDataPorts().iterator() : null;
            for (int i = 0; i < reads; i++) {
                Port localReadEnablePort = makeDataPort();
                Port localAddressPort = makeDataPort();
                Port localSizePort = makeDataPort();
                readSlots.add(new ReadSlot(localReadEnablePort, localAddressPort, localSizePort, gatedDoneBus, gatedDataBus));
                if (addressMux != null) {
                    Port addressMuxGoPort = (Port) addressMuxPorts.next();
                    addressMuxGoPort.setBus(localReadEnablePort.getPeer());
                    Port addressMuxDataPort = addressMux.getDataPort(addressMuxGoPort);
                    addressMuxDataPort.setBus(localAddressPort.getPeer());
                } else {
                    internalAddress = localAddressPort.getPeer();
                }
                if (sizeMux != null) {
                    Port p = (Port) sizeMuxPorts.next();
                    p.setBus(localReadEnablePort.getPeer());
                    p = sizeMux.getDataPort(p);
                    p.setBus(localSizePort.getPeer());
                } else {
                    internalSize = localSizePort.getPeer();
                }
                if (readOr != null) {
                    Port orDataPort = (Port) readOrPorts.next();
                    orDataPort.setBus(localReadEnablePort.getPeer());
                } else {
                    internalReadEnable = localReadEnablePort.getPeer();
                }
            }
        }
        memoryDonePort = makeDataPort(Component.SIDEBAND);
        Bus memoryDonePortBus = memoryDonePort.getPeer();
        gatedDoneBus.getPeer().setBus(memoryDonePortBus);
        if (reads > 0) {
            memoryReadEnableBus = exit.makeDataBus(Component.SIDEBAND);
            memoryReadEnableBus.setSize(1, false);
            memoryReadEnableBus.setIDLogical(baseName + "_SIDE_RE");
            memoryDataReadPort = makeDataPort(Component.SIDEBAND);
            Bus memoryDataReadPortBus = memoryDataReadPort.getPeer();
            memoryReadEnableBus.getPeer().setBus(internalReadEnable);
            gatedDataBus.getPeer().setBus(memoryDataReadPortBus);
        }
        if (writes > 0) {
            memoryWriteEnableBus = exit.makeDataBus(Component.SIDEBAND);
            memoryWriteEnableBus.setSize(1, false);
            memoryWriteEnableBus.setIDLogical(baseName + "_SIDE__WE");
            memoryDataWriteBus = exit.makeDataBus(Component.SIDEBAND);
            memoryDataWriteBus.setSize(dataWidth, false);
            memoryDataWriteBus.setIDLogical(baseName + "_SIDE__WDATA");
            memoryWriteEnableBus.getPeer().setBus(internalWriteEnable);
            memoryDataWriteBus.getPeer().setBus(internalWriteData);
        }
        memoryAddressBus = exit.makeDataBus(Component.SIDEBAND);
        memoryAddressBus.setSize(maxAddressWidth, false);
        memoryAddressBus.setIDLogical(baseName + "_SIDE_ADDR");
        memoryAddressBus.getPeer().setBus(internalAddress);
        memorySizeBus = exit.makeDataBus(Component.SIDEBAND);
        memorySizeBus.setSize(sizeWidth, false);
        memorySizeBus.setIDLogical(baseName + "_SIDE_SIZE");
        memorySizeBus.getPeer().setBus(internalSize);
    }
