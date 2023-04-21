    public static void main(String[] args) throws Exception {
        String file1 = args[0];
        String file2 = args[1];
        ImageReader reader = new ImageReader();
        reader.SetFileName(file1);
        boolean ret = reader.Read();
        if (!ret) {
            throw new Exception("Could not read: " + file1);
        }
        ImageChangeTransferSyntax change = new ImageChangeTransferSyntax();
        change.SetTransferSyntax(new TransferSyntax(TransferSyntax.TSType.ImplicitVRLittleEndian));
        change.SetInput(reader.GetPixmap());
        if (!change.Change()) {
            throw new Exception("Could not change: " + file1);
        }
        Pixmap out2 = (Pixmap) change.GetOutput();
        System.out.println(out2.toString());
        FileMetaInformation.SetSourceApplicationEntityTitle("Just For Fun");
        PixmapWriter writer = new PixmapWriter();
        writer.SetFileName(file2);
        writer.SetFile(reader.GetFile());
        writer.SetImage(out2);
        ret = writer.Write();
        if (!ret) {
            throw new Exception("Could not write: " + file2);
        }
    }
