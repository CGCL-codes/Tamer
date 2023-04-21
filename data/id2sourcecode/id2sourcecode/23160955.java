        @Override
        public Object getColumnValue(Timer tm, int column) {
            Object rv = null;
            switch(column) {
                case 0:
                    rv = tm.getSequence();
                    break;
                case 1:
                    rv = tm.isActive();
                    break;
                case 2:
                    rv = tm.isVps();
                    break;
                case 3:
                    if (tm.getEvent() != null && tm.getEvent().getChannel() != null) rv = tm.getEvent().getChannel().getName(); else rv = tm.getChannelId();
                    break;
                case 4:
                    rv = tm.getBegin();
                    if (rv == null && tm.getEvent() != null) rv = tm.getEvent().getBegin();
                    break;
                case 5:
                    rv = tm.getEnd();
                    break;
                case 6:
                    if (tm.getEvent() != null && tm.getEvent().getTitle() != null) {
                        StringBuilder sb = new StringBuilder(tm.getEvent().getTitle());
                        if (tm.getEvent().getSubTitle() != null) {
                            sb.append(" - ");
                            sb.append(tm.getEvent().getSubTitle());
                        }
                        rv = sb.toString();
                    }
                    break;
                case 7:
                    rv = tm.getPriority();
                    break;
                case 8:
                    rv = tm.getLifeTime();
                    break;
            }
            return rv;
        }
