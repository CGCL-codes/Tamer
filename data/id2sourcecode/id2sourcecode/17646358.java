    public static void updateViaScraping(final ResultProgressHandle progress, final GameTitle game) throws Exception {
        if (!(game instanceof RbGameTitle)) throw new IllegalArgumentException("game must be an RbGameTitle");
        Session session = null;
        Transaction tx = null;
        try {
            session = openSession();
            tx = session.beginTransaction();
            for (Game g : Game.getByTitle(game)) {
                if (null != progress) progress.setBusy("Downloading song list for " + g);
                List<RbSong> songs = RbSongScraper.scrape((RbGame) g);
                LOG.finer("scraped " + songs.size() + " songs for " + g);
                assert null != RbSongScraper.lastScrapedTiers;
                LOG.finer("remapping " + RbSongScraper.lastScrapedTiers.size() + " tiers for " + g);
                Tiers tiers = new Tiers(RbSongScraper.lastScrapedTiers);
                Tiers.setTiers(g, tiers);
                ((RbGame) g).mapTiers(tiers);
                int i = 0, total = songs.size();
                for (RbSong song : songs) {
                    if (null != progress) progress.setProgress(String.format("Processing song %s of %s", i + 1, total), i, total);
                    RbSong result = (RbSong) session.createQuery("FROM RbSong WHERE scoreHeroId=:shid AND gameTitle=:gameTtl").setInteger("shid", song.getScoreHeroId()).setString("gameTtl", song.getGameTitle().title).uniqueResult();
                    if (null == result) {
                        LOG.info("Inserting song: " + song);
                        session.save(song);
                    } else {
                        LOG.finest("found song: " + result);
                        if (result.update(song, false)) {
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
            }
            Tiers.write();
            LOG.info("Deleting old song orders");
            int deletedOrderCount = session.createQuery("delete SongOrder where gameTitle=:gameTitle").setString("gameTitle", game.toString()).executeUpdate();
            LOG.finer("deleted " + deletedOrderCount + " old song orders");
            for (Game g : Game.getByTitle(game)) {
                if (null != progress) progress.setBusy("Downloading song order lists for " + g);
                List<SongOrder> orders = RbSongScraper.scrapeOrders(progress, (RbGame) g);
                LOG.finer("scraped " + orders.size() + " song orderings for " + g);
                int i = 0, total = orders.size();
                for (SongOrder order : orders) {
                    LOG.info("Inserting song order: " + order);
                    session.save(order);
                    if (i % 64 == 0) {
                        session.flush();
                        session.clear();
                        if (null != progress) progress.setProgress("Processing song order lists...", i, total);
                    }
                    i++;
                }
            }
            tx.commit();
        } catch (HibernateException e) {
            if (null != tx && tx.isActive()) tx.rollback();
            LOG.throwing("RbSongUpdater", "updateViaScraping", e);
            throw e;
        } finally {
            if (null != session && session.isOpen()) session.close();
        }
    }
