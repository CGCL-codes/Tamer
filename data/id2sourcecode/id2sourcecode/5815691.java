    public ChannelIF getChannelDocument() {
        ChannelIF channel = builder.createChannel(title);
        log.debug("Channel created");
        if (type.equals("2.0")) {
            channel.setFormat(ChannelFormat.RSS_2_0);
            log.debug("RSS type set 2.0");
        } else if (type.equals("1.0")) {
            channel.setFormat(ChannelFormat.RSS_1_0);
            log.debug("RSS type set 1.0");
        } else if (type.equals("0.91")) {
            channel.setFormat(ChannelFormat.RSS_0_91);
            log.debug("RSS type set 0.91");
        } else {
            channel.setFormat(ChannelFormat.RSS_2_0);
            log.debug("RSS type set 2.0");
        }
        log.debug("Description is: " + description);
        channel.setDescription(description);
        log.debug("Number of items in the RSS document: " + items.size());
        for (Iterator<ItemIF> i = items.iterator(); i.hasNext(); ) {
            ItemIF itemAuxiliar = i.next();
            channel.addItem(itemAuxiliar);
            log.debug("Item added: " + i);
        }
        log.debug("Numero de elementos en el canal: " + channel.getItems().size());
        return channel;
    }
