    public boolean respond(Request request) throws IOException {
        if (!isMine.match(request.url)) {
            return false;
        }
        String current = request.props.getProperty(session);
        if (!force && current != null && !current.equals("")) {
            request.log(Server.LOG_INFORMATIONAL, isMine.prefix(), session + " already exists, skipping");
            return false;
        }
        Props props = new Props(request.headers, request.props);
        props.extra("ipaddress", request.getSocket().getInetAddress().getHostAddress());
        props.extra("url", request.url);
        props.extra("query", request.query);
        props.extra("method", request.method);
        String key = Format.subst(props, extract);
        String id;
        value = Format.subst(request.props, value, true);
        if (invert) {
            if (regexp.match(key) == null) {
                id = value;
            } else {
                id = null;
            }
        } else {
            id = regexp.sub(key, value);
        }
        if (id == null) {
            request.log(Server.LOG_DIAGNOSTIC, isMine.prefix(), "(" + key + ") doesn't match re, not set");
            return false;
        }
        if (digest != null) {
            digest.reset();
            id = Base64.encode(digest.digest(id.getBytes()));
        }
        request.props.put(session, id);
        request.log(Server.LOG_DIAGNOSTIC, isMine.prefix(), "Using (" + id + ") as session id");
        return false;
    }
