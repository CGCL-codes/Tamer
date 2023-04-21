    public FIXMessage read(final long timeout) {
        try {
            FIXMessage rval = messages.poll(100, TimeUnit.MILLISECONDS);
            while (rval == null && istream.getChannel().isOpen()) {
                rval = messages.poll(100, TimeUnit.MILLISECONDS);
            }
            return rval;
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return null;
        }
    }
