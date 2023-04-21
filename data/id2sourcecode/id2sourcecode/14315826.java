    private void run2(final Network network) throws Exception {
        log.info("running " + this.getClass().getName() + " module...");
        FileChannel in = new FileInputStream(this.srDbfFileName).getChannel();
        DbaseFileReader r = new DbaseFileReader(in, true);
        int srIdNameIndex = -1;
        int srSpeedNameIndex = -1;
        int srValDirNameIndex = -1;
        int srVerifiedNameIndex = -1;
        for (int i = 0; i < r.getHeader().getNumFields(); i++) {
            if (r.getHeader().getFieldName(i).equals(SR_ID_NAME)) {
                srIdNameIndex = i;
            }
            if (r.getHeader().getFieldName(i).equals(SR_SPEED_NAME)) {
                srSpeedNameIndex = i;
            }
            if (r.getHeader().getFieldName(i).equals(SR_VALDIR_NAME)) {
                srValDirNameIndex = i;
            }
            if (r.getHeader().getFieldName(i).equals(SR_VERIFIED_NAME)) {
                srVerifiedNameIndex = i;
            }
        }
        if (srIdNameIndex < 0) {
            throw new NoSuchFieldException("Field name '" + SR_ID_NAME + "' not found.");
        }
        if (srSpeedNameIndex < 0) {
            throw new NoSuchFieldException("Field name '" + SR_SPEED_NAME + "' not found.");
        }
        if (srValDirNameIndex < 0) {
            throw new NoSuchFieldException("Field name '" + SR_VALDIR_NAME + "' not found.");
        }
        if (srVerifiedNameIndex < 0) {
            throw new NoSuchFieldException("Field name '" + SR_VERIFIED_NAME + "' not found.");
        }
        log.trace("  FieldName-->Index:");
        log.trace("    " + SR_ID_NAME + "-->" + srIdNameIndex);
        log.trace("    " + SR_SPEED_NAME + "-->" + srSpeedNameIndex);
        log.trace("    " + SR_VALDIR_NAME + "-->" + srValDirNameIndex);
        log.trace("    " + SR_VERIFIED_NAME + "-->" + srVerifiedNameIndex);
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
                        log.trace("  linkid=" + id + ", valdir=" + valdir + ": at least one link not found. Ignoring and proceeding anyway...");
                        srIgnoreCnt++;
                    } else {
                        double speed = Double.parseDouble(entries[srSpeedNameIndex].toString()) / 3.6;
                        if (speed < ftLink.getFreespeed()) {
                            ftLink.setFreespeed(speed);
                            srCnt++;
                        } else {
                            srIgnoreCnt++;
                        }
                        if (speed < tfLink.getFreespeed()) {
                            tfLink.setFreespeed(speed);
                            srCnt++;
                        } else {
                            srIgnoreCnt++;
                        }
                    }
                } else if (valdir == 2) {
                    Link ftLink = network.getLinks().get(new IdImpl(id + "FT"));
                    if (ftLink == null) {
                        log.trace("  linkid=" + id + ", valdir=" + valdir + ": link not found. Ignoring and proceeding anyway...");
                        srIgnoreCnt++;
                    } else {
                        double speed = Double.parseDouble(entries[srSpeedNameIndex].toString()) / 3.6;
                        if (speed < ftLink.getFreespeed()) {
                            ftLink.setFreespeed(speed);
                            srCnt++;
                        } else {
                            srIgnoreCnt++;
                        }
                    }
                } else if (valdir == 3) {
                    Link tfLink = network.getLinks().get(new IdImpl(id + "TF"));
                    if (tfLink == null) {
                        log.trace("  linkid=" + id + ", valdir=" + valdir + ": link not found. Ignoring and proceeding anyway...");
                        srIgnoreCnt++;
                    } else {
                        double speed = Double.parseDouble(entries[srSpeedNameIndex].toString()) / 3.6;
                        if (speed < tfLink.getFreespeed()) {
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
        log.info("done.");
    }
