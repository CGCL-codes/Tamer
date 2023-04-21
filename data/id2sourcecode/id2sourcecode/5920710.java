    private boolean generateInfoFile(ImagePlus ip, File aviFile, int nFirstIdx, int nLastIdx) {
        if (nFirstIdx > nLastIdx || nLastIdx > ip.getStackSize()) throw new IllegalArgumentException("Start idx: " + nFirstIdx + ", End idx: " + nLastIdx + ", Stack size: " + ip.getStackSize());
        String parentPath = aviFile.getParentFile().getAbsolutePath();
        String infoFileName = aviFile.getName().replace(".avi", ".info");
        File infoFile = new File(parentPath + File.separator + infoFileName);
        try {
            if (infoFile.exists()) {
                int ans = JOptionPane.showConfirmDialog(m_dlgAvi, "The file already exists, do you want to overwrite?", "", JOptionPane.YES_NO_OPTION);
                if (ans == JOptionPane.NO_OPTION) return false;
                infoFile.delete();
            }
            infoFile.createNewFile();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(m_dlgAvi, "Couldn't create the info file: " + infoFile.getName(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        }
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(infoFile));
            String nl = System.getProperty("line.separator");
            out.write((nLastIdx - nFirstIdx + 1) + nl);
            out.write(nl);
            ImageStack stack = ip.getStack();
            for (int i = nFirstIdx; i <= nLastIdx; i++) {
                out.write(stack.getSliceLabel(i) + nl);
            }
            out.flush();
            out.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(m_dlgAvi, "Couldn't write the info file: " + infoFile.getName(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        }
        return true;
    }
