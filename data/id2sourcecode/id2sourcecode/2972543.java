            public void handleEvent(Event e) {
                int indeks = subsList.getSelectionIndex();
                ItemsTable.refresh();
                if (Preview.folderPreview.getItemCount() != 0) {
                    JReader.selectChannel(indeks, Preview.folderPreview.getSelectionIndex());
                    Preview.previewItemList.get(Preview.folderPreview.getSelectionIndex()).refresh();
                } else {
                    JReader.addNewPreviewTab();
                    JReader.selectChannel(indeks, 0);
                    GUI.openTab(JReader.getChannel(indeks).getTitle()).refresh();
                }
            }
