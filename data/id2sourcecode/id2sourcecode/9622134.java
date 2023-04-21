    static NoticeEvent notice(String data, Connection con) {
        Matcher m = noticePattern1.matcher(data);
        if (m.matches()) {
            NoticeEvent noticeEvent = new NoticeEventImpl(data, myManager.getSessionFor(con), "generic", m.group(1), "", "", null);
            return noticeEvent;
        }
        m = noticePattern2.matcher(data);
        if (m.matches()) {
            NoticeEvent ne = new NoticeEventImpl(data, myManager.getSessionFor(con), "channel", m.group(3), "", m.group(1), con.getChannel(m.group(2).toLowerCase()));
            return ne;
        }
        m = noticePattern3.matcher(data);
        if (m.matches()) {
            NoticeEvent ne = new NoticeEventImpl(data, myManager.getSessionFor(con), "user", m.group(3), m.group(2), m.group(1), null);
            return ne;
        }
        m = noticePattern4.matcher(data);
        if (m.matches()) {
            NoticeEvent ne = new NoticeEventImpl(data, myManager.getSessionFor(con), "user", m.group(3), m.group(2), m.group(1), null);
            return ne;
        }
        debug("NOTICE", data);
        return null;
    }
