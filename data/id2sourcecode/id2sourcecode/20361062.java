        public Object getValueAt(int row, int col) {
            MidiEvent ev = track.get(row);
            MidiMessage m = ev.getMessage();
            switch(col) {
                case TICK:
                    return new Long(ev.getTick());
                case POSITION:
                    return getPosition(ev.getTick());
                case TYPE:
                    return MidiUI.getMessageType(m);
                case CHANNEL:
                    if (ChannelMsg.isChannel(m)) return String.valueOf(1 + ChannelMsg.getChannel(m));
                    break;
                case VALUE:
                    return MidiUI.getMessageValue(m);
            }
            return "";
        }
