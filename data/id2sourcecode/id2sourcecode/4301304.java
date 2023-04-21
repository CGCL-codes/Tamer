    public void showDetails(Object object) {
        details = object;
        Cursor oldCursor = getCursor();
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            JComponent detailPane = new JPanel();
            if (object instanceof Box) {
                detailPane = new GenericBoxPane((Box) object);
            }
            detailPanel.removeAll();
            detailPanel.add(detailPane, BorderLayout.CENTER);
            detailPanel.revalidate();
            ByteBuffer displayMe;
            if (object instanceof Box && !(object instanceof IsoFile)) {
                displayMe = ByteBuffer.allocate(l2i(((Box) object).getSize()));
                try {
                    ((Box) object).getBox(new ByteBufferByteChannel(displayMe));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else if (object instanceof IsoFile) {
                FileChannel fc = new FileInputStream(this.file).getChannel();
                displayMe = fc.map(FileChannel.MapMode.READ_ONLY, 0, Math.min(this.file.length(), Integer.MAX_VALUE));
                fc.close();
            } else {
                displayMe = ByteBuffer.allocate(0);
            }
            rawDataSplitPane.setBottomComponent(new JHexEditor(displayMe));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            setCursor(oldCursor);
        }
    }
