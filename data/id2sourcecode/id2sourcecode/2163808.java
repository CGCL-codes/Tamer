    public static void parse(final StringScroll scr, final ZZCell start) {
        (new Runnable() {

            long offs = 0;

            ZZCell lastCell = start;

            Hashtable ids = new Hashtable();

            String getLine() throws Exception {
                p("getline " + scr.curEnd() + " " + offs);
                int ind = -1;
                if (offs == scr.curEnd()) return null;
                String s = null;
                for (int i = 100; ind < 0 && i < scr.curEnd() - offs; i += 100) {
                    if (offs + i >= scr.curEnd()) {
                        i = (int) (scr.curEnd() - offs - 1);
                    }
                    s = scr.getString(offs, i);
                    ind = s.indexOf('\n');
                }
                if (ind < 0 && offs != scr.curEnd()) {
                    p("Problem: no newline at end!");
                    s = scr.getString(offs, (int) scr.curEnd());
                    offs = scr.curEnd();
                    return s;
                }
                s = s.substring(0, ind);
                offs += ind + 1;
                return s;
            }

            long hlstart;

            long hlend;

            String getHdrLine() throws Exception {
                hlstart = offs;
                long o0 = offs;
                String s = getLine();
                if (s.equals("")) {
                    return null;
                }
                if (startws == null) startws = new RE("^\\s");
                if (startws.getMatch(s, 0, RE.REG_ANCHORINDEX) != null) {
                    throw new ZZError("ARGH! Invalid header");
                }
                while (true) {
                    o0 = offs;
                    String extra = getLine();
                    if (startws.getMatch(extra, 0, RE.REG_ANCHORINDEX) == null) break;
                    s += extra;
                    p("Hdrline reloop " + extra);
                }
                offs = o0;
                hlend = offs - 2;
                return s;
            }

            boolean was_empty = false;

            String getBodyLine() throws Exception {
                p("Getbody");
                long o0 = offs;
                String s = getLine();
                if (s == null) return null;
                if (startfrom == null) startfrom = new RE("^From (.+) (... ... .. ..:..:.. ....)(.*)");
                if (startfrom.getMatch(s, 0, RE.REG_ANCHORINDEX) != null && was_empty) {
                    p("GETBODY: END BODY " + s);
                    offs = o0;
                    return null;
                }
                p("GETBODYRET: " + s);
                if (s.equals("")) {
                    was_empty = true;
                } else {
                    was_empty = false;
                }
                return s;
            }

            Span sp(long offs, REMatch m, int i) {
                return sp(offs, m.getSubStartIndex(i), m.getSubEndIndex(i));
            }

            Span sp(long offs, long i1, long i2) {
                return sp(i1 + offs, i2 + offs);
            }

            Span sp(long i1, long i2) {
                return Span.create(Address.scrollOffs(scr, i1), Address.scrollOffs(scr, i2));
            }

            void dohoriz(ZZCell c, long o, REMatch m, int n) {
                for (int i = 1; i < n + 1; i++) {
                    c.setSpan(sp(o, m, i));
                    if (i < n - 1) c = c.N("d.1", 1);
                }
            }

            void doMessage() throws Exception {
                lastCell = lastCell.N("d.2", 1);
                boolean received = false;
                ZZCell cur = lastCell;
                long o0 = offs;
                String s = getLine();
                if (iline == null) iline = new RE("^(From) (.*)");
                REMatch m = iline.getMatch(s, 0, RE.REG_ANCHORINDEX);
                String h;
                ZZCell fullhdr = cur;
                long soffs = o0;
                long eoffs = soffs;
                ZZCell ar = null;
                String irt = null;
                while ((h = getHdrLine()) != null) {
                    ZZCell ptr;
                    if (hdrline == null) hdrline = new RE("^(\\S+):(.*)");
                    m = hdrline.getMatch(h, 0, RE.REG_ANCHORINDEX);
                    if (m == null) throw new ZZError("Inv hdr line " + h);
                    String hid = m.toString(1);
                    int poffs = m.getSubStartIndex(2);
                    String hparm = m.toString(2);
                    eoffs = offs;
                    if (!hid.equals("Date") && !hid.equals("From") && !hid.equals("Received") && !hid.equalsIgnoreCase("Message-Id") && !hid.equals("In-Reply-To") && !hid.equals("References") && !hid.equals("Subject")) {
                        continue;
                    }
                    ptr = fullhdr;
                    if (hid.equals("Subject")) {
                        cur = cur.N("d.handle", 1);
                        cur.setText("Subject:");
                        ar = cur.N("d.1", 1);
                        ar.setText(hparm);
                        bySubject.insert("d.byfield", 1, ar);
                    } else if (hid.equals("From")) {
                        cur = cur.N("d.handle", 1);
                        cur.setText("From:");
                        ar = cur.N("d.1", 1);
                        int idx = 0;
                        hparm = hparm.replace('"', ' ');
                        ar.setText(hparm.trim());
                        bySender.insert("d.byfield", 1, ar);
                    } else if (hid.equals("Date")) {
                        cur = cur.N("d.handle", 1);
                        cur.setText("Date:");
                        ar = cur.N("d.1", 1);
                        try {
                            Calendar c = ZZDateParser.parse(hparm);
                            Date d = c.getTime();
                            ar.setText(new SimpleDateFormat().format(d));
                        } catch (NumberFormatException nfe) {
                            pa("Not a rfc822 date! Fix it !");
                            ar.setText(hparm);
                            ar = ar.N("d.1", 1);
                            ar.setText("not RFC822 compliant");
                        }
                        byDate.insert("d.byfield", 1, ar);
                    } else if (hid.equalsIgnoreCase("Message-Id")) {
                        String mi = hparm.trim();
                        p("Id: " + mi);
                        ZZCell t = byId;
                        cur = cur.N("d.handle", 1);
                        cur.setText("Message-Id:");
                        ar = cur.N("d.1", 1);
                        byId.insert("d.byfield", 1, ar);
                        ar.setText(mi);
                        ZZCell old = (ZZCell) ids.get(mi);
                        if (old != null) {
                            ZZCell clone = old.s("d.clone", 1);
                            old.excise("d.clone");
                            if (clone != null) ar.insert("d.clone", 1, clone);
                            old.excise("d.byfield");
                            ids.remove(mi);
                        }
                        ids.put(mi, ar);
                    } else if (hid.equals("In-Reply-To")) {
                        if (irt != null) pa("REFERENCES USED AND NOW I-R-T ");
                        irt = hparm;
                        try {
                            irt = irt.substring(irt.lastIndexOf('<'), irt.lastIndexOf('>') + 1);
                        } catch (StringIndexOutOfBoundsException e) {
                            irt = "";
                        }
                        if (!irt.equals("")) {
                            cur = cur.N("d.handle", 1);
                            cur.setText("In-Reply-To:");
                            ar = cur.N("d.1", 1);
                            ZZCell tmp = byId;
                            ZZCell repliedTo = (ZZCell) ids.get(irt);
                            if (repliedTo == null) {
                                repliedTo = byId.N("d.byfield", 1);
                                repliedTo.setText(irt);
                                ar.setText(irt);
                                ids.put(irt, repliedTo);
                            }
                            repliedTo.insert("d.clone", 1, ar);
                        }
                    } else if (hid.equals("References")) {
                        if (irt != null) {
                            irt = hparm;
                            try {
                                irt = irt.substring(irt.lastIndexOf('<'), irt.lastIndexOf('>') + 1);
                            } catch (StringIndexOutOfBoundsException e) {
                                irt = "";
                            }
                            if (!irt.equals("")) {
                                cur = cur.N("d.handle", 1);
                                cur.setText("In-Reply-To:");
                                ar = cur.N("d.1", 1);
                                ZZCell tmp = byId;
                                ZZCell repliedTo = (ZZCell) ids.get(irt);
                                if (repliedTo == null) {
                                    repliedTo = byId.N("d.byfield", 1);
                                    repliedTo.setText(irt);
                                    ar.setText(irt);
                                    ids.put(irt, repliedTo);
                                }
                                repliedTo.insert("d.clone", 1, ar);
                            }
                        }
                    } else if (hid.equals("Received") && !received) {
                        cur = cur.N("d.handle", 1);
                        cur.setText("ARDate:");
                        ar = cur.N("d.1", 1);
                        String dar = hparm;
                        dar = dar.substring(dar.indexOf(";") + 1, dar.length());
                        try {
                            Calendar c = ZZDateParser.parse(dar);
                            Date d = c.getTime();
                            ar.setText(new SimpleDateFormat().format(d));
                        } catch (NumberFormatException nfe) {
                            pa("Not a rfc822 date!");
                            ar.setText(dar);
                            ar = ar.N("d.1", 1);
                            ar.setText("not RFC822 compliant");
                        }
                        received = true;
                    }
                }
                ZZCell head = fullhdr.N("d.headers", 1);
                head.setSpan(sp(soffs, eoffs - 1));
                String b;
                long bod = offs;
                while ((b = getBodyLine()) != null) {
                }
                cur = cur.N("d.handle", 1);
                cur.setSpan(sp(bod, offs));
            }

            public void run() {
                try {
                    while (offs < scr.curEnd()) {
                        doMessage();
                    }
                } catch (Exception e) {
                    p("" + e);
                    e.printStackTrace();
                }
                sort();
                float date_float = 0;
                float subj_float = 0;
                float from_float = 0;
                ZZCell c = byDate.h("d.byfield", -1);
                if (c != null) {
                    int length = c.getRankLength("d.byfield") - 1;
                    c = c.s("d.byfield", 1);
                    for (int i = 0; i < length; i++) {
                        c.N("d.order", 1).setText("" + date_float);
                        date_float += 1.0 / length;
                        c = c.s("d.byfield", 1);
                    }
                }
                c = bySubject.h("d.byfield", -1);
                if (c != null) {
                    int length = c.getRankLength("d.byfield") - 1;
                    c = c.s("d.byfield", 1);
                    for (int i = 0; i < length; i++) {
                        c.N("d.order", 1).setText("" + subj_float);
                        subj_float = subj_float + ((float) 1) / length;
                        c = c.s("d.byfield", 1);
                    }
                }
                c = bySender.h("d.byfield", -1);
                if (c != null) {
                    int length = c.getRankLength("d.byfield") - 1;
                    c = c.s("d.byfield", 1);
                    for (int i = 0; i < length; i++) {
                        c.N("d.order", 1).setText("" + from_float);
                        from_float += 1.0 / length;
                        c = c.s("d.byfield", 1);
                    }
                }
            }
        }).run();
    }
