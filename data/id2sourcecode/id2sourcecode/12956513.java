    public static void updateViaXml(final ResultProgressHandle progress, final GameTitle game) throws Exception {
        if (!(game instanceof WtGameTitle)) throw new IllegalArgumentException("game must be a WtGameTitle");
        Session session = null;
        Transaction tx = null;
        try {
            session = openSession();
            tx = session.beginTransaction();
            if (null != progress) progress.setBusy("Downloading song data for " + game);
            WtSongDataFetcher fetcher = new WtSongDataFetcher();
            fetcher.fetch((WtGameTitle) game);
            LOG.finer("xml updated at " + fetcher.updated);
            Map<WtGame, Tiers> tiers = fetcher.tierMap;
            LOG.finer("xml had " + tiers.size() + " tiers for " + game);
            if (null != progress) progress.setBusy("Updating tiers");
            for (WtGame key : tiers.keySet()) {
                Tiers.setTiers(key, tiers.get(key));
                key.mapTiers(tiers.get(key));
            }
            Tiers.write();
            List<WtSong> songs = fetcher.songs;
            LOG.finer("xml had " + songs.size() + " songs for " + game);
            int i = 0, total = songs.size();
            for (WtSong song : songs) {
                if (null != progress) progress.setProgress(String.format("Processing song %s of %s", i + 1, total), i, total);
                WtSong result = (WtSong) session.createQuery("FROM WtSong WHERE scoreHeroId=:shid AND gameTitle=:gameTtl").setInteger("shid", song.getScoreHeroId()).setString("gameTtl", song.getGameTitle().title).uniqueResult();
                if (null == result) {
                    LOG.info("Inserting song: " + song);
                    session.save(song);
                } else {
                    LOG.finest("found song: " + result);
                    if (result.update(song, true)) {
                        LOG.info("Updating song to: " + result);
                        session.update(result);
                    } else {
                        LOG.finest("No changes to song: " + result);
                    }
                }
                if (i % 64 == 0) {
                    session.flush();
                    session.clear();
                }
                i++;
            }
            List<SongOrder> orders = fetcher.orders;
            LOG.finer("xml had " + orders.size() + " song orderings for " + game);
            LOG.info("Deleting old song orders");
            int deletedOrderCount = session.createQuery("delete SongOrder where gameTitle=:gameTitle").setString("gameTitle", game.toString()).executeUpdate();
            LOG.finer("deleted " + deletedOrderCount + " old song orders");
            i = 0;
            total = orders.size();
            for (SongOrder order : orders) {
                order.setSong(WtSong.getByScoreHeroId(session, (WtGameTitle) game, order.getSong().getScoreHeroId()));
                LOG.info("Inserting song order: " + order);
                session.save(order);
                if (i % 64 == 0) {
                    session.flush();
                    session.clear();
                    if (null != progress) progress.setProgress("Processing song order lists...", i, total);
                }
                i++;
            }
            tx.commit();
        } catch (HibernateException e) {
            if (null != tx && tx.isActive()) tx.rollback();
            LOG.throwing("WtSongUpdater", "updateViaXml", e);
            throw e;
        } finally {
            if (null != session && session.isOpen()) session.close();
        }
    }
