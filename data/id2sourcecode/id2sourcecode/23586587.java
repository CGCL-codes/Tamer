    private final void addSpeedRestrictionsFromSRdbf() throws Exception {
        FileChannel in = new FileInputStream(this.srDbfFileName).getChannel();
        DbaseFileReader r = new DbaseFileReader(in, true);
        int srIdNameIndex = -1;
        int srSpeedNameIndex = -1;
        int srValDirNameIndex = -1;
        int srVerifiedNameIndex = -1;
        for (int i = 0; i < r.getHeader().getNumFields(); i++) {
            if (r.getHeader().getFieldName(i).equals(srIdName)) {
                srIdNameIndex = i;
            }
            if (r.getHeader().getFieldName(i).equals(srSpeedName)) {
                srSpeedNameIndex = i;
            }
            if (r.getHeader().getFieldName(i).equals(srValDirName)) {
                srValDirNameIndex = i;
            }
            if (r.getHeader().getFieldName(i).equals(srVerifiedName)) {
                srVerifiedNameIndex = i;
            }
        }
        if (srIdNameIndex < 0) {
            throw new NoSuchFieldException("Field name '" + srIdName + "' not found.");
        }
        if (srSpeedNameIndex < 0) {
            throw new NoSuchFieldException("Field name '" + srSpeedName + "' not found.");
        }
        if (srValDirNameIndex < 0) {
            throw new NoSuchFieldException("Field name '" + srValDirName + "' not found.");
        }
        if (srVerifiedNameIndex < 0) {
            throw new NoSuchFieldException("Field name '" + srVerifiedName + "' not found.");
        }
        log.debug("  FieldName-->Index:");
        log.debug("    " + srIdName + "-->" + srIdNameIndex);
        log.debug("    " + srSpeedName + "-->" + srSpeedNameIndex);
        log.debug("    " + srValDirName + "-->" + srValDirNameIndex);
        log.debug("    " + srVerifiedName + "-->" + srVerifiedNameIndex);
        int srCnt = 0;
        int srIgnoreCnt = 0;
        while (r.hasNext()) {
            Object[] entries = r.readEntry();
            int verified = Integer.parseInt(entries[srVerifiedNameIndex].toString());
            if (verified == 1) {
                int valdir = Integer.parseInt(entries[srValDirNameIndex].toString());
                String id = entries[srIdNameIndex].toString();
                if (valdir == 1) {
                    Link ftLink = network.getLinks().get(new IdImpl(id + "FT"));
                    Link tfLink = network.getLinks().get(new IdImpl(id + "TF"));
                    if ((ftLink == null) || (tfLink == null)) {
                        log.debug("  linkid=" + id + ", valdir=" + valdir + ": at least one link not found. Ignoring and proceeding anyway...");
                        srIgnoreCnt++;
                    } else {
                        double speed = Double.parseDouble(entries[srSpeedNameIndex].toString()) / 3.6;
                        if (speed < ftLink.getFreespeed(Time.UNDEFINED_TIME)) {
                            ftLink.setFreespeed(speed);
                            srCnt++;
                        } else {
                            srIgnoreCnt++;
                        }
                        if (speed < tfLink.getFreespeed(Time.UNDEFINED_TIME)) {
                            tfLink.setFreespeed(speed);
                            srCnt++;
                        } else {
                            srIgnoreCnt++;
                        }
                    }
                } else if (valdir == 2) {
                    Link ftLink = network.getLinks().get(new IdImpl(id + "FT"));
                    if (ftLink == null) {
                        log.debug("  linkid=" + id + ", valdir=" + valdir + ": link not found. Ignoring and proceeding anyway...");
                        srIgnoreCnt++;
                    } else {
                        double speed = Double.parseDouble(entries[srSpeedNameIndex].toString()) / 3.6;
                        if (speed < ftLink.getFreespeed(Time.UNDEFINED_TIME)) {
                            ftLink.setFreespeed(speed);
                            srCnt++;
                        } else {
                            srIgnoreCnt++;
                        }
                    }
                } else if (valdir == 3) {
                    Link tfLink = network.getLinks().get(new IdImpl(id + "TF"));
                    if (tfLink == null) {
                        log.debug("  linkid=" + id + ", valdir=" + valdir + ": link not found. Ignoring and proceeding anyway...");
                        srIgnoreCnt++;
                    } else {
                        double speed = Double.parseDouble(entries[srSpeedNameIndex].toString()) / 3.6;
                        if (speed < tfLink.getFreespeed(Time.UNDEFINED_TIME)) {
                            tfLink.setFreespeed(speed);
                            srCnt++;
                        } else {
                            srIgnoreCnt++;
                        }
                    }
                } else {
                    throw new IllegalArgumentException("linkid=" + id + ": valdir=" + valdir + " not known.");
                }
            }
        }
        log.info("  " + srCnt + " links with restricted speed assigned.");
        log.info("  " + srIgnoreCnt + " speed restrictions ignored (while verified = 1).");
    }
