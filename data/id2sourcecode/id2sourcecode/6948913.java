    private Viewer() throws IOException {
        for (int i = 0; i < stats.length; ++i) {
            stats[i] = 0;
        }
        frame = new JFrame("Viewer");
        final JFileChooser fc = new JFileChooser("F:\\DigicorderImages\\dump HDS2\\");
        int returnVal = fc.showOpenDialog(frame);
        if (returnVal != JFileChooser.APPROVE_OPTION) {
            System.exit(0);
        }
        FileInputStream fis = new FileInputStream(fc.getSelectedFile());
        FileChannel chan = fis.getChannel();
        buff = chan.map(MapMode.READ_ONLY, 0, fis.available());
        chan.close();
        fis.close();
        System.out.println("Size: " + buff.capacity());
        blocks = new ArrayList<InfoBlockEntry>();
        buff.getInt();
        buff.order(ByteOrder.BIG_ENDIAN);
        if (buff.getInt() != 1) {
            buff.order(ByteOrder.LITTLE_ENDIAN);
        }
        buff.rewind();
        InfoBlockEntry tmp;
        do {
            tmp = new InfoBlockEntry();
            tmp.parseData(buff);
            addToStats(tmp);
            blocks.add(tmp);
        } while (tmp.getType() != 0x02);
        System.out.println(blocks.size());
        System.out.println("STATISTICS:");
        for (int i = 0; i < stats.length; ++i) {
            System.out.println(i + ": " + stats[i] / CLUSTERS_PER_MB + " (" + stats[i] + ")");
        }
        try {
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainPanel = new JPanel();
            mainPanel.setLayout(new BorderLayout());
            jbInit();
            frame.add(mainPanel);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        frame.pack();
        frame.validate();
        frame.setVisible(true);
    }
