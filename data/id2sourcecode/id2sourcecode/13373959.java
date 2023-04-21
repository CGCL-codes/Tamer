    private URLConnection getConnection() throws IOException {
        if (connection == null) {
            BrokerPool database = null;
            DBBroker broker = null;
            try {
                database = BrokerPool.getInstance();
                broker = database.get(null);
                Subject subject = broker.getSubject();
                URL url = new URL("xmldb:exist://jsessionid:" + subject.getSessionId() + "@" + uri.toString());
                connection = url.openConnection();
            } catch (IllegalArgumentException e) {
                throw new IOException(e);
            } catch (MalformedURLException e) {
                throw new IOException(e);
            } catch (EXistException e) {
                throw new IOException(e);
            } finally {
                if (database != null) database.release(broker);
            }
        }
        return connection;
    }
