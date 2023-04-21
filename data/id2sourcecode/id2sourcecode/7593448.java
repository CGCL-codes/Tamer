    public XmlWriter writeCData(String cdata) throws IOException {
        logger.debug("writeCData(cdata={}) - start", cdata);
        closeOpeningTag();
        boolean hasAlreadyEnclosingCdata = cdata.startsWith(CDATA_START) && cdata.endsWith(CDATA_END);
        if (!hasAlreadyEnclosingCdata) {
            cdata = cdata.replaceAll(CDATA_END, "]]]]><![CDATA[>");
        }
        this.empty = false;
        this.wroteText = true;
        if (!hasAlreadyEnclosingCdata) this.out.write(CDATA_START);
        this.out.write(cdata);
        if (!hasAlreadyEnclosingCdata) this.out.write(CDATA_END);
        return this;
    }
