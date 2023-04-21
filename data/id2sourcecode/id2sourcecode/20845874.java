    public static void transferBody(final BytesMessage msg, final OutputStream out) throws JMSException, IOException {
        final long len = msg.getBodyLength();
        if (len > 0) {
            final byte[] buf = new byte[(int) Math.min(8192, len)];
            for (int step; (step = msg.readBytes(buf)) > 0; ) out.write(buf, 0, step);
        }
    }
