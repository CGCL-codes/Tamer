    public void actionPerformed(ActionEvent event) {
        if (event.getActionCommand().equals("Load")) {
            JFileChooser fc = new JFileChooser();
            int returnVal = fc.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                try {
                    File file = fc.getSelectedFile();
                    System.out.println("Loading file: " + file);
                    EpgBinaryMarshaller marshaller = new EpgBinaryMarshaller();
                    FileChannel channel = new FileInputStream(file).getChannel();
                    ByteBuffer buf = ByteBuffer.allocate((int) channel.size());
                    channel.read(buf);
                    System.out.println("Unmarshalling data");
                    epg = marshaller.unmarshall(buf.array());
                    System.out.println("Finished");
                    viewer.refresh(epg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (event.getActionCommand().equals("Save")) {
        } else if (event.getActionCommand().equals("Exit")) {
            this.dispose();
        } else if (event.getActionCommand().equals("About")) {
            JDialog dialog = new JDialog(this, "About EPG Studio", true);
            dialog.setSize(400, 250);
            AboutPanel panel = new AboutPanel();
            dialog.add(panel);
            dialog.setVisible(true);
        }
    }
