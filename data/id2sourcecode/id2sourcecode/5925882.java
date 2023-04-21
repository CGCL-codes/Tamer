    protected synchronized void exportToIpFilter(PluginInterface aPlugin) {
        List evilEntries = getSafePeer().getCache().getEvilPeerEntries();
        Iterator it = evilEntries.iterator();
        List newRanges = new ArrayList(evilEntries.size());
        List oldRanges = new ArrayList(Arrays.asList(aPlugin.getIPFilter().getRanges()));
        Collections.sort(oldRanges);
        int newRangesCount = 0;
        while (it.hasNext()) {
            IpRange entry = (IpRange) it.next();
            IPRange range = aPlugin.getIPFilter().createRange(true);
            range.setStartIP(entry.getStartIp());
            range.setEndIP(entry.getEndIp());
            range.setDescription(entry.getDescription());
            range.checkValid();
            if (!containsRange(oldRanges, range)) {
                newRangesCount++;
                aPlugin.getIPFilter().addRange(range);
            }
        }
        if (0 == newRangesCount) aPlugin.getIPFilter().markAsUpToDate();
        LoggerChannel logger = aPlugin.getLogger().getChannel("SafePeer");
        String entry = "entry";
        if (newRangesCount > 1) entry = "entries";
        String logMsg = "SafePeer exported " + newRangesCount + " " + entry + " to the IpFilter (currently " + aPlugin.getIPFilter().getRanges().length + " " + entry + ")";
        logger.log(LoggerChannel.LT_INFORMATION, logMsg);
        long end = System.currentTimeMillis();
        DefaultEvilIPCache.getInstance().clear();
        MiniLogger log = DefaultMiniLogger.getInstance(SafePeer.LOG_IDENTIFIER);
        log.info(logMsg);
    }
