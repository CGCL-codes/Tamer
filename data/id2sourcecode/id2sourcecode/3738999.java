    public static Object jsFunction_send(Context cx, Scriptable thisObj, Object[] args, Function fun) throws JavaScriptException {
        email _email = this_email(thisObj);
        byte[] rewritten_html = null;
        if (null != _email.body_pg) {
            file pagef = _email.body_pg;
            if (pagef.canRead()) {
                String type = mimes.instance.getTypeFile(pagef.path());
                if (null != type && type.startsWith("text/html")) {
                    _email.mimetype = type;
                    OutputStream out = _email.body_bbo();
                    try {
                        request req = null;
                        boolean urlrewrite = true;
                        if (null != args && 0 < args.length) {
                            Object arg = args[0];
                            if (arg instanceof request) req = (request) arg; else throw new JavaScriptException("Sending HTML email requires a `req' request object argument.");
                            if (1 < args.length) {
                                arg = args[1];
                                if (arg instanceof Boolean) urlrewrite = ((Boolean) arg).booleanValue(); else throw new JavaScriptException("Second argument to `email.send(req,rewrite)' not recognized, must be boolean.");
                            }
                        } else throw new JavaScriptException("Sending HTML email requires a `req' request object argument.");
                        if (page.PC_page(req, pagef)) {
                            page.PC_send(cx, req, pagef, out);
                        } else {
                            InputStream in = pagef.getInputStream();
                            try {
                                long flen = pagef.length();
                                int read, buflen;
                                if (flen > 102400) buflen = 4096; else if (flen < 8192) buflen = 512; else buflen = 1024;
                                byte[] buf = new byte[buflen];
                                while (0 < (read = in.read(buf, 0, buflen))) out.write(buf, 0, read);
                            } finally {
                                in.close();
                            }
                        }
                        if (urlrewrite) {
                            rewritten_html = _email.body_buf.toByteArray();
                            if (null != rewritten_html) {
                                String sessid = req.getRequestSessionID();
                                if (null != sessid) rewritten_html = request.HtmlUrlRewrite(rewritten_html, req.srv.jsGet_defaultUrl(), req.urlTermSessid()); else throw new JavaScriptException("BBBUGGGG! Email page resource `" + pagef + "' error; no session identifier in request.");
                            } else throw new JavaScriptException("BBBUGGGG! Email page resource `" + pagef + "' error; no content in body?");
                        }
                    } catch (IOException iox) {
                        throw new JavaScriptException("Email page resource `" + pagef + "' error (" + iox + ").");
                    }
                } else {
                    if (null == type) throw new JavaScriptException("Email page resource `" + pagef + "' content type not found."); else throw new JavaScriptException("Email page resource `" + pagef + "' content type `" + type + "' not supported (not HTML).");
                }
            } else throw new JavaScriptException("Email page resource `" + pagef + "' not found.");
        }
        if (null == _email.mailserver) {
            InetAddress host = aprops.ServerMail();
            if (null != host) _email.mailserver = host.getHostName(); else throw new JavaScriptException("Sending an email requires a `mailserver'.");
        }
        if (null == _email.from || 0 > _email.from.indexOf("@")) throw new JavaScriptException("Sending an email requires a complete `from' address."); else if (null == _email.to) throw new JavaScriptException("Sending an email requires one or more addressees in the `to' field."); else {
            String[] to_list = address(_email.to);
            String[] from_list = address(_email.from);
            socket sock = new socket();
            if (_email.debug) sock.debug = true;
            sock.hostname = _email.mailserver;
            sock.portnum = 25;
            String smtp_req, smtp_rep, s;
            int cc, len;
            Object[] args0 = new Object[0];
            Object[] args1 = new Object[1];
            try {
                smtp_rep = (String) sock._readLine(cx, args0, null);
            } catch (JavaScriptException iox) {
                throw new JavaScriptException("Mailhost (" + _email.mailserver + ") is not an SMTP server.");
            }
            if ('2' != smtp_rep.charAt(0)) {
                args1[0] = "QUIT";
                try {
                    sock._println(cx, args1, null);
                } catch (Throwable t) {
                }
                sock.jsFunction_close(cx, sock, args0, null);
                throw new JavaScriptException("SMTP HELO? " + smtp_rep);
            }
            if (null == _email.maildomain) {
                _email.maildomain = aprops.instance.getProperty("mail-domain");
                if (null == _email.maildomain) {
                    _email.maildomain = aprops.LocalHostNameDomain();
                }
            }
            if (null != _email.maildomain) {
                smtp_req = chbuf.cat("HELO ", _email.maildomain);
                args1[0] = smtp_req;
                sock._println(cx, args1, null);
                smtp_rep = (String) sock._readLine(cx, args0, null);
                if ('2' != smtp_rep.charAt(0)) {
                    args1[0] = "QUIT";
                    try {
                        sock._println(cx, args1, null);
                    } catch (Throwable t) {
                    }
                    sock.jsFunction_close(cx, sock, args0, null);
                    throw new JavaScriptException(smtp_req + ", SMTP response " + smtp_rep);
                }
            }
            String tmp;
            tmp = from_list[0];
            if (-1 < tmp.indexOf('<')) smtp_req = chbuf.cat("MAIL FROM: ", tmp); else smtp_req = chbuf.cat("MAIL FROM: <", tmp, ">");
            args1[0] = smtp_req;
            sock._println(cx, args1, null);
            smtp_rep = (String) sock._readLine(cx, args0, null);
            if ('2' != smtp_rep.charAt(0)) {
                args1[0] = "QUIT";
                try {
                    sock._println(cx, args1, null);
                } catch (Throwable t) {
                }
                sock.jsFunction_close(cx, sock, args0, null);
                throw new JavaScriptException(smtp_req + ", SMTP response " + smtp_rep);
            }
            if (null != to_list) {
                len = to_list.length;
                for (cc = 0; cc < len; cc++) {
                    tmp = to_list[cc];
                    if (-1 < tmp.indexOf('<')) smtp_req = chbuf.cat("RCPT TO: ", tmp); else smtp_req = chbuf.cat("RCPT TO: <", tmp, ">");
                    args1[0] = smtp_req;
                    sock._println(cx, args1, null);
                    smtp_rep = (String) sock._readLine(cx, args0, null);
                    if ('2' != smtp_rep.charAt(0)) {
                        args1[0] = "QUIT";
                        try {
                            sock._println(cx, args1, null);
                        } catch (Throwable t) {
                        }
                        sock.jsFunction_close(cx, sock, args0, null);
                        throw new JavaScriptException(smtp_req + ", SMTP response " + smtp_rep);
                    }
                }
            } else {
                args1[0] = "QUIT";
                try {
                    sock._println(cx, args1, null);
                } catch (Throwable t) {
                }
                sock.jsFunction_close(cx, sock, args0, null);
                throw new JavaScriptException("Missing Mail To.");
            }
            args1[0] = "DATA";
            sock._println(cx, args1, null);
            smtp_rep = (String) sock._readLine(cx, args0, null);
            if ('2' != smtp_rep.charAt(0) && '3' != smtp_rep.charAt(0)) {
                args1[0] = "QUIT";
                try {
                    sock._println(cx, args1, null);
                } catch (Throwable t) {
                }
                sock.jsFunction_close(cx, sock, args0, null);
                throw new JavaScriptException("DATA Start, SMTP response " + smtp_rep);
            }
            args1[0] = chbuf.cat("From: ", _email.from);
            sock._println(cx, args1, null);
            if (null != _email.replyto) {
                args1[0] = chbuf.cat("Reply-To: ", _email.replyto);
                sock._println(cx, args1, null);
            }
            args1[0] = "To: " + _email.to;
            sock._println(cx, args1, null);
            if (null != _email.subject) {
                args1[0] = chbuf.cat("Subject: ", _email.subject);
                sock._println(cx, args1, null);
            }
            args1[0] = Mailer;
            sock._println(cx, args1, null);
            if (_email.mimetype.equals("text/html")) {
                args1[0] = "Content-Type: text/html ;charset=us-ascii";
                sock._println(cx, args1, null);
                args1[0] = "Content-Transfer-Encoding: 7bit";
                sock._println(cx, args1, null);
            } else {
                args1[0] = chbuf.cat("Content-Type: ", _email.mimetype);
                sock._println(cx, args1, null);
            }
            args1[0] = "MIME-Version: 1.0";
            sock._println(cx, args1, null);
            args1[0] = chbuf.cat("Date: ", new java.util.Date().toGMTString());
            sock._println(cx, args1, null);
            if (!_email.bodyHeaders) {
                sock._println(cx, args0, null);
            }
            if (null != rewritten_html) args1[0] = rewritten_html; else args1[0] = _email.body_buf.toByteArray();
            Object contentlength = sock._println(cx, args1, null);
            sock._println(cx, args0, null);
            args1[0] = ".";
            sock._println(cx, args1, null);
            smtp_rep = (String) sock._readLine(cx, args0, null);
            if (smtp_rep.startsWith("250")) {
                String[] endline = linebuf.toStringArray(smtp_rep, " ");
                if (null != endline) {
                    if (4 < endline.length) _email.ticket = endline[2]; else if (2 < endline.length) _email.ticket = endline[1];
                }
                args1[0] = "QUIT";
                sock._println(cx, args1, null);
                smtp_rep = (String) sock._readLine(cx, args0, null);
                sock.jsFunction_close(cx, sock, args0, null);
                if (null != _email.ticket) return _email.ticket; else return null;
            } else {
                args1[0] = "QUIT";
                sock._println(cx, args1, null);
                sock.jsFunction_close(cx, sock, args0, null);
                throw new JavaScriptException("DATA Terminal, SMTP response " + smtp_rep);
            }
        }
    }
