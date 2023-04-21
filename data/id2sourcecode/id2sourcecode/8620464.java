    public void loadExecFromFile() {
        File file = chooseFileToLoad();
        if (file != null) {
            textArea.setText("");
            long file_size = file.length();
            int byteSize = (int) file_size;
            FileInputStream f = null;
            try {
                f = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(frame, "File " + file.getName() + " not found.", "File load error", JOptionPane.ERROR_MESSAGE);
            }
            FileChannel ch = f.getChannel();
            ByteBuffer bb = ByteBuffer.allocateDirect(131072);
            byte[] barray = new byte[byteSize];
            long checkSum = 0L;
            int nRead, nGet;
            try {
                while ((nRead = ch.read(bb)) != -1) {
                    if (nRead == 0) continue;
                    bb.position(0);
                    bb.limit(nRead);
                    while (bb.hasRemaining()) {
                        nGet = Math.min(bb.remaining(), byteSize);
                        bb.get(barray, 0, nGet);
                        for (int i = 0; i < nGet; i++) checkSum += barray[i];
                    }
                    textArea.setText(new String(barray));
                    textArea.setCaretPosition(0);
                    bb.clear();
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(frame, "Error in reading a file " + file.getName(), "File read error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
