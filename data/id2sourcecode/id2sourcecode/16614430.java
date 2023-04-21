    public void DMovieNextFrame() {
        Enumeration pplist = displaysOfParticipants.elements();
        while (pplist.hasMoreElements()) {
            ParticipantMovie pm = (ParticipantMovie) pplist.nextElement();
            Participant p = pm.me;
            Participant other = pm.other;
            if (other != null) {
                String readwrite = other.isTyping(super.getIsTypingTimeOut()) ? "write" : "read";
                if (readwrite.contentEquals("read")) {
                    String URL = pm.baseURL + readwrite + dirsep + String.format("%05d.html", pm.readingFrameNumber);
                    c.changeWebpage(p, "webpage", URL);
                    pm.readingFrameNumber = (pm.readingFrameNumber + 1) % maxFrameNum;
                } else {
                    String URL = pm.baseURL + readwrite + dirsep + String.format("%05d.html", pm.writingFrameNumber);
                    c.changeWebpage(p, "webpage", URL);
                    pm.writingFrameNumber = (pm.writingFrameNumber + 1) % maxFrameNum;
                }
            }
        }
    }
