    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("usage: java PDFInfo pdfspec");
            return;
        }
        RandomAccessFile raf = new RandomAccessFile(new File(args[0]), "r");
        FileChannel fc = raf.getChannel();
        ByteBuffer buf = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
        PDFFile pdfFile = new PDFFile(buf);
        System.out.println("Major version = " + pdfFile.getMajorVersion());
        System.out.println("Minor version = " + pdfFile.getMinorVersion());
        System.out.println("Version string = " + pdfFile.getVersionString() + "\n");
        System.out.println("Is printable = " + pdfFile.isPrintable());
        System.out.println("Is saveable = " + pdfFile.isSaveable() + "\n");
        OutlineNode oln = pdfFile.getOutline();
        if (oln != null) {
            System.out.println("Outline\n");
            Enumeration e = oln.preorderEnumeration();
            while (e.hasMoreElements()) {
                DefaultMutableTreeNode node;
                node = (DefaultMutableTreeNode) e.nextElement();
                System.out.println(node);
            }
        }
    }
