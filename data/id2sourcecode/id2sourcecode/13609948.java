    public <T> T resolve(Class<T> type, URI uri) {
        if (inProgress) return null;
        inProgress = true;
        try {
            if (uri == null) {
                throw new RuntimeException("No uri specified in a reference.");
            }
            URL url = uri.toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() == 200) {
                JAXBContext context = JAXBContext.newInstance(type);
                return (T) context.createUnmarshaller().unmarshal(conn.getInputStream());
            } else {
                throw new WebApplicationException(new Throwable("Resource for " + uri + " does not exist."), 404);
            }
        } catch (WebApplicationException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new WebApplicationException(ex);
        } finally {
            removeInstance();
        }
    }
