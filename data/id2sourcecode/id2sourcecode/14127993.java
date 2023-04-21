    private void saveBlip(PersistenceManager persistenceManager, HttpServletRequest request) {
        String title = request.getParameter("blip.title");
        String desc = request.getParameter("blip.desc");
        Float blipLatitude = Float.valueOf(request.getParameter("blip.loc.lat"));
        Float blipLongitude = Float.valueOf(request.getParameter("blip.loc.long"));
        Set<Key> channelKeys = getChannelKeys(request.getParameterValues("blip.channels"));
        persistenceManager.makePersistent(new Blip(title, desc, new GeoPt(blipLatitude, blipLongitude), channelKeys, null));
    }
