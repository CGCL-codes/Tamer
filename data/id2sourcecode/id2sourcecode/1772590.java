            public void actionPerformed(ActionEvent e) {
                new AddChannelDialog(getMainframe(), name, URL).open();
                log.config("Command: Add Channel " + name.getValue() + ", at " + URL.getValue());
                if (name.getValue() != null && URL.getValue() != null) {
                    cmdSimpleAddChannel(GlobalModel.SINGLETON.getChannelGuideSet().selectedGuide(), null, (String) URL.getValue(), (String) name.getValue());
                }
                ;
            }
