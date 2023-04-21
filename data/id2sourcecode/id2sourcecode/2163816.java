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
