    public DereferencingResult executeTask(DereferencingTask task) {
        DereferencingResult result = null;
        this.tempNgs = ngsFactory.create();
        try {
            url = new URL(task.getURI());
        } catch (MalformedURLException ex) {
            return createErrorResult(task, DereferencingResult.STATUS_MALFORMED_URL, ex, null);
        }
        try {
            URLConnection con = url.openConnection();
            con.setConnectTimeout(connectTimeout);
            con.setReadTimeout(readTimeout);
            if (task.conditional) {
                con.setIfModifiedSince(task.ifModifiedSince);
            }
            connection = (HttpURLConnection) con;
        } catch (IOException e) {
            log.debug("Creating a connection to <" + url.toString() + "> caused a " + e.getClass().getName() + ": " + e.getMessage(), e);
            return createErrorResult(task, DereferencingResult.STATUS_UNABLE_TO_CONNECT, e, null);
        }
        connection.setInstanceFollowRedirects(false);
        connection.addRequestProperty("Accept", "application/rdf+xml;q=1," + "text/xml;q=0.6,text/rdf+n3;q=0.9," + "application/octet-stream;q=0.5," + "application/xml q=0.5,application/rss+xml;q=0.5," + "text/plain; q=0.5,application/x-turtle;q=0.5," + "application/x-trig;q=0.5," + "application/xhtml+xml;q=0.5, " + "text/html;q=0.5");
        try {
            connection.connect();
        } catch (SocketTimeoutException e) {
            log.debug("Connecting to <" + url.toString() + "> caused a " + e.getClass().getName() + ": " + e.getMessage());
            connection.disconnect();
            connection = null;
            return createErrorResult(task, DereferencingResult.STATUS_TIMEOUT, e, null);
        } catch (IOException e) {
            log.debug("Connecting to <" + url.toString() + "> caused a " + e.getClass().getName() + ": " + e.getMessage(), e);
            connection.disconnect();
            connection = null;
            return createErrorResult(task, DereferencingResult.STATUS_UNABLE_TO_CONNECT, e, null);
        } catch (RuntimeException e) {
            log.debug("Connecting to <" + url.toString() + "> caused a " + e.getClass().getName() + ": " + e.getMessage());
            connection.disconnect();
            connection = null;
            return createErrorResult(task, DereferencingResult.STATUS_UNABLE_TO_CONNECT, e, null);
        }
        try {
            this.log.debug(this.connection.getResponseCode() + " " + this.url + " (" + this.connection.getContentType() + ")");
            if ((this.connection.getResponseCode() == 301) || (this.connection.getResponseCode() == 302) || (this.connection.getResponseCode() == 303)) {
                String redirectURI = this.connection.getHeaderField("Location");
                DereferencingResult r = new DereferencingResult(task, DereferencingResult.STATUS_REDIRECTED, redirectURI, connection.getHeaderFields());
                connection.disconnect();
                connection = null;
                return r;
            }
            if (this.connection.getResponseCode() == 304) {
                DereferencingResult r = new DereferencingResult(task, DereferencingResult.STATUS_UNMODIFIED, null, null, connection.getHeaderFields());
                connection.disconnect();
                connection = null;
                return r;
            }
            if (this.connection.getResponseCode() != 200) {
                DereferencingResult r = createErrorResult(task, DereferencingResult.STATUS_UNABLE_TO_CONNECT, new Exception("Unexpected response code (" + connection.getResponseCode() + ")"), connection.getHeaderFields());
                connection.disconnect();
                connection = null;
                return r;
            }
            if (connection.getContentType() == null) {
                DereferencingResult r = createErrorResult(task, DereferencingResult.STATUS_UNABLE_TO_CONNECT, new Exception("Unknown content type"), connection.getHeaderFields());
                connection.disconnect();
                connection = null;
                return r;
            }
            String lang = setLang();
            try {
                result = this.parseRdf(task, lang);
            } catch (Exception ex) {
                this.log.debug(ex.getMessage());
                DereferencingResult r = createErrorResult(task, DereferencingResult.STATUS_PARSING_FAILED, ex, connection.getHeaderFields());
                connection.disconnect();
                connection = null;
                return r;
            }
        } catch (SocketTimeoutException e) {
            log.debug("Accessing the connection to <" + url.toString() + "> caused a " + e.getClass().getName() + ": " + e.getMessage());
            DereferencingResult r = createErrorResult(task, DereferencingResult.STATUS_TIMEOUT, e, null);
            connection.disconnect();
            connection = null;
            return r;
        } catch (IOException e) {
            log.debug("Accessing the connection to <" + url.toString() + "> caused a " + e.getClass().getName() + ": " + e.getMessage(), e);
            DereferencingResult r = createErrorResult(task, DereferencingResult.STATUS_UNABLE_TO_CONNECT, e, null);
            connection.disconnect();
            connection = null;
            return r;
        }
        connection.disconnect();
        connection = null;
        return result;
    }
