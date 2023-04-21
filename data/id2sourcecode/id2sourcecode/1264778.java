    @Description("If true, the builder will write all output to a file that can be read by the " + "Dashboard reporting application while the builder is executing.")
    @Optional
    @Default("false")
    public void setLiveOutput(final boolean showAntOutput) {
        delegate.setLiveOutput(showAntOutput);
    }
