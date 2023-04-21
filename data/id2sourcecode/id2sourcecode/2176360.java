    protected int Insert(String Id, String Ver, InputStream Bytes) throws PDException {
        OutputStream fo = null;
        int Tot = 0;
        try {
            fo = ftpCon.storeFileStream(GenPath(Id, Ver) + Id);
            int readed = Bytes.read(Buffer);
            while (readed != -1) {
                fo.write(Buffer, 0, readed);
                Tot += readed;
                readed = Bytes.read(Buffer);
            }
            Bytes.close();
            ftpCon.completePendingCommand();
            fo.close();
        } catch (IOException ex) {
            PDException.GenPDException("Error_writing_file_to_ftp", Id + "/" + Ver + "=" + ex.getLocalizedMessage());
        }
        return (Tot);
    }
