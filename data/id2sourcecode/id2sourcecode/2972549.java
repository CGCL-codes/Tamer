                    public void handleEvent(Event event) {
                        JReader.editTags(JReader.getChannel(indeks), tags.getText());
                        TagList.refresh();
                        changeShell.close();
                    }
