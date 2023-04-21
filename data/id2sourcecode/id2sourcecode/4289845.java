            @Override
            public void widgetSelected(SelectionEvent e) {
                TestVoiceDialog.show(shell);
                if (getStore().getChannel() == null) {
                    return;
                }
                getClassMemberPane().setAddOnImage(Images.testvoice16);
            }
