    private int indexOf(ForwardingChannel channel) {
        synchronized (active) {
            int j = active.size();
            try {
                for (int i = 0; i < j; i++) {
                    ActiveChannelWrapper a = (ActiveChannelWrapper) active.elementAt(i);
                    if (a.getChannel() == channel) {
                        return i;
                    }
                }
                return -1;
            } catch (ArrayIndexOutOfBoundsException ex) {
                return -1;
            }
        }
    }
