    AsteriskChannelImpl getOtherSideOfLocalChannel(AsteriskChannel localChannel) {
        final String name;
        final char num;
        if (localChannel == null) {
            return null;
        }
        name = localChannel.getName();
        if (name == null || !name.startsWith("Local/") || (name.charAt(name.length() - 2) != ',' && name.charAt(name.length() - 2) != ';')) {
            return null;
        }
        num = name.charAt(name.length() - 1);
        if (num == '1') {
            return getChannelImplByName(name.substring(0, name.length() - 1) + "2");
        } else if (num == '2') {
            return getChannelImplByName(name.substring(0, name.length() - 1) + "1");
        } else {
            return null;
        }
    }
