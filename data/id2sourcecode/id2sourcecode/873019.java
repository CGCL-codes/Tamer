    void display() {
        boolean gotTime = false;
        boolean gotPos = false;
        boolean gotHeading = false;
        String dline;
        long oldtime = 0, thistime;
        while (dispThread != null) {
            synchronized (this) {
                int ol;
                while (nline < 1) {
                    try {
                        CAux.perr("NMEA wait", 2);
                        wait();
                    } catch (InterruptedException ie) {
                        CAux.perr("NMEA wake up", 2);
                    }
                }
                ol = lindex - nline;
                if (ol < 0) {
                    ol = ol + NLine;
                }
                tok++;
                dline = inputLine[ol];
                nline--;
            }
            thistime = System.currentTimeMillis();
            if ((thistime - oldtime) / 1000 > GMenv.NMEAInterval) {
                int lres;
                if (CAux.tLevel > 2) {
                    CAux.perr("NMEA dT=" + thistime + "-" + oldtime + " =" + (thistime - oldtime) / 1000 + " I=" + GMenv.NMEAInterval, 1);
                }
                lres = NMEAd.processLine(dline, true, gotTime || (GMenv.NMEAInterval == 0), !gotPos);
                if (lres == GOTPOS || lres == GOTPOSTIME || lres == GOTPOSHEADING) {
                    gotPos = true;
                }
                if (lres == GOTHEADING || lres == GOTPOSHEADING) {
                    gotHeading = true;
                }
                if (lres == GOTPOSTIME || lres == GOTTIME) {
                    gotTime = true;
                }
                if (gotPos && gotTime && gotHeading) {
                    oldtime = thistime;
                    gotHeading = false;
                    gotPos = false;
                    gotTime = false;
                }
            }
        }
    }
