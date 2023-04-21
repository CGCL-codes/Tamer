    public JFrameHelp(int demo) {
        initComponents();
        try {
            this.setTitle("Help :: live descriptor example number " + demo);
            jTextPane1.setContentType("text/plain");
            URL url = new URL("http://csvtosql.sourceforge.net/descriptors/" + demo + "/descriptor.xml");
            BufferedReader html = new BufferedReader(new InputStreamReader(url.openStream()));
            String strHtmlrow = new String();
            StringBuffer sb = new StringBuffer();
            while ((strHtmlrow = html.readLine()) != null) {
                sb.append(strHtmlrow + "\n");
            }
            jTextPane1.setText(sb.toString());
            jTextPane1.setCaretPosition(0);
        } catch (FileNotFoundException e) {
            jTextPane1.setText("this example no longer exist. sorry :)");
        } catch (Exception e) {
            jTextPane1.setText("need internet connection.");
        }
    }
