            public void run() {
                boolean error = false;
                try {
                    exportDataThread(rbnbServer, channels, startTime, endTime, file, writer, progressWindow);
                } catch (SAPIException e) {
                    error = true;
                    e.printStackTrace();
                } catch (IOException e) {
                    error = true;
                    e.printStackTrace();
                }
                progressWindow.dispose();
                if (!error) {
                    JOptionPane.showMessageDialog(null, "Export complete.", "Export complete", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "There was an error export the data to file.", "Export failed", JOptionPane.ERROR_MESSAGE);
                }
            }
