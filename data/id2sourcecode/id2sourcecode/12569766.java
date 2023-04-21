    public void receiveRPY(Message message) {
        log.debug("OTP Authenticator.receiveRPY");
        Blob blob = null;
        boolean sendAbort = true;
        try {
            if ((state != STATE_STARTED) && (state != STATE_CHALLENGE) && (state != STATE_COMPLETE)) {
                sendAbort = true;
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
                log.debug("OTPAuthenticator receiveRPY got an RPY=>" + blob.getData());
                sendAbort = false;
                abort(ERR_PEER_ABORTED + blob.getData());
            }
            if (state == STATE_STARTED) {
                receiveChallenge(blob);
                return;
            } else if (blob.getStatus() != Blob.ABORT) {
                profile.finishInitiatorAuthentication(new SessionCredential(credential), channel.getSession());
                synchronized (this) {
                    this.notify();
                }
                return;
            } else {
                abort(ERR_UNKNOWN_COMMAND);
                return;
            }
        } catch (Exception x) {
            log.error(x);
            synchronized (this) {
                this.notify();
            }
            if (sendAbort) {
                try {
                    Blob a = new Blob(Blob.STATUS_ABORT, x.getMessage());
                    channel.sendMSG(new StringOutputDataStream(a.toString()), this);
                } catch (BEEPException y) {
                    message.getChannel().getSession().terminate(y.getMessage());
                }
            }
        }
    }
