    public void sendMessage(Message message, Address[] addresses) throws MessagingException, SendFailedException {
        if (!isConnected()) {
            throw new MessagingException("not connected");
        }
        if (!(message instanceof MimeMessage)) {
            throw new SendFailedException("only MimeMessages are supported");
        }
        MimeMessage mimeMessage = (MimeMessage) message;
        int len = addresses.length;
        List sent = new ArrayList(len);
        List unsent = new ArrayList(len);
        List invalid = new ArrayList(len);
        int deliveryStatus = TransportEvent.MESSAGE_NOT_DELIVERED;
        ParameterList params = null;
        synchronized (connection) {
            try {
                String from0 = getProperty("from");
                InternetAddress from = null;
                if (from0 != null) {
                    InternetAddress[] from1 = InternetAddress.parse(from0);
                    if (from1 != null && from1.length > 0) {
                        from = from1[0];
                    }
                }
                if (from == null) {
                    Address[] from2 = mimeMessage.getFrom();
                    if (from2 != null && from2.length > 0 && from2[0] instanceof InternetAddress) {
                        from = (InternetAddress) from2[0];
                    }
                }
                if (from == null) {
                    from = InternetAddress.getLocalAddress(session);
                }
                String reversePath = from.getAddress();
                String dsnRet = getProperty("dsn.ret");
                if (dsnRet != null && extensions != null && extensions.contains("DSN")) {
                    String FULL = "FULL", HDRS = "HDRS";
                    String value = null;
                    if (FULL.equalsIgnoreCase(dsnRet)) {
                        value = FULL;
                    } else if (HDRS.equalsIgnoreCase(dsnRet)) {
                        value = HDRS;
                    }
                    if (value != null) {
                        if (params == null) params = new ParameterList();
                        params.add(new Parameter("RET", value));
                    }
                }
                String mtrk = getProperty("mtrk");
                if ("true".equals(mtrk) && extensions != null && extensions.contains("MTRK")) {
                    int mtrkTimeout = 0;
                    String mt = getProperty("mtrk.timeout");
                    if (mt != null) mtrkTimeout = Integer.parseInt(mt);
                    try {
                        Random r = new Random();
                        byte[] a = new byte[256];
                        r.nextBytes(a);
                        MessageDigest md = MessageDigest.getInstance("SHA-1");
                        md.update(a);
                        byte[] b = md.digest();
                        byte[] certifier = BASE64.encode(b);
                        if (params == null) params = new ParameterList();
                        String value = new String(certifier, "US-ASCII");
                        if (mtrkTimeout > 0) {
                            value += ":" + mtrkTimeout;
                        }
                        params.add(new Parameter("MTRK", value));
                        String envid = mimeMessage.getMessageID();
                        if (envid != null) {
                            int ai = envid.indexOf('@');
                            if (ai != -1) envid = envid.substring(0, ai);
                        } else {
                            envid = "";
                        }
                        envid += Long.toHexString(System.currentTimeMillis());
                        envid += "@";
                        String lha = InetAddress.getLocalHost().getHostAddress();
                        if (envid.length() + lha.length() > 100) {
                            b = lha.getBytes("UTF-8");
                            md.reset();
                            md.update(b);
                            b = md.digest();
                            b = BASE64.encode(b);
                            lha = new String(b, "US-ASCII");
                        }
                        envid += lha;
                        params.add(new Parameter("ENVID", envid));
                    } catch (NoSuchAlgorithmException e) {
                        MessagingException e2 = new MessagingException(e.getMessage());
                        e2.initCause(e);
                        throw e2;
                    } catch (UnsupportedEncodingException e) {
                        MessagingException e2 = new MessagingException(e.getMessage());
                        e2.initCause(e);
                        throw e2;
                    }
                }
                if (!connection.mailFrom(reversePath, params)) {
                    throw new SendFailedException(connection.getLastResponse());
                }
                params = null;
                String dsnNotify = getProperty("dsn.notify");
                if (dsnNotify != null && extensions != null && extensions.contains("DSN")) {
                    String NEVER = "NEVER", SUCCESS = "SUCCESS";
                    String FAILURE = "FAILURE", DELAY = "DELAY";
                    String value = null;
                    if (NEVER.equalsIgnoreCase(dsnNotify)) {
                        value = NEVER;
                    } else {
                        StringBuffer buf = new StringBuffer();
                        StringTokenizer st = new StringTokenizer(dsnNotify, " ,");
                        while (st.hasMoreTokens()) {
                            String token = st.nextToken();
                            if (SUCCESS.equalsIgnoreCase(token)) {
                                if (buf.length() > 0) {
                                    buf.append(',');
                                }
                                buf.append(SUCCESS);
                            } else if (FAILURE.equalsIgnoreCase(token)) {
                                if (buf.length() > 0) {
                                    buf.append(',');
                                }
                                buf.append(FAILURE);
                            } else if (DELAY.equalsIgnoreCase(token)) {
                                if (buf.length() > 0) {
                                    buf.append(',');
                                }
                                buf.append(DELAY);
                            }
                        }
                        if (buf.length() > 0) {
                            value = buf.toString();
                        }
                    }
                    if (value != null) {
                        params = new ParameterList();
                        params.add(new Parameter("NOTIFY", value));
                    }
                }
                for (int i = 0; i < addresses.length; i++) {
                    Address address = addresses[i];
                    if (address instanceof InternetAddress) {
                        String forwardPath = ((InternetAddress) address).getAddress();
                        if (connection.rcptTo(forwardPath, params)) {
                            sent.add(address);
                        } else {
                            invalid.add(address);
                        }
                    } else {
                        invalid.add(address);
                    }
                }
            } catch (IOException e) {
                try {
                    connection.rset();
                } catch (IOException e2) {
                }
                throw new SendFailedException(e.getMessage());
            }
            if (sent.size() > 0) {
                try {
                    OutputStream dataStream = connection.data();
                    if (dataStream == null) {
                        String msg = connection.getLastResponse();
                        throw new MessagingException(msg);
                    }
                    mimeMessage.writeTo(dataStream);
                    dataStream.flush();
                    if (!connection.finishData()) {
                        unsent.addAll(sent);
                        sent.clear();
                        deliveryStatus = TransportEvent.MESSAGE_NOT_DELIVERED;
                    } else {
                        deliveryStatus = invalid.isEmpty() ? TransportEvent.MESSAGE_DELIVERED : TransportEvent.MESSAGE_PARTIALLY_DELIVERED;
                    }
                } catch (IOException e) {
                    try {
                        if (connection.finishData()) {
                            connection.rset();
                        }
                    } catch (IOException e2) {
                    }
                    throw new SendFailedException(e.getMessage());
                }
            }
        }
        Address[] a_sent = new Address[sent.size()];
        sent.toArray(a_sent);
        Address[] a_unsent = new Address[unsent.size()];
        unsent.toArray(a_unsent);
        Address[] a_invalid = new Address[invalid.size()];
        invalid.toArray(a_invalid);
        notifyTransportListeners(deliveryStatus, a_sent, a_unsent, a_invalid, mimeMessage);
    }
