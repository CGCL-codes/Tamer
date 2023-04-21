    public void testUpdateExisting() throws Exception {
        long pk = nextLong();
        WSRPPortlet newWSRPPortlet = _persistence.create(pk);
        newWSRPPortlet.setName(randomString());
        newWSRPPortlet.setChannelName(randomString());
        newWSRPPortlet.setTitle(randomString());
        newWSRPPortlet.setShortTitle(randomString());
        newWSRPPortlet.setDisplayName(randomString());
        newWSRPPortlet.setKeywords(randomString());
        newWSRPPortlet.setStatus(nextInt());
        newWSRPPortlet.setProducerEntityId(randomString());
        newWSRPPortlet.setConsumerId(randomString());
        newWSRPPortlet.setPortletHandle(randomString());
        newWSRPPortlet.setMimeTypes(randomString());
        _persistence.update(newWSRPPortlet, false);
        WSRPPortlet existingWSRPPortlet = _persistence.findByPrimaryKey(newWSRPPortlet.getPrimaryKey());
        assertEquals(existingWSRPPortlet.getPortletId(), newWSRPPortlet.getPortletId());
        assertEquals(existingWSRPPortlet.getName(), newWSRPPortlet.getName());
        assertEquals(existingWSRPPortlet.getChannelName(), newWSRPPortlet.getChannelName());
        assertEquals(existingWSRPPortlet.getTitle(), newWSRPPortlet.getTitle());
        assertEquals(existingWSRPPortlet.getShortTitle(), newWSRPPortlet.getShortTitle());
        assertEquals(existingWSRPPortlet.getDisplayName(), newWSRPPortlet.getDisplayName());
        assertEquals(existingWSRPPortlet.getKeywords(), newWSRPPortlet.getKeywords());
        assertEquals(existingWSRPPortlet.getStatus(), newWSRPPortlet.getStatus());
        assertEquals(existingWSRPPortlet.getProducerEntityId(), newWSRPPortlet.getProducerEntityId());
        assertEquals(existingWSRPPortlet.getConsumerId(), newWSRPPortlet.getConsumerId());
        assertEquals(existingWSRPPortlet.getPortletHandle(), newWSRPPortlet.getPortletHandle());
        assertEquals(existingWSRPPortlet.getMimeTypes(), newWSRPPortlet.getMimeTypes());
    }
