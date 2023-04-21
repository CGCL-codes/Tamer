    public String getChannelAttributeValue(String attributename) {
        Iterator<Attribute> iterator = channelAttributes.iterator();
        Attribute attribute;
        String result = null;
        while (iterator.hasNext()) {
            attribute = iterator.next();
            if (attribute.name.equals(attributename)) {
                result = attribute.value;
            }
        }
        return result;
    }
