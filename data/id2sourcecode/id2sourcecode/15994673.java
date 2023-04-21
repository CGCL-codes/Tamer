    private void initReader() {
        if (this.special_config.getParameter("SMARTPIX_VERSION").equals(AccuChekSmartPixSpecialConfig.SMARTPIX_V2)) this.reader = new AccuChekSmartPixReaderV2(m_da, this.output_writer, this); else this.reader = new AccuChekSmartPixReaderV3(m_da, this.output_writer, this);
        try {
            openDevice();
        } catch (Exception ex) {
            log.error("Error checking if device present. Ex.: " + ex, ex);
        }
    }
