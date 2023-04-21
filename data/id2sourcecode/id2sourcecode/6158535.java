        public int do_challenge(int auth, Command c) throws IOException {
            switch(auth) {
                case AUTH_SHA:
                    String[] reply = stupidlyDecode(c.getData()).split(" ");
                    if (Debug.debug) Debug.print(Debug.VERBOSE, Arrays.toString(reply));
                    if (3 != reply.length) {
                        if (Debug.debug) Debug.print(Debug.DEBUG, "Reply is not length 3");
                        return ERROR;
                    }
                    String context = reply[0];
                    String ID = reply[1];
                    String serverchallenge = reply[2];
                    MessageDigest md = null;
                    try {
                        md = MessageDigest.getInstance("SHA");
                    } catch (NoSuchAlgorithmException NSAe) {
                        if (Debug.debug && AbstractConnection.EXCEPTION_DEBUG) Debug.print(Debug.ERR, NSAe);
                        return ERROR;
                    }
                    byte[] buf = new byte[8];
                    Message.marshallintBig(System.currentTimeMillis(), buf, 0, 8);
                    String clientchallenge = stupidlyEncode(md.digest(buf));
                    md.reset();
                    long start = System.currentTimeMillis();
                    String cookie = null;
                    while (null == cookie && (System.currentTimeMillis() - start) < LOCK_TIMEOUT) cookie = findCookie(context, ID);
                    if (null == cookie) {
                        if (Debug.debug) Debug.print(Debug.DEBUG, "Did not find a cookie in context " + context + " with ID " + ID);
                        return ERROR;
                    }
                    String response = serverchallenge + ":" + clientchallenge + ":" + cookie;
                    buf = md.digest(response.getBytes());
                    if (Debug.debug) Debug.print(Debug.VERBOSE, "Response: " + response + " hash: " + Hexdump.format(buf));
                    response = stupidlyEncode(buf);
                    c.setResponse(stupidlyEncode(clientchallenge + " " + response));
                    return OK;
                default:
                    if (Debug.debug) Debug.print(Debug.DEBUG, "Not DBUS_COOKIE_SHA1 authtype.");
                    return ERROR;
            }
        }
