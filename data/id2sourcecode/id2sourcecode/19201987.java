    public Object getValueAt(int row, int columnIndex) {
        if (columnIndex == 0) {
            long tick = getTickForRow(row);
            double beat = tick / (double) sequence.getResolution();
            if (beat % 1 == 0) return (timeUtils.tickToBarBeat(tick)); else return "";
        } else {
            int col = tableColumnToTrackerColumn(columnIndex);
            int eventCol = (columnIndex - 1) % COLUMNS;
            MultiEvent me = getCellEvent(row, col);
            if (me != null) {
                if (eventCol == COLUMN_TIME) {
                    long relativeTick = me.getStartTick() - startTick;
                    long rowTick = (long) (row * ticksPerRow);
                    return ((relativeTick - rowTick) / ticksPerRow);
                } else {
                    if (me instanceof ChannelEvent) {
                        ChannelEvent event = (ChannelEvent) me;
                        switch(eventCol) {
                            case COLUMN_CHANNEL:
                                return (new Integer(event.getChannel()));
                            case COLUMN_NOTEORCC:
                                if (event instanceof NoteEvent) return ((NoteEvent) event).getNoteName(); else if (event instanceof ControllerEvent) return "CC" + ((ControllerEvent) event).getControlNumber(); else if (event instanceof PitchBendEvent) return "PB"; else return null;
                            case COLUMN_VELORVAL:
                                if (event instanceof NoteEvent) return ((NoteEvent) event).getVelocity(); else if (event instanceof ControllerEvent) return ((ControllerEvent) event).getValue(); else if (event instanceof PitchBendEvent) return (((PitchBendEvent) event).getValue() >> 7); else return null;
                            case COLUMN_LEN:
                                if (event instanceof NoteEvent) return ((NoteEvent) event).getDuration() / ticksPerRow;
                            default:
                                return (null);
                        }
                    } else if (me instanceof SysexEvent) {
                        switch(eventCol) {
                            case COLUMN_NOTEORCC:
                                return "SYX";
                            default:
                                return (null);
                        }
                    } else {
                        return null;
                    }
                }
            } else {
                return null;
            }
        }
    }
