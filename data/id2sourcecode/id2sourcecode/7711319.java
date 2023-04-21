    public void receiveRPY(Message message) {
        log.debug("Anonymous Authenticator.receiveRPY");
        Blob blob = null;
        boolean sendAbort = true;
        try {
            if (state != STATE_ID) {
                abort(ERR_ANON_STATE);
            }
            try {
                InputStream is = message.getDataStream().getInputStream();
                int limit = is.available();
                byte buff[] = new byte[limit];
                is.read(buff);
                blob = new Blob(new String(buff));
            } catch (IOException x) {
                abort(x.getMessage());
            }
            String status = blob.getStatus();
            if ((status != null) && status.equals(SASLProfile.SASL_STATUS_ABORT)) {
                log.debug("Anonymous Authenticator receiveRPY=>" + blob.getData());
                sendAbort = false;
                abort(ERR_PEER_ABORTED);
            }
            if (!status.equals(Blob.ABORT)) {
                profile.finishInitiatorAuthentication(new SessionCredential(credential), channel.getSession());
                synchronized (this) {
                    this.notify();
                }
                return;
            } else {
                abort(blob.getData());
            }
        } catch (SASLException x) {
            log.error(x);
            synchronized (this) {
                this.notify();
            }
            try {
                if (sendAbort) {
                    Blob reply = new Blob(Blob.STATUS_ABORT, x.getMessage());
                    channel.sendMSG(new StringOutputDataStream(blob.toString()), this);
                }
            } catch (Exception q) {
                message.getChannel().getSession().terminate(q.getMessage());
            }
        }
    }
