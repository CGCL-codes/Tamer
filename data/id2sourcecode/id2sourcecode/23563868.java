    public void loadBroadcastsByChannel(List channelsIds, GregorianCalendar date, int epgSourceType) throws TVListServiceException {
        IHTTPClientSessionFactory clientSessionFactory = null;
        try {
            clientSessionFactory = new HTTPClientSessionFactory();
            IChannelSourceDAO channelSourceDAO = DAOFactory.getChannelSourceDAO(clientSessionFactory);
            IEPGSource epgSource = EPGSourceFactory.getEPGSource(epgSourceType);
            Iterator iter = channelsIds.iterator();
            while (iter.hasNext()) {
                Long channelId = (Long) iter.next();
                String channelExtId = channelDAO.getExtIdById(channelId);
                String htmlData = channelSourceDAO.getChannelSourceData(channelExtId, date, epgSourceType);
                List broadcasts = epgSource.parseChannelSource(htmlData, date);
                saveBroadcasts(broadcasts, channelId);
            }
        } catch (PersistenceException e) {
            throw new TVListServiceException(e);
        } catch (EPGParserException e) {
            throw new TVListServiceException(e);
        } finally {
            try {
                clientSessionFactory.closeSessions();
            } catch (PersistenceException e) {
            }
        }
    }
