    private void namesLine(String data, Connection con) {
        Pattern p = Pattern.compile("^:(?:.+?)\\s+353\\s+\\S+\\s+(?:=|@)\\s+(\\S+)\\s:(.+)$");
        Matcher m = p.matcher(data);
        if (m.matches()) {
            Channel chan = con.getChannel(m.group(1).toLowerCase());
            String[] names = m.group(2).split("\\s+");
            for (String name : names) {
                if (name != null && name.length() > 0) {
                    chan.addNick(name);
                }
            }
        }
    }
