    private void doSaveOgg() throws Exception {
        RandomAccessFile raf = null, temp = null;
        File f = null;
        try {
            raf = new RandomAccessFile(item.getLocation(), "rw");
            f = File.createTempFile("temp", "fix");
            log.log(Level.INFO, "临时文件是:" + f.getPath());
            temp = new RandomAccessFile(f, "rw");
            OggVorbisTagWriter w = new OggVorbisTagWriter();
            VorbisCommentTag vt = new VorbisCommentTag();
            vt.setAlbum(album.getText());
            vt.setArtist(artist.getText());
            vt.setComment(comment.getText());
            vt.setGenre(genre.getText());
            vt.setTitle(title.getText());
            vt.setTrack(track.getText());
            vt.setYear(year.getText());
            vt.setVendor("hadeslee");
            w.write(vt, raf, temp);
            temp.seek(0);
            raf.setLength(temp.length());
            raf.seek(0);
            raf.getChannel().transferFrom(temp.getChannel(), 0, temp.length());
            log.log(Level.INFO, "完毕");
        } finally {
            if (raf != null) {
                raf.close();
            }
            if (temp != null) {
                temp.close();
            }
            if (f != null) {
                f.delete();
            }
        }
    }
