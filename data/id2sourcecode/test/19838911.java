            @Override
            public void widgetSelected(SelectionEvent e) {
                ChannelModeForm cmf = new ChannelModeForm(getShell(), SWT.NONE, getChannel());
                cmf.open();
                super.widgetSelected(e);
            }
