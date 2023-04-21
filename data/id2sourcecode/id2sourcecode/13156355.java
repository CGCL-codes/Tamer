    public void writePort(int port, int value) {
        if (port == 0xfbfe || port == 0xfbee) return;
        int selPort = (port & portHighMask) == portHighTest ? 2 : 0;
        if ((port & portLowMask) == portLowTest) selPort++;
        if (selPort == PORT_CONTROL) {
            if ((value & 0x80) == 0) {
                IOPort ioPort = ports[PORT_C];
                int mask = 1 << ((value >> 1) & 0x07);
                if ((value & 0x01) == 0) ioPort.write(ioPort.readOutput() & (mask ^ 0xff)); else ioPort.write(ioPort.readOutput() | mask);
            } else setControl(value);
        } else ports[selPort].write(value);
    }
