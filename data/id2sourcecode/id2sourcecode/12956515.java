    public static void updateViaScraping(final ResultProgressHandle progress, final GameTitle game) throws Exception {
        if (!(game instanceof WtGameTitle)) throw new IllegalArgumentException("game must be a WtGameTitle");
        Session session = null;
        Transaction tx = null;
        try {
            session = openSession();
            tx = session.beginTransaction();
            for (Game g : Game.getByTitle(game)) {
                if (null != progress) progress.setBusy("Downloading song list for " + g);
                List<WtSong> songs = WtSongScraper.scrape((WtGame) g);
                LOG.finer("scraped " + songs.size() + " songs for " + g);
                assert null != WtSongScraper.lastScrapedTiers;
                LOG.finer("remapping " + WtSongScraper.lastScrapedTiers.size() + " tiers");
                Tiers tiers = new Tiers(WtSongScraper.lastScrapedTiers);
                Tiers.setTiers(g, tiers);
                ((WtGame) g).mapTiers(tiers);
                int i = 0, total = songs.size();
                for (WtSong song : songs) {
                    if (null != progress) progress.setProgress(String.format("Processing song %s of %s", i + 1, total), i, total);
                    WtSong result = (WtSong) session.createQuery("FROM WtSong WHERE scoreHeroId=:shid AND gameTitle=:gameTtl").setInteger("shid", song.getScoreHeroId()).setString("gameTtl", song.getGameTitle().title).uniqueResult();
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
                    i++;
                }
                if (((WtGameTitle) g.title).supportsExpertPlus) {
                    if (null != progress) progress.setBusy("Checking Expert+ song status for " + g);
                    songs = WtSongScraper.scrape((WtGame) g, Instrument.Group.DRUMS, Difficulty.EXPERT_PLUS);
                    LOG.finer("scraped " + songs.size() + " songs for expert+ check for " + g);
                    i = 0;
                    total = songs.size();
                    for (WtSong song : songs) {
                        if (null != progress) progress.setProgress(String.format("Processing song %s of %s", i + 1, total), i, total);
                        WtSong result = (WtSong) session.createQuery("FROM WtSong WHERE scoreHeroId=:shid AND gameTitle=:gameTtl").setInteger("shid", song.getScoreHeroId()).setString("gameTtl", song.getGameTitle().title).uniqueResult();
                        if (null == result) {
                            LOG.warning("Didn't find song in DB during Expert+ check: " + song);
                        } else {
                            LOG.finest("found song: " + result);
                            result.setExpertPlusSupported(true);
                            LOG.info("Updating song to supprot Expert+: " + result);
                            session.update(result);
                        }
                        if (i % 64 == 0) {
                            session.flush();
                            session.clear();
                        }
                        i++;
                    }
                }
            }
            Tiers.write();
            LOG.info("Deleting old song orders");
            int deletedOrderCount = session.createQuery("delete SongOrder where gameTitle=:gameTitle").setString("gameTitle", game.toString()).executeUpdate();
            LOG.finer("deleted " + deletedOrderCount + " old song orders");
            for (Game g : Game.getByTitle(game)) {
                if (null != progress) progress.setBusy("Downloading song order lists for " + g);
                List<SongOrder> orders = WtSongScraper.scrapeOrders(progress, (WtGame) g);
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
            LOG.throwing("WtSongUpdater", "updateViaScraping", e);
            throw e;
        } finally {
            if (null != session && session.isOpen()) session.close();
        }
    }
