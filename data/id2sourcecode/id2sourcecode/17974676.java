    public C64Bus(readableBus[] readTableCpu, writeableBus[] writeTableCpu, readableBus[] readTableVic, ColorRAM color) {
        setTableCpu(readTableCpu, writeTableCpu);
        setTableVic(readTableVic);
        setColor(color);
    }
