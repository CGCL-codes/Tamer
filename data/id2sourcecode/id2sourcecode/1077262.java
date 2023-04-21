    void fixPubDate() throws ParseException {
        SimpleDateFormat sdf = null;
        if (strings.matches(pubDatePattern, this.text)) {
            setPubDateStr(strings.getGroup(1));
            if (Character.isDigit(this.pubDateStr.charAt(this.pubDateStr.length() - 1))) {
                sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US);
            } else {
                sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
            }
            try {
                this.pubDate = sdf.parse(this.getPubDateStr());
                sdf.setTimeZone(TimeZone.getDefault());
                StringBuffer sb = new StringBuffer(30);
                setPubDateStr(sdf.format(this.pubDate, sb, new FieldPosition(0)).toString());
            } catch (ParseException pe) {
                throw new ParseException("Unparseable date \"" + this.getPubDateStr() + "\" in " + this.text.substring(0, 200), pe.getErrorOffset());
            }
        } else if (strings.matches(dcDatePattern, this.text)) {
            String dcDateStr = strings.substitute("(.+) (00:00)$", "$1+$2", strings.getGroup(1));
            dcDateStr = strings.substitute("(.+)(\\d\\d):(\\d\\d)$", "$1$2$3", dcDateStr);
            try {
                sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US);
                this.pubDate = sdf.parse(dcDateStr);
                setPubDateStr(this.pubDate);
            } catch (ParseException pe) {
                throw new ParseException("Unparseable date \"" + dcDateStr + "\" in " + this.text.substring(0, 200), pe.getErrorOffset());
            }
        }
    }
