    public static void checkUpdateAvailable(JP1Frame frame) throws IOException {
        URL url = new URL("http://controlremote.sourceforge.net/version.dat");
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String latestVersion = in.readLine();
        in.close();
        String text = null;
        if (RemoteMaster.version.compareTo(latestVersion) >= 0) {
            text = "You are using the latest version (" + RemoteMaster.version + ") of RemoteMaster.";
        } else {
            text = "<html>Version " + latestVersion + " of RemoteMaster is available, but you are still using version " + RemoteMaster.version + "<p>The new version is available for download from<br><a href=\"http://prdownloads.sourceforge.net/controlremote/RemoteMaster." + latestVersion + ".zip?download\">" + "http://prdownloads.sourceforge.net/controlremote/RemoteMaster." + latestVersion + ".zip?download</a></html>";
        }
        JEditorPane pane = new JEditorPane("text/html", text);
        pane.setEditable(false);
        pane.setBackground(frame.getContentPane().getBackground());
        new TextPopupMenu(pane);
        JOptionPane.showMessageDialog(frame, pane, "RemoteMaster Version Check", JOptionPane.INFORMATION_MESSAGE);
    }
