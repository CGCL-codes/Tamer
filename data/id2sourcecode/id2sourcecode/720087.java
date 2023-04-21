    public void setup() throws IOException {
        JFrame frame = new JFrame("PDF Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        PagePanel pagePanel = new PagePanel() {

            @Override
            public void paint(Graphics g) {
                super.paint(g);
                for (Component c : getComponents()) {
                    c.repaint();
                }
            }
        };
        pagePanel.setLayout(null);
        createTextField(pagePanel);
        frame.add(pagePanel);
        int dpi = frame.getToolkit().getScreenResolution();
        System.out.println(dpi);
        frame.setSize(765, 990);
        frame.setVisible(true);
        File file = new File("test.pdf");
        RandomAccessFile raf = new RandomAccessFile(file, "r");
        FileChannel channel = raf.getChannel();
        ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
        PDFFile pdffile = new PDFFile(buf);
        PDFPage page = pdffile.getPage(1, true);
        pagePanel.showPage(page);
    }
