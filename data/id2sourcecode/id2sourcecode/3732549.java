    protected int Insert(String Id, String Ver, InputStream Bytes) throws PDException {
        VerifyId(Id);
        FileOutputStream fo = null;
        int Tot = 0;
        try {
            File Path = new File(getPath() + GenPath(Id, Ver));
            if (!Path.isDirectory()) Path.mkdirs();
            fo = new FileOutputStream(getPath() + GenPath(Id, Ver) + Id + "_" + Ver);
            int readed = Bytes.read(Buffer);
            while (readed != -1) {
                fo.write(Buffer, 0, readed);
                Tot += readed;
                readed = Bytes.read(Buffer);
            }
            Bytes.close();
            fo.close();
        } catch (Exception e) {
            PDException.GenPDException("Error_writing_to_file", Id + "/" + Ver + "=" + e.getLocalizedMessage());
        } finally {
            try {
                if (fo != null) fo.close();
            } catch (Exception e) {
            }
        }
        return (Tot);
    }
