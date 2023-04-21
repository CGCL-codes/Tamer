                    public void handleEvent(Event e) {
                        JReader.editTags(JReader.getChannel(indeks), tags.getText());
                        TagList.refresh();
                        changeShell.close();
                    }
