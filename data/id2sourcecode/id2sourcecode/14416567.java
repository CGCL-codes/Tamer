    public String getExportURL(long epochID, SpindleDTO dto, int position) {
        SpindleIndication si = (SpindleIndication) ServletUtil.map(dto, SpindleIndication.class);
        ExportService exp = new ExportService();
        Epoch epoch = epochDAO.get(epochID);
        Date startDate = epoch.getStart();
        String startDateTxt = DateFormatUtils.format(startDate, "yyyy-MM-dd_hh-mm-ss");
        SessionPart part = partDAO.get(epoch.getSessionPartID());
        String channel = part.getChannel();
        SleepSession sleep = sleepDAO.get(part.getSleepSessionID());
        Person p = personDAO.get(sleep.getPersonID());
        String name = p.getLastName() + "_" + p.getFirstName();
        String dir = ServletUtil.getSessionExpPath(getThreadLocalRequest().getSession()) + "/";
        String filename = name + "_" + channel + "_" + startDateTxt + "_" + epochID + "_" + position + ".txt";
        exp.exportASCII(dir + filename, epoch, Util.join(epoch.getStart(), si.getStart()), Util.join(epoch.getStart(), si.getEnd()));
        return ServletUtil.DOC_BASE + getThreadLocalRequest().getSession().getId() + "/" + filename;
    }
