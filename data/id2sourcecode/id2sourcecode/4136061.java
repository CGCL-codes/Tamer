    public APEDialog(JFrame parent, String title, APEInfo mi) {
        super(parent, title);
        initComponents();
        _apeinfo = mi;
        int size = _apeinfo.getLocation().length();
        locationLabel.setText(size > 50 ? ("..." + _apeinfo.getLocation().substring(size - 50)) : _apeinfo.getLocation());
        if ((_apeinfo.getTitle() != null) && (!_apeinfo.getTitle().equals(""))) textField.append("Title=" + _apeinfo.getTitle() + "\n");
        if ((_apeinfo.getArtist() != null) && (!_apeinfo.getArtist().equals(""))) textField.append("Artist=" + _apeinfo.getArtist() + "\n");
        if ((_apeinfo.getAlbum() != null) && (!_apeinfo.getAlbum().equals(""))) textField.append("Album=" + _apeinfo.getAlbum() + "\n");
        if (_apeinfo.getTrack() > 0) textField.append("Track=" + _apeinfo.getTrack() + "\n");
        if ((_apeinfo.getYear() != null) && (!_apeinfo.getYear().equals(""))) textField.append("Year=" + _apeinfo.getYear() + "\n");
        if ((_apeinfo.getGenre() != null) && (!_apeinfo.getGenre().equals(""))) textField.append("Genre=" + _apeinfo.getGenre() + "\n");
        java.util.List comments = _apeinfo.getComment();
        if (comments != null) {
            for (int i = 0; i < comments.size(); i++) textField.append(comments.get(i) + "\n");
        }
        int secondsAmount = Math.round(_apeinfo.getPlayTime());
        if (secondsAmount < 0) secondsAmount = 0;
        int minutes = secondsAmount / 60;
        int seconds = secondsAmount - (minutes * 60);
        lengthLabel.setText("Length : " + minutes + ":" + seconds);
        DecimalFormat df = new DecimalFormat("#,###,###");
        sizeLabel.setText("Size : " + df.format(_apeinfo.getSize()) + " bytes");
        versionLabel.setText("Version: " + df.format(_apeinfo.getVersion()));
        compressionLabel.setText("Compression: " + _apeinfo.getCompressionlevel());
        channelsLabel.setText("Channels: " + _apeinfo.getChannels());
        bitspersampleLabel.setText("Bits Per Sample: " + _apeinfo.getBitsPerSample());
        bitrateLabel.setText("Average Bitrate: " + (_apeinfo.getBitRate() / 1000) + " kbps");
        samplerateLabel.setText("Sample Rate: " + _apeinfo.getSamplingRate() + " Hz");
        peaklevelLabel.setText("Peak Level: " + (_apeinfo.getPeaklevel() > 0 ? String.valueOf(_apeinfo.getPeaklevel()) : ""));
        copyrightLabel.setText("Copyrighted: " + (_apeinfo.getCopyright() != null ? _apeinfo.getCopyright() : ""));
        buttonsPanel.add(_close);
        pack();
    }
