    protected boolean setDocument(Component parent, Source source, Entry entry) {
        URL url = null;
        if (source == Source.FILE) {
            JFileChooser chooser = new JFileChooser(System.getProperty("user.dir"));
            chooser.setDialogTitle(peer.getString("filerepository", "FileRepository.FileChooser.caption"));
            chooser.addChoosableFileFilter(new DocumentFilter());
            int returnVal = chooser.showDialog(parent, peer.getString("filerepository", "FileRepository.FileChooser.approve"));
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                if (file != null) {
                    try {
                        url = file.toURL();
                    } catch (MalformedURLException ex) {
                    }
                }
            }
        } else if (source == Source.URL) {
            String urlString = JOptionPane.showInputDialog(parent, peer.getString("filerepository", "FileRepository.URLChooser.message") + ":", peer.getString("filerepository", "FileRepository.SetFromURL.caption"), JOptionPane.QUESTION_MESSAGE);
            if (urlString != null) {
                try {
                    url = new URL(urlString);
                } catch (MalformedURLException ex) {
                }
            }
        }
        if (url == null) {
            updateActions(entry);
            return false;
        }
        try {
            URLConnection con = url.openConnection();
            con.connect();
            String ext = getExtensionForMimetype(con.getContentType());
            discard(entry);
            File file = new File(inboundPath + makeFilename(entry) + "." + ext);
            java.io.OutputStream out = new java.io.FileOutputStream(file);
            byte[] buf = new byte[4096];
            InputStream in = con.getInputStream();
            int len;
            do {
                len = in.read(buf);
                if (len != -1) {
                    out.write(buf, 0, len);
                }
            } while (len != -1);
            con = null;
            System.gc();
            out.close();
            updateActions(entry);
            return true;
        } catch (IOException ex) {
            updateActions(entry);
            return false;
        }
    }
