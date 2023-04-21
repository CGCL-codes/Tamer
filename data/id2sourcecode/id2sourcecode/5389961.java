    protected HttpURLConnection getConnection() throws DereferencingException {
        try {
            return (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            String errmsg = "I/O error (type: " + e.getClass().getName() + ") caught while creating a connection for URI <" + url.toString() + "> (ID: " + uriID + "): " + e.getMessage();
            log.error(errmsg);
            throw new DereferencingException(errmsg, e);
        } catch (Exception e) {
            String errmsg = "Unexpected exception (type: " + e.getClass().getName() + ", first stack trace element: " + e.getStackTrace()[0].toString() + ") caught while creating a connection for URI <" + url.toString() + "> (ID: " + uriID + "): " + e.getMessage();
            log.error(errmsg);
            throw new DereferencingException(errmsg, e);
        }
    }
