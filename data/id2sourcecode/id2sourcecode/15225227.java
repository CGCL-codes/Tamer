    public void drawSequence(Graphics g, ColourSchemeI cs, SequenceI seq, int start, int end, int x1, int y1, double width, int height, boolean showScores, boolean displayBoxes, boolean displayText, Vector pid, int seqnum, AlignViewport av, Hashtable props, int intpid[][]) {
        SequenceI topseq = av.getAlignment().getSequenceAt(0);
        Color currentColor = Color.white;
        LinkedHashMap conf = av.getGFFConfig();
        boolean confchanged = false;
        if (seq instanceof GFF) {
            GFF gff = (GFF) seq;
            if (gff == null || gff.getType() == null || width < 0.01 && gff.getType().equals("repeat")) {
                return;
            }
            Vector feat = gff.overlaps(start, end);
            for (int i = 0; i < feat.size(); i++) {
                SequenceFeature sftmp = (SequenceFeature) feat.elementAt(i);
                Vector tmpf = new Vector();
                if (sftmp.getFeatures() != null) {
                    tmpf = sftmp.getFeatures();
                } else {
                    tmpf.addElement(sftmp);
                }
                for (int j = 0; j < tmpf.size(); j++) {
                    SequenceFeature sf = (SequenceFeature) tmpf.elementAt(j);
                    Color c;
                    if (conf != null && conf.containsKey(sf.getType())) {
                        c = (Color) (conf.get(sf.getType()));
                        g.setColor(c);
                    } else {
                        c = new Color((int) (Math.random() * 200 + 50), (int) (Math.random() * 200 + 50), (int) (Math.random() * 200 + 50));
                        conf.put(sf.getType(), c);
                        g.setColor(c);
                        confchanged = true;
                    }
                    if (sf.getStrand() == 1) {
                        g.setColor(c.brighter());
                    } else {
                        g.setColor(c.darker());
                    }
                    int fstart = sf.getStart();
                    int fend = sf.getEnd();
                    if (fstart < start) {
                        fstart = start;
                    }
                    if (fend > end) {
                        fend = end + 1;
                    }
                    g.fillRect(x1 + (int) ((fstart - start - 1) * width), y1, (int) ((fend - fstart + 1) * width + 1), 3 * height / 4);
                    String name = sf.getId();
                    if (sf.getHitFeature() != null) {
                        name = sf.getHitFeature().getId();
                    }
                    if (name == null) {
                        name = sf.getType();
                    }
                    if ((width > 2 && (sf.getStart() >= start && sf.getStart() <= end) && !sf.getType().equals("repeat"))) {
                        g.setFont(f);
                        g.setColor(Color.black);
                        g.drawString(name, (int) (x1 + (int) ((fstart - start - 3 * name.length()) * width)), (int) (y1 + 3 * height / 4));
                    }
                    g.setColor(c);
                    if (sftmp.getFeatures() != null) {
                        if (j > 0 && (sftmp.getType().equals("gene") || sftmp.getType().equals("blat"))) {
                            SequenceFeature prev = (SequenceFeature) tmpf.elementAt(j - 1);
                            int tmpstart = prev.getEnd();
                            int tmpend = sf.getStart();
                            if (tmpstart < start) {
                                tmpstart = start;
                            }
                            if (tmpend > end) {
                                tmpend = end;
                            }
                            g.drawLine(x1 + (int) ((tmpstart - start) * width), y1 + 3 * height / 8, x1 + (int) ((tmpend - start + 1) * width), y1 + 3 * height / 8);
                        }
                    }
                }
                if (sftmp.getType().equals("gene")) {
                    SequenceFeature sf = (SequenceFeature) tmpf.elementAt(0);
                    String name = sf.getId();
                    if (sf.getHitFeature() != null) {
                        name = sf.getHitFeature().getId();
                    }
                    int sfstart = sftmp.getStart();
                    int sfend = sftmp.getEnd();
                    int mid = (sfstart + sfend) / 2;
                    if (mid > (start - 150 / width) && mid < (end + 150 / width)) {
                        g.setFont(f);
                        g.setColor(Color.black);
                        g.drawString(name, (int) ((x1 + ((mid - start) * width))), (int) (y1 - height / 4));
                    }
                }
            }
        }
        if (av.getMinipog() != null) {
            av.getMinipog().getAlignViewport().setGFFConfig(conf);
        }
        av.setGFFConfig(conf);
        if (confchanged) {
            av.writeGFFConfig();
        }
    }
