            public void widgetSelected(SelectionEvent e) {
                final int indeks = subsList.getSelectionIndex();
                if (indeks == -1) return;
                final Shell changeShell = new Shell(comp.getDisplay(), SWT.DIALOG_TRIM);
                changeShell.setText("Edit tags: " + subsList.getItem(indeks).getText());
                RowLayout rowLayout = new RowLayout(SWT.VERTICAL);
                rowLayout.pack = true;
                rowLayout.justify = true;
                rowLayout.marginWidth = 40;
                rowLayout.center = true;
                rowLayout.spacing = 10;
                changeShell.setLayout(rowLayout);
                changeShell.setLocation(300, 300);
                new Label(changeShell, SWT.NONE).setText("Enter tags: ");
                final Text tags = new Text(changeShell, SWT.BORDER);
                tags.setText(JReader.getChannel(indeks).getTagsAsString());
                Button okBut = new Button(changeShell, SWT.PUSH);
                okBut.setText("OK");
                okBut.addListener(SWT.Selection, new Listener() {

                    public void handleEvent(Event event) {
                        JReader.editTags(JReader.getChannel(indeks), tags.getText());
                        TagList.refresh();
                        changeShell.close();
                    }
                });
                tags.addListener(SWT.DefaultSelection, new Listener() {

                    public void handleEvent(Event e) {
                        JReader.editTags(JReader.getChannel(indeks), tags.getText());
                        TagList.refresh();
                        changeShell.close();
                    }
                });
                changeShell.pack();
                changeShell.open();
                TagList.refresh();
            }
