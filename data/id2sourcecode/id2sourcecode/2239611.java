    @Override
    protected void writeCallableExecuteParameters(Output out) throws SQLException, IOException {
        new Values(this.parameterMetaData).read(getDelegate()).write(out);
    }
