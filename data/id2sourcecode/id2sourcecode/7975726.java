    public MpegDialog(JFrame parent, String title, MpegInfo mi) {
        super(parent, title);
        initComponents();
        _mpeginfo = mi;
        int size = _mpeginfo.getLocation().length();
        locationLabel.setText(size > 50 ? ("..." + _mpeginfo.getLocation().substring(size - 50)) : _mpeginfo.getLocation());
        if ((_mpeginfo.getTitle() != null) && ((!_mpeginfo.getTitle().equals("")))) textField.append("Title=" + _mpeginfo.getTitle() + "\n");
        if ((_mpeginfo.getArtist() != null) && ((!_mpeginfo.getArtist().equals("")))) textField.append("Artist=" + _mpeginfo.getArtist() + "\n");
        if ((_mpeginfo.getAlbum() != null) && ((!_mpeginfo.getAlbum().equals("")))) textField.append("Album=" + _mpeginfo.getAlbum() + "\n");
        if (_mpeginfo.getTrack() > 0) textField.append("Track=" + _mpeginfo.getTrack() + "\n");
        if ((_mpeginfo.getYear() != null) && ((!_mpeginfo.getYear().equals("")))) textField.append("Year=" + _mpeginfo.getYear() + "\n");
        if ((_mpeginfo.getGenre() != null) && ((!_mpeginfo.getGenre().equals("")))) textField.append("Genre=" + _mpeginfo.getGenre() + "\n");
        java.util.List comments = _mpeginfo.getComment();
        if (comments != null) {
            for (int i = 0; i < comments.size(); i++) textField.append(comments.get(i) + "\n");
        }
        int secondsAmount = Math.round(_mpeginfo.getPlayTime());
        if (secondsAmount < 0) secondsAmount = 0;
        int minutes = secondsAmount / 60;
        int seconds = secondsAmount - (minutes * 60);
        lengthLabel.setText("Length : " + minutes + ":" + seconds);
        DecimalFormat df = new DecimalFormat("#,###,###");
        sizeLabel.setText("Size : " + df.format(_mpeginfo.getSize()) + " bytes");
        versionLabel.setText(_mpeginfo.getVersion() + " " + _mpeginfo.getLayer());
        bitrateLabel.setText((_mpeginfo.getBitRate() / 1000) + " kbps");
        samplerateLabel.setText(_mpeginfo.getSamplingRate() + " Hz " + _mpeginfo.getChannelsMode());
        vbrLabel.setText("VBR : " + _mpeginfo.getVBR());
        crcLabel.setText("CRCs : " + _mpeginfo.getCRC());
        copyrightLabel.setText("Copyrighted : " + _mpeginfo.getCopyright());
        originalLabel.setText("Original : " + _mpeginfo.getOriginal());
        emphasisLabel.setText("Emphasis : " + _mpeginfo.getEmphasis());
        buttonsPanel.add(_close);
        pack();
    }
