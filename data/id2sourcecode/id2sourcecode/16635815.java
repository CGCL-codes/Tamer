        @Override
        public Object getColumnValue(EpgEvent evt, int idx) {
            Object rv = null;
            if (evt != null) {
                switch(idx) {
                    case 0:
                        rv = evt.getBegin();
                        break;
                    case 1:
                        rv = evt.getDuration();
                        break;
                    case 2:
                        rv = evt.getTitle();
                        break;
                    case 3:
                        rv = evt.getSubTitle();
                        break;
                    case 4:
                        rv = evt.getChannel().getName();
                        break;
                }
            }
            return rv;
        }
