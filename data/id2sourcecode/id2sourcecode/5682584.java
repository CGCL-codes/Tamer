    public static final void main(String[] args) {
        TerragenSkyboxDownfiller filler = new TerragenSkyboxDownfiller(4);
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Choose images to fill");
        chooser.setMultiSelectionEnabled(true);
        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            for (File f : chooser.getSelectedFiles()) {
                File outFile = new File(f.getParent(), "d_" + f.getName());
                try {
                    BufferedImage read = ImageIO.read(f);
                    filler.downfill(read);
                    System.out.println(ImageIO.write(read, "bmp", outFile));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
