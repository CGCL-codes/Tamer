    @Override
    public final void setFile(final File _f) {
        file = _f;
        if (analyze_ == null) {
            analyze_ = new CtuluLog();
        }
        analyze_.setDesc(super.getOperationDescription(_f));
        try {
            in = new FileInputStream(file);
            helper = new NativeNIOHelperCorrected(in.getChannel());
            helper.inverseOrder();
        } catch (final FileNotFoundException _e) {
            analyze_.manageException(_e);
            if (in != null) {
                CtuluLibFile.close(in);
            }
        }
    }
