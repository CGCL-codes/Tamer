    @SuppressWarnings("unchecked")
    public List getAddresses(Long partnerID, String addressTypeName) {
        AddressType type = new AddressType();
        type.setId(1);
        List<Address> list = getSession().createQuery("select p.addresses from " + Partner.class.getName() + " p where p.id=:id and p.addresses.addressType=:addressType and p.addresses.deleted=false").setParameter("id", partnerID).setParameter("addressType", type).list();
        for (Address address : list) {
            address.getChannels().isEmpty();
        }
        return list;
    }
