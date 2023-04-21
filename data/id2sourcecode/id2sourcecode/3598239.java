    protected void handleResponce(Serializable responceObject) {
        try {
            writeResponse(responceObject);
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).logp(Level.WARNING, this.getClass().getName(), "handleResponce", "error, Could not write responce .. writing error", ex);
            try {
                writeResponse(new ServerFataError("Writing responce failed", ex));
            } catch (Exception fatalEx) {
                Logger.getLogger(this.getClass().getName()).logp(Level.SEVERE, this.getClass().getName(), "handleResponce", "Error, Could not write error .. closing socket", fatalEx);
                try {
                    getChannel().socket().close();
                } catch (Exception networkEx) {
                    Logger.getLogger(this.getClass().getName()).logp(Level.SEVERE, this.getClass().getName(), "handleResponce", "Error, Could not close socket .. ", networkEx);
                }
            }
        }
    }
