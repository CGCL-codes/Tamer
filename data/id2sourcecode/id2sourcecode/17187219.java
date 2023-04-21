    private URLConnection getConnection() throws IOException {
        if (connection == null) {
            BrokerPool db = null;
            DBBroker broker = null;
            try {
                db = BrokerPool.getInstance();
                broker = db.get(null);
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
                if (db != null) db.release(broker);
            }
        }
        return connection;
    }
