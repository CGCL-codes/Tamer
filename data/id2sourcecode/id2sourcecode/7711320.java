    public void receiveERR(Message message) {
        log.debug("Anonymous Authenticator.receiveERR");
        try {
            InputStream is = message.getDataStream().getInputStream();
            int limit = is.available();
            byte buff[] = new byte[limit];
            is.read(buff);
            if (log.isDebugEnabled()) {
                log.debug("SASL-Anonymous Authentication ERR received=>\n" + new String(buff));
            }
            abortNoThrow(new String(buff));
            synchronized (this) {
                this.notify();
            }
        } catch (Exception x) {
            message.getChannel().getSession().terminate(x.getMessage());
        }
    }
