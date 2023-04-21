    public HexTextbox(Composite parent, int style, int numBytes, char defaultValue) {
        super(parent, style);
        this.numBytes = numBytes;
        this.fillValue = defaultValue;
        this.text = new char[2 * numBytes];
        for (int i = 0; i < text.length; i += 2) text[i] = text[i + 1] = defaultValue;
        errorToolTip = new ToolTip(parent.getShell(), SWT.BALLOON | SWT.ERROR);
        errorToolTip.setText(Messages.getString("HexTextbox.ErrorToolTip.Title"));
        errorToolTip.setMessage(Messages.getString("HexTextbox.ErrorToolTip.Message"));
        errorToolTip.setAutoHide(true);
        super.setTextLimit(3 * numBytes - 1);
        showText();
        super.addKeyListener(new HexTextboxKeyListener());
    }
