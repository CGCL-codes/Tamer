    public Object getValueAt(int nRow, int nColumn) {
        if (nColumn < 8) {
            return super.getValueAt(nRow, nColumn);
        }
        Synthesizer device = (Synthesizer) m_devices.get(nRow);
        switch(nColumn) {
            case 8:
                return "" + device.getChannels().length;
            case 9:
                return "" + device.getMaxPolyphony();
            case 10:
                return "" + device.getLatency();
            default:
                return null;
        }
    }
