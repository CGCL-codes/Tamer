            public void onProgramme(TVProgramme programme) {
                int row = currentChannelSet.getChannelIndex(getCurrentChannel().getID());
                if (row != -1) {
                    panel.getProgrammesPanel().addProgramme(programme, row);
                }
            }
