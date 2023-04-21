    public static void setup() throws IOException {
        javax.swing.JFrame frame = new JFrame("PDF Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        PagePanel panel = new PagePanel();
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
        CtuluFileChooser chooser = new CtuluFileChooser(true);
        int reponse = chooser.showOpenDialog(frame);
        if (reponse != JFileChooser.APPROVE_OPTION) return;
        File file = chooser.getSelectedFile();
        RandomAccessFile raf = new RandomAccessFile(file, "r");
        FileChannel channel = raf.getChannel();
        ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
        PDFFile pdffile = new PDFFile(buf);
        PDFPage page = pdffile.getPage(0);
        panel.showPage(page);
    }
