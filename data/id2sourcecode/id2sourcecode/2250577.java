    protected Session acceptNewSession() {
        TelnetSession newSession = null;
        try {
            Socket sock = null;
            try {
                sock = socket.accept();
                logger.debug("accepted new telnet connection: " + sock.getInetAddress().toString());
                last_wait = 100;
                sock.setSoTimeout(timeout);
            } catch (BindException be) {
                throw be;
            } catch (IOException ioe) {
                try {
                    Thread.sleep(last_wait);
                } catch (InterruptedException ie) {
                }
                last_wait = last_wait << 1;
                if (last_wait > MAXIMUM_WAIT) {
                    last_wait = MAXIMUM_WAIT;
                }
                return null;
            } catch (OutOfMemoryError oome) {
                System.gc();
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException ioe) {
                }
                return null;
            }
            if (shutdown) return null;
            SystemPrintStream sout = new SystemPrintStream(sock.getOutputStream());
            TelnetInputStream sin = new TelnetInputStream(sock.getInputStream(), sout);
            logger.debug("creating new telnet session");
            newSession = new TelnetSession(sin, sout, sout, sock, this);
            logger.debug("new telnet session created");
            sin.setSession(newSession);
            try {
                logger.debug("starting new telnet session");
                newSession.start();
            } catch (Throwable t) {
                sout.write("Thread limit reached.  Connection Terminated.".getBytes());
                sock.close();
                newSession = null;
            }
        } catch (IOException ioe) {
            logger.error("Telnet error: " + ioe.getLocalizedMessage(), ioe);
            try {
                if (!shutdown) shutDown();
            } catch (Throwable t) {
                shutdown = true;
            }
        }
        return newSession;
    }
