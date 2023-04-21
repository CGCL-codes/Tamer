    public List<ContactChannelDisplayBean> getChannels() {
        List<ContactChannelDisplayBean> displayBeans = new ArrayList<ContactChannelDisplayBean>();
        for (ContactChannelType type : contactChannelTypes) {
            Class displayBeanClass = getContactChannelDisplayBeanClass();
            ContactChannel contactChannel = address.getChannels().get(type);
            if (contactChannel == null) {
                contactChannel = getContactChannelInstance(type);
                address.getChannels().put(type, contactChannel);
            }
            try {
                Constructor constructor = displayBeanClass.getConstructor(getContactChannelDisplayBeanConstructorSignature());
                ContactChannelDisplayBean displayBean = (ContactChannelDisplayBean) constructor.newInstance(contactChannel, type, messageSource, locale);
                displayBeans.add(displayBean);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return displayBeans;
    }
