    public static final void showText(java.net.URL url) {
        try {
            Reader st = new InputStreamReader(url.openStream());
            int ch;
            TextArea mp = new TextArea();
            ch = st.read();
            while (ch != -1) {
                char[] cbuf = new char[1];
                cbuf[0] = (char) ch;
                mp.append(new String(cbuf));
                ch = st.read();
            }
            final Frame top = new Frame("Show URL");
            top.addWindowListener(new WindowAdapter() {

                public void windowClosing(WindowEvent windowEvent) {
                    top.dispose();
                }
            });
            top.add(mp);
            top.pack();
            top.setVisible(true);
        } catch (java.io.IOException e) {
        }
    }
