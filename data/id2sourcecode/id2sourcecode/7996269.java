    @SuppressWarnings("unchecked")
    public T loadPartner(Long id, boolean loadMedia, boolean loadAddresses, boolean loadContactChannels) {
        T partner = (T) super.load(Partner.class, id);
        if (loadMedia) {
            partner.getMedia().isEmpty();
        }
        if (loadAddresses) {
            if (loadContactChannels) {
                for (Address address : partner.getAddresses().values()) {
                    address.getChannels().isEmpty();
                }
            } else {
                partner.getAddresses().isEmpty();
            }
        }
        return partner;
    }
