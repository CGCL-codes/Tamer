    public Object getValueAt(int row, int col) {
        ConsoleFade cue = getCueIndexed(row);
        switch(col) {
            case 0:
                if (_console.isCurrentCue(cue.getNumber())) return ">"; else return "";
            case 1:
                return Float.toString(cue.getNumber() / (float) 1000);
            default:
                if (cue.getCue().getChannel(col - 2).getLevel() >= 0) return _levelTrans.toString(cue.getCue().getChannel(col - 2).getLevel()); else return "";
        }
    }
