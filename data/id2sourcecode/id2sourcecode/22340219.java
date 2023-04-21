    public TGSong readSong() throws TGFileFormatException {
        try {
            readVersion();
            if (!isSupportedVersion(getVersion())) {
                this.close();
                throw new GTPFormatException("Unsupported Version");
            }
            TGSong song = getFactory().newSong();
            readInfo(song);
            int tempo = readInt();
            int tripletFeel = ((readInt() == 1) ? TGMeasureHeader.TRIPLET_FEEL_EIGHTH : TGMeasureHeader.TRIPLET_FEEL_NONE);
            readInt();
            for (int i = 0; i < TRACK_COUNT; i++) {
                TGChannel channel = getFactory().newChannel();
                TGChannelParameter gmChannel1Param = getFactory().newChannelParameter();
                TGChannelParameter gmChannel2Param = getFactory().newChannelParameter();
                gmChannel1Param.setKey(GMChannelRoute.PARAMETER_GM_CHANNEL_1);
                gmChannel1Param.setValue(Integer.toString(TRACK_CHANNELS[i][1]));
                gmChannel2Param.setKey(GMChannelRoute.PARAMETER_GM_CHANNEL_2);
                gmChannel2Param.setValue(Integer.toString(TRACK_CHANNELS[i][2]));
                channel.setChannelId(TRACK_CHANNELS[i][0]);
                channel.addParameter(gmChannel1Param);
                channel.addParameter(gmChannel2Param);
                song.addChannel(channel);
            }
            for (int i = 0; i < TRACK_COUNT; i++) {
                TGTrack track = getFactory().newTrack();
                track.setNumber((i + 1));
                track.setChannelId(TRACK_CHANNELS[i][0]);
                TGColor.RED.copy(track.getColor());
                int strings = readInt();
                for (int j = 0; j < strings; j++) {
                    TGString string = getFactory().newString();
                    string.setNumber(j + 1);
                    string.setValue(readInt());
                    track.getStrings().add(string);
                }
                song.addTrack(track);
            }
            int measureCount = readInt();
            for (int i = 0; i < TRACK_COUNT; i++) {
                readTrack(song.getTrack(i), song.getChannel(i));
            }
            skip(10);
            TGMeasureHeader previous = null;
            long[] lastReadedStarts = new long[TRACK_COUNT];
            for (int i = 0; i < measureCount; i++) {
                TGMeasureHeader header = getFactory().newHeader();
                header.setStart((previous == null) ? TGDuration.QUARTER_TIME : (previous.getStart() + previous.getLength()));
                header.setNumber((previous == null) ? 1 : previous.getNumber() + 1);
                header.getTempo().setValue((previous == null) ? tempo : previous.getTempo().getValue());
                header.setTripletFeel(tripletFeel);
                readTrackMeasures(song, header, lastReadedStarts);
                previous = header;
            }
            TGSongManager manager = new TGSongManager(getFactory());
            manager.setSong(song);
            manager.autoCompleteSilences();
            this.close();
            return song;
        } catch (GTPFormatException gtpFormatException) {
            throw gtpFormatException;
        } catch (Throwable throwable) {
            throw new TGFileFormatException(throwable);
        }
    }
