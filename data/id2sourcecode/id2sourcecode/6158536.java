        public int do_response(int auth, String Uid, String kernelUid, Command c) {
            MessageDigest md = null;
            try {
                md = MessageDigest.getInstance("SHA");
            } catch (NoSuchAlgorithmException NSAe) {
                if (Debug.debug && AbstractConnection.EXCEPTION_DEBUG) Debug.print(Debug.ERR, NSAe);
                return ERROR;
            }
            switch(auth) {
                case AUTH_NONE:
                    switch(c.getMechs()) {
                        case AUTH_ANON:
                            return OK;
                        case AUTH_EXTERNAL:
                            if (0 == col.compare(Uid, c.getData()) && (null == kernelUid || 0 == col.compare(Uid, kernelUid))) return OK; else return ERROR;
                        case AUTH_SHA:
                            String context = COOKIE_CONTEXT;
                            long id = System.currentTimeMillis();
                            byte[] buf = new byte[8];
                            Message.marshallintBig(id, buf, 0, 8);
                            challenge = stupidlyEncode(md.digest(buf));
                            Random r = new Random();
                            r.nextBytes(buf);
                            cookie = stupidlyEncode(md.digest(buf));
                            try {
                                addCookie(context, "" + id, id / 1000, cookie);
                            } catch (IOException IOe) {
                                if (Debug.debug && AbstractConnection.EXCEPTION_DEBUG) Debug.print(Debug.ERR, IOe);
                            }
                            if (Debug.debug) Debug.print(Debug.DEBUG, "Sending challenge: " + context + ' ' + id + ' ' + challenge);
                            c.setResponse(stupidlyEncode(context + ' ' + id + ' ' + challenge));
                            return CONTINUE;
                        default:
                            return ERROR;
                    }
                case AUTH_SHA:
                    String[] response = stupidlyDecode(c.getData()).split(" ");
                    if (response.length < 2) return ERROR;
                    String cchal = response[0];
                    String hash = response[1];
                    String prehash = challenge + ":" + cchal + ":" + cookie;
                    byte[] buf = md.digest(prehash.getBytes());
                    String posthash = stupidlyEncode(buf);
                    if (Debug.debug) Debug.print(Debug.DEBUG, "Authenticating Hash; data=" + prehash + " remote hash=" + hash + " local hash=" + posthash);
                    if (0 == col.compare(posthash, hash)) return OK; else return ERROR;
                default:
                    return ERROR;
            }
        }
