            public void widgetSelected(SelectionEvent e) {
                int indeks = subsList.getSelectionIndex();
                if (indeks == -1) return;
                try {
                    JReader.updateChannel(JReader.getChannel(indeks));
                    JReader.getChannel(indeks).setFail(false);
                    GUI.statusLine.setText("Channel has been updated.");
                } catch (SAXParseException spe) {
                    GUI.statusLine.setText("Failed to update channel.");
                    errorDialog("Failed to update channel.\n" + "Source is not a valid XML.\n" + "Error in line " + spe.getLineNumber() + ". " + "Details:\n" + spe.getLocalizedMessage());
                    JReader.getChannel(indeks).setFail(true);
                } catch (SAXException saxe) {
                    GUI.statusLine.setText("Failed to update channel.");
                    errorDialog("Failed to update channel.\n" + "XML parser error has occured.");
                    JReader.getChannel(indeks).setFail(true);
                } catch (SocketException se) {
                    GUI.statusLine.setText("Failed to update channel.");
                    errorDialog("Failed to update channel.\n" + se.getLocalizedMessage());
                    JReader.getChannel(indeks).setFail(true);
                } catch (IOException ioe) {
                    GUI.statusLine.setText("Failed to update channel.");
                    errorDialog("Failed to update channel.\n" + "Unable to connect to the site.");
                    JReader.getChannel(indeks).setFail(true);
                }
                SubsList.refresh();
                ItemsTable.refresh();
                Filters.refresh();
            }
