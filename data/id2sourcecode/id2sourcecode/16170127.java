    public static void write(org.omg.CORBA.portable.OutputStream ostream, library.corba.ReaderAccountInfo value) {
        ostream.write_ulong(value.readerID);
        ostream.write_long(value.accountInfo.length);
        for (int _i0 = 0; _i0 < value.accountInfo.length; ++_i0) library.corba.AccountInfoPerLibraryHelper.write(ostream, value.accountInfo[_i0]);
    }
