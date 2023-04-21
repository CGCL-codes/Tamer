    public static int setMediaFile(MediaFile f) {
        int fileid = 0;
        try {
            Connection con = DatabaseService.getConnection();
            PreparedStatement stmt_file = con.prepareStatement("insert into files(filename, path, size, stream_count, container_type,title,author,copyright,comment,metaalbum,metayear,metatrack,metagenre,duration,bitrate,insertdate,filetype,parent) " + "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            PreparedStatement stmt_stream = con.prepareStatement("insert into streams (fileid,stream_index, stream_type,codec, codec_name,framerate_num, framerate_den,start_time, first_dts,duration,nb_frames,time_base_num, time_base_den,codec_time_base_num,codec_time_base_den,ticks_per_frame, width, height, gop_size, pix_fmt,bit_rate, rate_emu, sample_rate, channels, sample_fmt, bits_per_coded_sample, priv_data_size, priv_data, extra_data_size,extra_data, extra_profile_flags, framecount, flags) values" + "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            File file = new File(f.getPath() + File.separator + f.getFilename());
            stmt_file.setString(1, file.getName());
            stmt_file.setString(2, file.getParent());
            stmt_file.setLong(3, f.getSize());
            stmt_file.setLong(4, f.getStreamCount());
            stmt_file.setString(5, f.getContainerType());
            stmt_file.setString(6, f.getTitle());
            stmt_file.setString(7, f.getAuthor());
            stmt_file.setString(8, f.getCopyright());
            stmt_file.setString(9, f.getComment());
            stmt_file.setString(10, f.getAlbum());
            stmt_file.setInt(11, f.getYear());
            stmt_file.setInt(12, f.getTrack());
            stmt_file.setInt(13, f.getGenre());
            stmt_file.setLong(14, f.getDuration());
            stmt_file.setLong(15, f.getBitrate());
            stmt_file.setDate(16, new Date(f.getInsertDate().getTime()));
            stmt_file.setLong(17, f.getFileType());
            if (f.getParent() != null) stmt_file.setLong(18, f.getParent().getId()); else stmt_file.setLong(18, 0L);
            stmt_file.execute();
            ResultSet gen = stmt_file.getGeneratedKeys();
            if (gen != null && gen.next()) fileid = gen.getInt(1);
            f.setId(fileid);
            for (MediaStream s : f.getStreams()) {
                stmt_stream.setInt(1, fileid);
                stmt_stream.setInt(2, s.getStreamIndex());
                stmt_stream.setInt(3, s.getCodecType());
                stmt_stream.setInt(4, s.getCodecId());
                stmt_stream.setString(5, s.getCodecName());
                stmt_stream.setInt(6, s.getFrameRateNum());
                stmt_stream.setInt(7, s.getFrameRateDen());
                stmt_stream.setLong(8, s.getStartTime());
                stmt_stream.setLong(9, s.getFirstDts());
                stmt_stream.setLong(10, s.getDuration());
                stmt_stream.setLong(11, s.getNumFrames());
                stmt_stream.setLong(12, s.getTimeBaseNum());
                stmt_stream.setLong(13, s.getTimeBaseDen());
                stmt_stream.setLong(14, s.getCodecTimeBaseNum());
                stmt_stream.setLong(15, s.getCodecTimeBaseDen());
                stmt_stream.setLong(16, s.getTicksPerFrame());
                stmt_stream.setLong(17, s.getWidth());
                stmt_stream.setLong(18, s.getHeight());
                stmt_stream.setLong(19, s.getGopSize());
                stmt_stream.setLong(20, s.getPixelFormat());
                stmt_stream.setLong(21, s.getBitrate());
                stmt_stream.setLong(22, 0L);
                stmt_stream.setLong(23, s.getSampleRate());
                stmt_stream.setLong(24, s.getChannels());
                stmt_stream.setLong(25, s.getSampleFormat());
                stmt_stream.setLong(26, s.getBitsPerCodedSample());
                stmt_stream.setLong(27, 0);
                stmt_stream.setNull(28, Types.BLOB);
                stmt_stream.setLong(29, s.getExtraDataSize());
                stmt_stream.setBytes(30, s.getExtraData());
                stmt_stream.setString(31, s.getExtraCodecFlags());
                stmt_stream.setInt(32, s.getFrameCount());
                stmt_stream.setInt(33, s.getFlags());
                stmt_stream.execute();
                ResultSet gens = stmt_stream.getGeneratedKeys();
                if (gens != null && gens.next()) s.setId(gens.getInt(1));
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
        return fileid;
    }
