    public void copyFile(String from, String to) {
        try {
            FileChannel from2 = new RandomAccessFile(from, "r").getChannel();
            FileChannel to2 = new RandomAccessFile(to, "rwd").getChannel();
            from2.transferTo(0, from2.size(), to2);
            from2.close();
            to2.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro copyFile(): " + e.toString());
        }
    }
