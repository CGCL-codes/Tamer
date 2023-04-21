    @Override
    public String log(final LogLevelWrapper l, final String msg, final Throwable t) {
        if (isEnabledFor(l)) writeMessage(Thread.currentThread(), System.currentTimeMillis(), getLoggingClass(), l, getThreadContext(), msg, t);
        return msg;
    }
