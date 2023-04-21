    public Editor(String arg, MainFrame frame, StringBuffer s) {
        charset = Charset.forName(wlFxp.getConfig().locale());
        decoder = charset.newDecoder();
        text = new JTextPane();
        text.setEditable(false);
        text.setFont(new Font("SansSerif", Font.PLAIN, 12));
        if (arg != null) {
            s = new StringBuffer(16000);
            try {
                FileInputStream fis = new FileInputStream(new File(arg));
                FileChannel fc = fis.getChannel();
                CharBuffer cb;
                while (fc.read(buf) != -1) {
                    buf.flip();
                    cb = decoder.decode(buf);
                    s.append(cb);
                    buf.clear();
                }
            } catch (Exception e) {
            }
        }
        text.setText(s.toString());
        scrollPane = new JScrollPane(text);
        getContentPane().add(scrollPane);
        setSize(500, 600);
        setLocationRelativeTo(frame);
        text.setCaretPosition(0);
        setTitle(arg);
        setVisible(true);
    }
