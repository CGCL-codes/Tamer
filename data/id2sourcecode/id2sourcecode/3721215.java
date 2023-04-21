    public static void main(String args[]) throws IOException, CloneNotSupportedException, Exception {
        PEFile pe = new PEFile(new File("F:/Documents and Settings/Rodrigo/Mes documents/projects/jsmooth/skeletons/simplewrap/JWrap.exe"));
        pe.open();
        File fout = new File("F:/Documents and Settings/Rodrigo/Mes documents/projects/jsmooth/skeletons/simplewrap/gen-application.jar");
        FileInputStream fis = new FileInputStream(fout);
        ByteBuffer data = ByteBuffer.allocate((int) fout.length());
        data.order(ByteOrder.LITTLE_ENDIAN);
        FileChannel fischan = fis.getChannel();
        fischan.read(data);
        data.position(0);
        fis.close();
        PEResourceDirectory resdir = pe.getResourceDirectory();
        java.awt.Image img = java.awt.Toolkit.getDefaultToolkit().getImage("c:\\gnome-color-browser2.png");
        java.awt.MediaTracker mt = new java.awt.MediaTracker(new javax.swing.JLabel("toto"));
        mt.addImage(img, 1);
        try {
            mt.waitForAll();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        ResIcon newicon = new ResIcon(img);
        pe.replaceDefaultIcon(newicon);
        File out = new File("F:/Documents and Settings/Rodrigo/Mes documents/projects/jsmooth/skeletons/simplewrap/COPIE.exe");
        pe.dumpTo(out);
    }
