    public Object getValueAt(int row, int col) {
        switch(col) {
            case 0:
                return new Long(midiEventList.get(row).getTimeStamp());
            case 1:
                return new Long(Math.round((midiEventList.get(row).getTimeStamp() * 1000000) / Globals.getNanoSecPerTick()));
            case 2:
                return new Integer(midiEventList.get(row).getCommand());
            case 3:
                return new Integer(midiEventList.get(row).getData1());
            case 4:
                return new Integer(midiEventList.get(row).getData2());
            case 5:
                return new Integer(midiEventList.get(row).getChannel());
            default:
                return new Object();
        }
    }
