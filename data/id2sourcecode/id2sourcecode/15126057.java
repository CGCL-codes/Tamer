    private String getChannelLine(List<String> lines, String source, String nid, String tid, String sid, String rid) {
        String regex = "^.*" + source + ".*" + sid + ".*" + nid + ".*" + tid + ".*" + (rid != null ? rid + ".*" : "");
        Pattern p = Pattern.compile(regex);
        String matches = null;
        for (String line : lines) {
            Matcher m = p.matcher(line);
            if (m.find()) {
                matches = m.group();
                break;
            }
        }
        return matches;
    }
