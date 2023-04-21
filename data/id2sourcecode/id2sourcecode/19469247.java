    public Object getValueAt(int r, int c) {
        synchronized (active) {
            ActiveChannelWrapper w = null;
            try {
                w = (ActiveChannelWrapper) active.elementAt(r);
            } catch (ArrayIndexOutOfBoundsException ex) {
            }
            switch(c) {
                case 0:
                    if (w != null) {
                        return new Boolean(w.isSending());
                    } else {
                        return new Boolean(false);
                    }
                case 1:
                    if (w != null) {
                        return new Boolean(w.isReceiving());
                    } else {
                        return new Boolean(false);
                    }
                case 2:
                    if (w != null) {
                        return new String(w.getChannel().getName());
                    } else {
                        return new Long(-1);
                    }
                default:
                    if (w != null) {
                        StringBuffer sb = new StringBuffer(w.getChannel().getOriginatingHost());
                        sb.append(":");
                        sb.append(w.getChannel().getOriginatingPort());
                        return sb.toString();
                    } else {
                        return "Removed";
                    }
            }
        }
    }
