    public HTMLInfoPanel(InstallerFrame parent, InstallData idata, String resPrefixStr, boolean showInfoLabelFlag) {
        super(parent, idata, new IzPanelLayout());
        panelResourcePrefixStr = resPrefixStr;
        panelResourceNameStr = resPrefixStr + ".info";
        if (showInfoLabelFlag) {
            add(LabelFactory.create(parent.langpack.getString("InfoPanel.info"), parent.icons.getImageIcon("edit"), LEADING), NEXT_LINE);
        }
        try {
            textArea = new JEditorPane() {

                protected InputStream getStream(URL urlObj) throws IOException {
                    final InputStream inStm = super.getStream(urlObj);
                    final ByteArrayOutputStream btArrOutStm = new ByteArrayOutputStream();
                    int b;
                    final byte[] buff = new byte[2048];
                    while ((b = inStm.read(buff, 0, buff.length)) > 0) btArrOutStm.write(buff, 0, b);
                    final String parsedStr = parseText(btArrOutStm.toString());
                    return new ByteArrayInputStream(parsedStr.getBytes());
                }
            };
            textArea.setContentType("text/html; charset=utf-8");
            textArea.setEditable(false);
            textArea.addHyperlinkListener(new HyperlinkHandler());
            JScrollPane scroller = new JScrollPane(textArea);
            textArea.setPage(loadHTMLInfoContent());
            textArea.setCaretPosition(0);
            add(scroller, NEXT_LINE);
        } catch (Exception err) {
            err.printStackTrace();
        }
        getLayoutHelper().completeLayout();
    }
