    public Object getValueAt(int row, int col) {
        if (col == EVTNO_COLUMN) return new Integer(row); else {
            ScrEvent temp = explode.getEventAt(row);
            int value;
            switch(col) {
                case TIME_COLUMN:
                    value = temp.getTime();
                    break;
                case PITCH_COLUMN:
                    value = temp.getPitch();
                    break;
                case DURATION_COLUMN:
                    value = temp.getDuration();
                    break;
                case VELOCITY_COLUMN:
                    value = temp.getVelocity();
                    break;
                case CHANNEL_COLUMN:
                    value = temp.getChannel();
                    break;
                default:
                    value = 0;
                    break;
            }
            return new Integer(value);
        }
    }
