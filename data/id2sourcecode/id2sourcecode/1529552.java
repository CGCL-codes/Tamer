    public void create_file_req(CreateRequest Q, Channel CH) {
        try {
            FileDesc fd;
            String NC;
            do {
                NC = alloc_temp();
                fd = dh.describe(NC);
            } while (fd.exists());
            final int mode = Modes.FailIfExist | Modes.CreateAlways;
            final WriteHandle fh = fd.openWrite(mode);
            final FileReader rd = Q.RD();
            final long S = rd.available();
            while (rd.available() > 0L) fh.write(rd.read(0x4000000));
            fh.close();
            String R = _finalize(NC, S);
            CH.string_success(R);
        } catch (FileOpFailure e) {
            CH.fileop_failure(e);
        }
    }
