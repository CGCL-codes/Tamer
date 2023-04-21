    public Channel getChannel(Long id, IChannelDAO dao) {
        Channel result = null;
        try {
            result = dao.getById(id);
        } catch (PersistenceException e) {
            fail(e.getMessage());
        }
        return result;
    }
