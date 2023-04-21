    private static final void writeMessage(Thread th, long logTime, Class<?> logClass, LogLevelWrapper l, Object ctx, String msg, Throwable t) {
        final PrintStream out = getPrintStream(l);
        final LogMsgComponentFormatter<?>[] fmts = getFormatters();
        if ((out != null) && (fmts != null) && (fmts.length > 0)) {
            int numWritten = 0;
            for (final LogMsgComponentFormatter<?> f : fmts) {
                if (null == f) continue;
                final String v = f.format(th, logTime, logClass, l, ctx, msg, t);
                out.append(String.valueOf(v));
                numWritten++;
            }
            if (numWritten > 0) out.println();
        }
    }
