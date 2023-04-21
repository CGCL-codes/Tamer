    public boolean reset() {
        if (logger.isDebugEnabled()) {
            logger.debug("DB contains");
            logger.debug("AudioItems:{}", getNumberOfAudioItems());
            logger.debug("Albums:{}", getNumberOfAlbums());
            logger.debug("Artists:{}", getNumberOfArtists());
        }
        clearCollections();
        AudioItemDAO dao = factory.getAudioItemDAO();
        Transaction t = dao.beginTransaction();
        try {
            logger.debug("Reseting db");
            dao.getSession().createQuery("Delete from " + HibernateAudioPlaylist.class.getName()).executeUpdate();
            dao.getSession().createQuery("Delete from " + HibernateAudioItem.class.getName()).executeUpdate();
            dao.getSession().createQuery("Delete from " + HibernateAlbum.class.getName()).executeUpdate();
            dao.getSession().createQuery("Delete from " + HibernateArtist.class.getName()).executeUpdate();
            dao.getSession().createQuery("Delete from " + HibernateGenre.class.getName()).executeUpdate();
            dao.getSession().createQuery("Delete from " + HibernateTVSeason.class.getName()).executeUpdate();
            dao.getSession().createQuery("Delete from " + HibernateTVShow.class.getName()).executeUpdate();
            dao.getSession().createQuery("Delete from " + HibernateTVEpisodeItem.class.getName()).executeUpdate();
            dao.getSession().createQuery("Delete from " + HibernateImageItem.class.getName()).executeUpdate();
            dao.getSession().createQuery("Delete from " + HibernateVideoItem.class.getName()).executeUpdate();
            dao.commitTransaction();
            logger.debug("Reset complete");
        } catch (Exception e) {
            t.rollback();
            logger.error("Error reseting db", e);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("DB now contains");
            logger.debug("AudioItems:{}", getNumberOfAudioItems());
            logger.debug("Albums:{}", getNumberOfAlbums());
            logger.debug("Artists:{}", getNumberOfArtists());
        }
        return false;
    }
