    public boolean isUserOnLine(String username) throws AxisFault {
        if (_log.isDebugEnabled()) {
            _log.debug("isUserOnLine(): entered. username = " + username);
        }
        boolean isOnline = false;
        BufferedReader reader = null;
        long startTime = 0;
        try {
            if (_log.isDebugEnabled()) {
                startTime = System.currentTimeMillis();
            }
            URL url = new URL("http://mail.opi.yahoo.com/online?u=" + username + "&m=t&t=0");
            URLConnection connection = url.openConnection();
            connection.connect();
            reader = getReader(connection);
            String result = reader.readLine();
            if (_log.isDebugEnabled()) {
                long elapsedTime = System.currentTimeMillis() - startTime;
                _log.debug("isUserOnLine(): time elapsed = " + elapsedTime);
            }
            if ((result != null) && (result.indexOf("NOT ONLINE") < 0)) {
                isOnline = true;
            }
        } catch (MalformedURLException e) {
            _log.error("isUserOnLine(): caught MalformedURLException: " + e.getMessage(), e);
            throw new AxisFault("Service Exception, please try again");
        } catch (IOException e) {
            _log.error("isUserOnLine(): caught IOException: " + e.getMessage(), e);
            throw new AxisFault("Service Exception, please try again");
        } finally {
            close(reader);
            if (_log.isDebugEnabled()) {
                _log.debug("isUserOnLine(): exiting with return value of " + isOnline + " for user " + username);
            }
        }
        return isOnline;
    }
