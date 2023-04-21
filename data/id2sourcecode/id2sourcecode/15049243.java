    public InputStream getDocumentByID(String id) {
        Reference ref = new Reference(store, id, null);
        Predicate predicate = new Predicate(new Reference[] { ref }, store, null);
        try {
            Content[] result = contentService.read(predicate, Constants.PROP_CONTENT);
            Content content = result[0];
            String target = content.getUrl() + "?ticket=" + ticket;
            URL url = new URL(target);
            log.debug("Retrieving:" + url.getPath());
            return url.openConnection().getInputStream();
        } catch (Exception e) {
            log.error("Error reading content", e);
            if (recoverSessionIfNecessary()) {
                return getDocumentByID(id);
            }
        }
        return null;
    }
