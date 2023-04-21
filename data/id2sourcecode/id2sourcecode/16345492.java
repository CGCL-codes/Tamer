    public void computeNewFile2(File file) {
        if (_files.contains(file.getName() + file.getParent())) {
            return;
        }
        _files.add(file.getName() + file.getParent());
        _filesScanned++;
        if (container.open(file.getAbsoluteFile().toString(), IContainer.Type.READ, null) < 0) _log.error("could not open file: " + file.getAbsoluteFile()); else try {
            stmt_file.setString(1, file.getName());
            stmt_file.setString(2, file.getParent());
            stmt_file.setLong(3, container.getFileSize());
            stmt_file.setLong(4, container.getNumStreams());
            stmt_file.setString(5, container.getContainerFormat().getInputFormatShortName());
            stmt_file.setString(6, container.getMetaData().getValue("title"));
            stmt_file.setString(7, container.getMetaData().getValue("author"));
            stmt_file.setString(8, container.getMetaData().getValue("copyright"));
            stmt_file.setString(9, container.getMetaData().getValue("comment"));
            stmt_file.setString(10, container.getMetaData().getValue("album"));
            stmt_file.setString(11, container.getMetaData().getValue("year"));
            stmt_file.setString(12, container.getMetaData().getValue("track"));
            stmt_file.setString(13, container.getMetaData().getValue("genre"));
            stmt_file.setLong(14, container.getDuration());
            stmt_file.setLong(15, container.getBitRate());
            stmt_file.setDate(16, new Date(new java.util.Date().getTime()));
            stmt_file.setLong(17, 0L);
            stmt_file.setLong(18, 0L);
            stmt_file.execute();
            ResultSet gen = stmt_file.getGeneratedKeys();
            int fileid = 0;
            if (gen != null && gen.next()) fileid = gen.getInt(1);
            int nb_streams = container.getNumStreams();
            for (int a = 0; a < nb_streams; a++) {
                IStream stream = container.getStream(a);
                IStreamCoder codec = stream.getStreamCoder();
                stmt_stream.setInt(1, fileid);
                stmt_stream.setInt(2, stream.getIndex());
                stmt_stream.setInt(3, codec.getCodecType().swigValue());
                stmt_stream.setInt(4, codec.getCodecID().swigValue());
                stmt_stream.setString(5, "implementd later");
                stmt_stream.setInt(6, stream.getFrameRate().getNumerator());
                stmt_stream.setInt(7, stream.getFrameRate().getDenominator());
                stmt_stream.setLong(8, stream.getStartTime());
                stmt_stream.setLong(9, stream.getFirstDts());
                stmt_stream.setLong(10, stream.getDuration());
                stmt_stream.setLong(11, stream.getNumFrames());
                stmt_stream.setLong(12, stream.getTimeBase().getNumerator());
                stmt_stream.setLong(13, stream.getTimeBase().getDenominator());
                stmt_stream.setLong(14, codec.getTimeBase().getNumerator());
                stmt_stream.setLong(15, codec.getTimeBase().getDenominator());
                stmt_stream.setLong(16, codec.getPropertyAsLong("ticks_per_frame"));
                stmt_stream.setLong(17, codec.getWidth());
                stmt_stream.setLong(18, codec.getHeight());
                stmt_stream.setLong(19, codec.getNumPicturesInGroupOfPictures());
                stmt_stream.setLong(20, codec.getPixelType().swigValue());
                stmt_stream.setLong(21, codec.getBitRate());
                stmt_stream.setLong(22, 0L);
                stmt_stream.setLong(23, codec.getSampleRate());
                stmt_stream.setLong(24, codec.getChannels());
                stmt_stream.setLong(25, codec.getSampleFormat().swigValue());
                stmt_stream.setLong(26, codec.getPropertyAsLong("bits_per_coded_sample"));
                stmt_stream.setLong(27, 0);
                stmt_stream.setNull(28, Types.BLOB);
                stmt_stream.setLong(29, codec.getExtraDataSize());
                stmt_stream.setNull(30, Types.BLOB);
                stmt_stream.execute();
            }
            _filesImported++;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
        container.close();
    }
