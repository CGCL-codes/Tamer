    @SuppressWarnings("unchecked")
    public List getAddresses(Long partnerID) {
        List<Address> list = getSession().createQuery("select distinct p.addresses from " + Partner.class.getName() + " p  where p.id=:id and p.addresses.deleted=false").setParameter("id", partnerID).list();
        for (Address address : list) {
            address.getChannels().isEmpty();
        }
        return list;
    }
