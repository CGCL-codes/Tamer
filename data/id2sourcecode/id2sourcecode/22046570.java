    public void loadStream(Readable is) {
        try {
            byte[] tmp = new byte[65536];
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int bytesRead;
            while ((bytesRead = is.read(tmp)) > 0) baos.write(tmp, 0, bytesRead);
            fileData = baos.toByteArray();
            baos = null;
            refreshView();
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    int vMin = textPaneScroller.getVerticalScrollBar().getMinimum();
                    int hMin = textPaneScroller.getHorizontalScrollBar().getMinimum();
                    textPaneScroller.getVerticalScrollBar().setValue(vMin);
                    textPaneScroller.getHorizontalScrollBar().setValue(hMin);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Exception while loading file:\n  " + e + "\"");
        }
    }
