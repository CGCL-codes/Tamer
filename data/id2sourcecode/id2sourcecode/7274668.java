    public TvDocument grab() {
        if (log.isInfoEnabled()) {
            log.info("Starting the grabber");
        }
        if (getFactory().getCache() != null) {
            doneSignal = new CountDownLatch(channels.size() * daysToGrab + 1);
            execute(getFactory().getCache().createCleaner());
        } else {
            doneSignal = new CountDownLatch(channels.size() * daysToGrab);
        }
        SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyyMMddHHmmss ZZZZ");
        int days = getDaysToGrab();
        TvDocument doc = TvDocument.Factory.newInstance();
        Tv tv = doc.addNewTv();
        tv.setGeneratorInfoName("anuta xmltv generator");
        tv.setGeneratorInfoUrl("http://www.anuta.org/xmltv");
        tv.setDate(sdfDateTime.format(new Date()));
        tv.setSourceDataUrl("http://www.tvgids.nl");
        int day = 0;
        while (day < days) {
            if (log.isDebugEnabled()) {
                log.debug("We are here");
            }
            for (Channel channel : getChannels()) {
                XMLTVGrabberTask task = getFactory().getGrabberTask(day, channel);
                execute(task);
            }
            day++;
        }
        log.debug("Start waiting");
        try {
            doneSignal.await();
        } catch (InterruptedException e) {
            log.debug(e);
            Thread.currentThread().interrupt();
        }
        log.debug("End waiting");
        shutdown();
        try {
            awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            log.debug(e);
            Thread.currentThread().interrupt();
        }
        for (Channel channel : getChannels()) {
            if (log.isDebugEnabled()) {
                log.debug("Add channel " + channel + " to xml");
            }
            org.anuta.xmltv.xmlbeans.Channel channelXml = tv.addNewChannel();
            channelXml.setId(channel.getXmltvChannelId());
            channelXml.setDisplayName(StringHelper.unescapeHTML(channel.getChannelName()));
            if (channel.getChannelLogo() != null) {
                Image icon = channelXml.addNewIcon();
                icon.setSrc(channel.getChannelLogo());
            }
        }
        Iterator it = queue.iterator();
        while (it.hasNext()) {
            TvDocument chandoc = (TvDocument) it.next();
            Programme[] progs = chandoc.getTv().getProgrammeArray();
            if ((progs != null) && (progs.length > 0)) {
                for (int i = 0; i < progs.length; i++) {
                    tv.addNewProgramme().set(progs[i]);
                }
            }
        }
        log.debug("ENDED");
        return doc;
    }
