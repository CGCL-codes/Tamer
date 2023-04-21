    private ActiveChannelWrapper findWrapper(ForwardingChannel channel) {
        synchronized (active) {
            int j = active.size();
            for (int i = 0; i < j; i++) {
                try {
                    ActiveChannelWrapper a = (ActiveChannelWrapper) active.elementAt(i);
                    if (a.getChannel() == channel) {
                        return a;
                    }
                } catch (ArrayIndexOutOfBoundsException ex) {
                    return null;
                }
            }
        }
        return null;
    }
