    public void pack() {
        try {
            String s = (String) JOptionPane.showInputDialog(null, "Enter Path + Filename to save, Make sure theres no existing file in the same directory.", "Landscape Editor", JOptionPane.PLAIN_MESSAGE, null, null, "C:/" + ourFile);
            for (int i = 0; i < 48 * 48; i++) {
                out = tiles[i].pack();
                File file = new File(s);
                FileChannel wChannel = new FileOutputStream(file, true).getChannel();
                wChannel.write(out);
                wChannel.close();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
