            public void actionPerformed(ActionEvent e) {
                new AddChannelDialog(getMainframe(), name, URL).open();
                log.config("Command: Add Channel " + name.getValue() + ", at " + URL.getValue());
                if (name.getValue() != null && URL.getValue() != null) {
                    cmdAddPersistentChannel(GlobalModel.SINGLETON.getChannelGuideSet().selectedGuide(), GlobalModel.SINGLETON.getChannelGuideSet().selectedGuide().selectedCGE(), (String) URL.getValue(), (String) name.getValue());
                }
                ;
            }
