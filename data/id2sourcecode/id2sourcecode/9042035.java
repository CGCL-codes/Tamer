    private void initialize() {
        GridData gridData1 = new GridData();
        gridData1.horizontalAlignment = GridData.CENTER;
        gridData1.verticalAlignment = GridData.BEGINNING;
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        GridData gridData = new GridData();
        gridData.horizontalAlignment = GridData.FILL;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        gridData.verticalAlignment = GridData.FILL;
        dataText = new Text(this, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
        dataText.setLayoutData(gridData);
        dataText.addModifyListener(new org.eclipse.swt.events.ModifyListener() {

            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                messageData.setBinaryData(dataText.getText().getBytes());
            }
        });
        loadFromFileButton = new Button(this, SWT.NONE);
        loadFromFileButton.setText("Load from file...");
        loadFromFileButton.setLayoutData(gridData1);
        loadFromFileButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                FileDialog fileDialog = new FileDialog(getShell());
                String filePath = fileDialog.open();
                FileInputStream fis = null;
                ByteArrayOutputStream baos = null;
                if (filePath != null) {
                    try {
                        fis = new FileInputStream(filePath);
                        baos = new ByteArrayOutputStream();
                        byte[] buffer = new byte[1024];
                        int read = fis.read(buffer, 0, 1024);
                        while (read > -1) {
                            baos.write(buffer, 0, read);
                            read = fis.read(buffer, 0, 1024);
                        }
                        messageData.setBinaryData(baos.toByteArray());
                        initFromMessageData();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    } finally {
                        closeQuitely(fis);
                        closeQuitely(baos);
                    }
                }
            }
        });
        this.setLayout(gridLayout);
        setSize(new Point(825, 419));
    }
