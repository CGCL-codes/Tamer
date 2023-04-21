    public static Gsv valueOf(final String... values) {
        Validator.notNull(values, "GSV sentences");
        if (values.length == 0) {
            throw new IllegalArgumentException("GSV sentences must not be empty.");
        }
        String[] parts = values[0].split(",");
        final int totalNumberOfMessages = Integer.parseInt(parts[1]);
        if (totalNumberOfMessages != values.length) {
            throw new NmeaFormatException(String.format("Not all messages given. Expected %s but got %s: '%s' ", totalNumberOfMessages, values.length, values[0]));
        }
        final int messageNumber = Integer.parseInt(parts[2]);
        if (messageNumber != 1) {
            throw new NmeaFormatException("Expecting message number 1, but was " + messageNumber + ".");
        }
        final int satellitesInView = Integer.parseInt(parts[3]);
        for (int i = 1; i < values.length; ++i) {
            Checksum.check(values[i]);
            parts = values[i].split(",");
            if (totalNumberOfMessages != Integer.parseInt(parts[1])) {
                throw new NmeaFormatException("Different number of messages.");
            }
            if (Integer.parseInt(parts[2]) != i + 1) {
                throw new NmeaFormatException("Expecting message number " + (i + 1) + ", but was " + messageNumber + ".");
            }
            if (satellitesInView != Integer.parseInt(parts[3])) {
                throw new NmeaFormatException("Different number of satellites in view.");
            }
        }
        final List<Info> info = new ArrayList<Info>();
        for (int i = 0; i < values.length; ++i) {
            parts = values[i].split(",");
            for (int j = 4; j < parts.length; j += 4) {
                if (info.size() < satellitesInView) {
                    String[] inf = new String[4];
                    inf[0] = parts[j + 0];
                    inf[1] = parts[j + 1];
                    inf[2] = parts[j + 2];
                    inf[3] = trim(parts[j + 3]);
                    info.add(parseInfo(inf));
                }
            }
        }
        if (info.size() != satellitesInView) {
            throw new NmeaFormatException("Different satellite infos.");
        }
        return new Gsv(totalNumberOfMessages, satellitesInView, info.toArray(new Info[0]));
    }
