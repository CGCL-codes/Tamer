    @Override
    public String toString() {
        StringBuilder rv = new StringBuilder();
        Calendar c = Calendar.getInstance();
        c.setTime(begin);
        c.add(Calendar.MINUTE, (-1) * preStart);
        Date nBegin = c.getTime();
        c.setTime(end);
        c.add(Calendar.MINUTE, postEnd);
        Date nEnd = c.getTime();
        rv.append(status);
        rv.append(VDR_SEPARATOR);
        rv.append(getChannelId());
        rv.append(VDR_SEPARATOR);
        rv.append(dayFormat.format(nBegin));
        rv.append(VDR_SEPARATOR);
        rv.append(timeFormat.format(nBegin));
        rv.append(VDR_SEPARATOR);
        rv.append(timeFormat.format(nEnd));
        rv.append(VDR_SEPARATOR);
        rv.append(String.format("%02d", priority));
        rv.append(VDR_SEPARATOR);
        rv.append(String.format("%02d", lifeTime));
        rv.append(VDR_SEPARATOR);
        if (pathPrefix != null && pathPrefix.length() > 1) {
            rv.append(pathPrefix.replace("/", "~"));
            rv.append("~");
        }
        if (name != null) rv.append(name.replace("\n", "|").replace(VDR_SEPARATOR, "~").replace(" ", "_")); else rv.append("<no name?>");
        if (event != null) {
            rv.append(VDR_SEPARATOR);
            rv.append(createXInfo());
        }
        return rv.toString();
    }
