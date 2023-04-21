    public OutputDirNotWriteableException(String msg) {
        super(String.format(Bundles.subgetBundle.getString("Cannot_read/write_to_output_directory_(%s),_aborting."), msg));
    }
