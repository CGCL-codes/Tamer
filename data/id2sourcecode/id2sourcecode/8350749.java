    private void connectGlobalRegisters(Design design, ConnectionFrame frame) {
        boolean isLittleEndian = EngineThread.getGenericJob().getUnscopedBooleanOptionValue(OptionRegistry.LITTLE_ENDIAN);
        for (Iterator registers = design.getRegisters().iterator(); registers.hasNext(); ) {
            Register register = (Register) registers.next();
            List readList = frame.getReadConnections(register);
            List writeList = frame.getWriteConnections(register);
            if (readList.isEmpty() && writeList.isEmpty()) {
                continue;
            }
            Component regPhys = register.makePhysicalComponent(readList, writeList);
            if (register.getInputSwapper() != null) design.getDesignModule().addComponent(register.getInputSwapper());
            design.getDesignModule().addComponent(regPhys);
            if (register.getOutputSwapper() != null) design.getDesignModule().addComponent(register.getOutputSwapper());
            assert regPhys != null;
            if (!writeList.isEmpty()) {
                final Iterator writePortIter = regPhys.getDataPorts().iterator();
                for (Iterator writeListIter = writeList.iterator(); writeListIter.hasNext(); ) {
                    final RegisterWriteConnection writeConn = (RegisterWriteConnection) writeListIter.next();
                    if (writeConn != null) {
                        assert writePortIter.hasNext() : "Too few ports on register physical (enable)";
                        final Port enablePort = (Port) writePortIter.next();
                        assert writePortIter.hasNext() : "Too few ports on register physical (data)";
                        final Port dataPort = (Port) writePortIter.next();
                        enablePort.setBus(writeConn.getEnable());
                        dataPort.setBus(writeConn.getData());
                    }
                }
            }
            if (!readList.isEmpty()) {
                Bus registerResultBus = null;
                Exit physicalExit = regPhys.getExit(Exit.DONE);
                registerResultBus = (Bus) physicalExit.getDataBuses().get(0);
                for (Iterator rpIter = readList.iterator(); rpIter.hasNext(); ) {
                    RegisterReadConnection rp = (RegisterReadConnection) rpIter.next();
                    if (rp != null) {
                        rp.getDataPort().setBus(registerResultBus);
                    }
                }
            }
        }
    }
