    public static void main(String[] args) throws IOException {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        try {
            lockFile = new File(System.getProperty("user.home") + File.separator + "puggle.lock");
            if (lockFile.exists()) {
                lockFile.delete();
            }
            channel = new RandomAccessFile(lockFile, "rw").getChannel();
            lock = channel.tryLock();
            if (lock == null) {
                channel.close();
                JOptionPane.showMessageDialog(null, "Another instance of Puggle is already running.", "Puggle", JOptionPane.ERROR_MESSAGE, ImageControl.getImageControl().getErrorIcon());
                return;
            }
            ShutdownHook shutdownHook = new ShutdownHook();
            Runtime.getRuntime().addShutdownHook(shutdownHook);
        } catch (IOException e) {
            throw new RuntimeException("Could not start process.", e);
        }
        IndexProperties props = null;
        if (Resources.isPortableEdition() == true) {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.indexOf("windows") < 0) {
                JOptionPane.showMessageDialog(null, "Sorry, portable edition is currently running\n" + "only in MS Windows operating systems.", "Puggle Portable", JOptionPane.ERROR_MESSAGE, ImageControl.getImageControl().getErrorIcon());
                return;
            }
            File root = new File(System.getProperty("user.dir"));
            while (root.getParentFile() != null) {
                root = root.getParentFile();
            }
            File index = new File(root.getPath() + ".puggle");
            Resources.setIndex(index);
            File propsFile = new File(Resources.getApplicationPropertiesCanonicalPath());
            props = new IndexProperties(propsFile);
            if (props.getVersion().equals(Resources.getApplicationVersion()) == false) {
                props.close();
                props = null;
                System.gc();
                File f = new File(Resources.getIndexCanonicalPath());
                if (Util.deleteDir(f) == false) {
                    JOptionPane.showMessageDialog(null, "Cannot delete directory '" + f + "'.\n" + "Please remove it manually and start application again.", "Error Removing Old Index Directory", JOptionPane.ERROR_MESSAGE, ImageControl.getImageControl().getErrorIcon());
                    System.exit(1);
                }
                props = new IndexProperties(propsFile);
            }
            if (IndexReader.indexExists(Resources.getIndexCanonicalPath()) == false) {
                props.setPortable(true);
                props.setFilesystemRoot(root.getAbsolutePath());
                props.setPath(root.getPath());
                Indexer indexer = new Indexer(index, props);
                try {
                    indexer.close();
                } catch (Throwable ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE, ImageControl.getImageControl().getErrorIcon());
                    System.exit(1);
                }
                IndexPropertiesDialog dialog = new IndexPropertiesDialog((java.awt.Frame) null, true);
                dialog.setProperties(props);
                dialog.setLocationRelativeTo(null);
                dialog.setVisible(true);
            }
        } else {
            Resources.makeApplicationDirectoryTree();
            File index = new File(Resources.getApplicationDirectoryCanonicalPath());
            File propsFile = new File(Resources.getApplicationPropertiesCanonicalPath());
            props = new IndexProperties(propsFile);
            if (props.getVersion().equals(Resources.getApplicationVersion()) == false) {
                props.close();
                props = null;
                System.gc();
                File f = new File(Resources.getApplicationDirectoryCanonicalPath());
                if (Util.deleteDir(f) == false) {
                    JOptionPane.showMessageDialog(null, "Cannot delete directory '" + f + "'.\n" + "Please remove it manually and start application again.", "Error Removing Old Index Directory", JOptionPane.ERROR_MESSAGE, ImageControl.getImageControl().getErrorIcon());
                    System.exit(1);
                }
                props = new IndexProperties(propsFile);
            }
            if (IndexReader.indexExists(Resources.getIndexCanonicalPath()) == false) {
                props.setPath(System.getProperty("user.home"));
                Indexer indexer = new Indexer(index, props);
                try {
                    indexer.close();
                } catch (Throwable ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE, ImageControl.getImageControl().getErrorIcon());
                    System.exit(1);
                }
                IndexPropertiesDialog dialog = new IndexPropertiesDialog((java.awt.Frame) null, true);
                dialog.setProperties(props);
                dialog.setLocationRelativeTo(null);
                dialog.setVisible(true);
            }
        }
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                searchFrame = new SearchFrame();
                searchFrame.setLocationRelativeTo(null);
                searchFrame.setVisible(true);
            }
        });
        Updater update = new Updater();
        try {
            if (update.isUpdated() == false) {
                JOptionPane.showMessageDialog(null, "<html>" + "The Puggle version that is currently running appears to be old. <br/><br/>" + "You can update Puggle manually by visiting this link and <br/>" + "downloading the latest version:<p align=\"center\">" + "<a href=\"" + Resources.getApplicationWebsite() + "\">" + Resources.getApplicationWebsite() + "</p></a><br/></html>", "Update found", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
        }
    }
