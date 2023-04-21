            public void widgetSelected(SelectionEvent e) {
                int indeks = subsList.getSelectionIndex();
                if (indeks == -1) return;
                JReader.markChannelAsRead(JReader.getChannel(indeks));
                SubsList.refresh();
                ItemsTable.refresh();
                Filters.refresh();
            }
