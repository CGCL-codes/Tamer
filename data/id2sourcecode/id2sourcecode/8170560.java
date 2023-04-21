    public FlacDialog(JFrame parent, String title, FlacInfo mi) {
        super(parent, title);
        initComponents();
        _flacinfo = mi;
        int size = _flacinfo.getLocation().length();
        locationLabel.setText(size > 50 ? ("..." + _flacinfo.getLocation().substring(size - 50)) : _flacinfo.getLocation());
        if ((_flacinfo.getTitle() != null) && (!_flacinfo.getTitle().equals(""))) textField.append("Title=" + _flacinfo.getTitle() + "\n");
        if ((_flacinfo.getArtist() != null) && (!_flacinfo.getArtist().equals(""))) textField.append("Artist=" + _flacinfo.getArtist() + "\n");
        if ((_flacinfo.getAlbum() != null) && (!_flacinfo.getAlbum().equals(""))) textField.append("Album=" + _flacinfo.getAlbum() + "\n");
        if (_flacinfo.getTrack() > 0) textField.append("Track=" + _flacinfo.getTrack() + "\n");
        if ((_flacinfo.getYear() != null) && (!_flacinfo.getYear().equals(""))) textField.append("Year=" + _flacinfo.getYear() + "\n");
        if ((_flacinfo.getGenre() != null) && (!_flacinfo.getGenre().equals(""))) textField.append("Genre=" + _flacinfo.getGenre() + "\n");
        java.util.List comments = _flacinfo.getComment();
        if (comments != null) {
            for (int i = 0; i < comments.size(); i++) textField.append(comments.get(i) + "\n");
        }
        DecimalFormat df = new DecimalFormat("#,###,###");
        sizeLabel.setText("Size : " + df.format(_flacinfo.getSize()) + " bytes");
        channelsLabel.setText("Channels: " + _flacinfo.getChannels());
        bitspersampleLabel.setText("Bits Per Sample: " + _flacinfo.getBitsPerSample());
        samplerateLabel.setText("Sample Rate: " + _flacinfo.getSamplingRate() + " Hz");
        buttonsPanel.add(_close);
        pack();
    }
