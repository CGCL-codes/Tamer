    public void decodeHeader() {
        try {
            MP4Atom type = MP4Atom.createAtom(fis);
            log.debug("Type {}", MP4Atom.intToType(type.getType()));
            int topAtoms = 0;
            while (topAtoms < 2) {
                MP4Atom atom = MP4Atom.createAtom(fis);
                switch(atom.getType()) {
                    case 1836019574:
                        topAtoms++;
                        MP4Atom moov = atom;
                        log.debug("Type {}", MP4Atom.intToType(moov.getType()));
                        log.debug("moov children: {}", moov.getChildren());
                        moovOffset = fis.getOffset() - moov.getSize();
                        MP4Atom mvhd = moov.lookup(MP4Atom.typeToInt("mvhd"), 0);
                        if (mvhd != null) {
                            log.debug("Movie header atom found");
                            timeScale = mvhd.getTimeScale();
                            duration = mvhd.getDuration();
                            log.debug("Time scale {} Duration {}", timeScale, duration);
                        }
                        MP4Atom trak = moov.lookup(MP4Atom.typeToInt("trak"), 0);
                        if (trak != null) {
                            log.debug("Track atom found");
                            log.debug("trak children: {}", trak.getChildren());
                            MP4Atom edts = trak.lookup(MP4Atom.typeToInt("edts"), 0);
                            if (edts != null) {
                                log.debug("Edit atom found");
                                log.debug("edts children: {}", edts.getChildren());
                            }
                            MP4Atom mdia = trak.lookup(MP4Atom.typeToInt("mdia"), 0);
                            if (mdia != null) {
                                log.debug("Media atom found");
                                int scale = 0;
                                MP4Atom mdhd = mdia.lookup(MP4Atom.typeToInt("mdhd"), 0);
                                if (mdhd != null) {
                                    log.debug("Media data header atom found");
                                    scale = mdhd.getTimeScale();
                                    log.debug("Time scale {}", scale);
                                }
                                MP4Atom hdlr = mdia.lookup(MP4Atom.typeToInt("hdlr"), 0);
                                if (hdlr != null) {
                                    log.debug("Handler ref atom found");
                                    log.debug("Handler type: {}", MP4Atom.intToType(hdlr.getHandlerType()));
                                    String hdlrType = MP4Atom.intToType(hdlr.getHandlerType());
                                    if ("soun".equals(hdlrType)) {
                                        if (scale > 0) {
                                            audioTimeScale = scale * 1.0;
                                            log.debug("Audio time scale: {}", audioTimeScale);
                                        }
                                    }
                                }
                                MP4Atom minf = mdia.lookup(MP4Atom.typeToInt("minf"), 0);
                                if (minf != null) {
                                    log.debug("Media info atom found");
                                    MP4Atom smhd = minf.lookup(MP4Atom.typeToInt("smhd"), 0);
                                    if (smhd != null) {
                                        log.debug("Sound header atom found");
                                        MP4Atom dinf = minf.lookup(MP4Atom.typeToInt("dinf"), 0);
                                        if (dinf != null) {
                                            log.debug("Data info atom found");
                                            log.debug("Sound dinf children: {}", dinf.getChildren());
                                            MP4Atom dref = dinf.lookup(MP4Atom.typeToInt("dref"), 0);
                                            if (dref != null) {
                                                log.debug("Data reference atom found");
                                            }
                                        }
                                        MP4Atom stbl = minf.lookup(MP4Atom.typeToInt("stbl"), 0);
                                        if (stbl != null) {
                                            log.debug("Sample table atom found");
                                            log.debug("Sound stbl children: {}", stbl.getChildren());
                                            MP4Atom stsd = stbl.lookup(MP4Atom.typeToInt("stsd"), 0);
                                            if (stsd != null) {
                                                log.debug("Sample description atom found");
                                                MP4Atom mp4a = stsd.getChildren().get(0);
                                                setAudioCodecId(MP4Atom.intToType(mp4a.getType()));
                                                log.debug("Sample size: {}", mp4a.getSampleSize());
                                                int ats = mp4a.getTimeScale();
                                                if (ats > 0) {
                                                    audioTimeScale = ats * 1.0;
                                                }
                                                audioChannels = mp4a.getChannelCount();
                                                log.debug("Sample rate (audio time scale): {}", audioTimeScale);
                                                log.debug("Channels: {}", audioChannels);
                                                if (mp4a.getChildren().size() > 0) {
                                                    log.debug("Elementary stream descriptor atom found");
                                                    MP4Atom esds = mp4a.getChildren().get(0);
                                                    log.debug("{}", ToStringBuilder.reflectionToString(esds));
                                                    MP4Descriptor descriptor = esds.getEsd_descriptor();
                                                    log.debug("{}", ToStringBuilder.reflectionToString(descriptor));
                                                    if (descriptor != null) {
                                                        Vector<MP4Descriptor> children = descriptor.getChildren();
                                                        for (int e = 0; e < children.size(); e++) {
                                                            MP4Descriptor descr = children.get(e);
                                                            log.debug("{}", ToStringBuilder.reflectionToString(descr));
                                                            if (descr.getChildren().size() > 0) {
                                                                Vector<MP4Descriptor> children2 = descr.getChildren();
                                                                for (int e2 = 0; e2 < children2.size(); e2++) {
                                                                    MP4Descriptor descr2 = children2.get(e2);
                                                                    log.debug("{}", ToStringBuilder.reflectionToString(descr2));
                                                                    if (descr2.getType() == MP4Descriptor.MP4DecSpecificInfoDescriptorTag) {
                                                                        audioDecoderBytes = descr2.getDSID();
                                                                        switch(audioDecoderBytes[0]) {
                                                                            case 0x12:
                                                                            default:
                                                                                audioCodecType = 1;
                                                                                break;
                                                                            case 0x0a:
                                                                                audioCodecType = 0;
                                                                                break;
                                                                            case 0x11:
                                                                            case 0x13:
                                                                                audioCodecType = 2;
                                                                                break;
                                                                        }
                                                                        e = 99;
                                                                        break;
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                            MP4Atom stsc = stbl.lookup(MP4Atom.typeToInt("stsc"), 0);
                                            if (stsc != null) {
                                                log.debug("Sample to chunk atom found");
                                                audioSamplesToChunks = stsc.getRecords();
                                                log.debug("Record count: {}", audioSamplesToChunks.size());
                                                MP4Atom.Record rec = audioSamplesToChunks.firstElement();
                                                log.debug("Record data: Description index={} Samples per chunk={}", rec.getSampleDescriptionIndex(), rec.getSamplesPerChunk());
                                            }
                                            MP4Atom stsz = stbl.lookup(MP4Atom.typeToInt("stsz"), 0);
                                            if (stsz != null) {
                                                log.debug("Sample size atom found");
                                                audioSamples = stsz.getSamples();
                                                log.debug("Sample size: {}", stsz.getSampleSize());
                                                log.debug("Sample count: {}", audioSamples.size());
                                            }
                                            MP4Atom stco = stbl.lookup(MP4Atom.typeToInt("stco"), 0);
                                            if (stco != null) {
                                                log.debug("Chunk offset atom found");
                                                audioChunkOffsets = stco.getChunks();
                                                log.debug("Chunk count: {}", audioChunkOffsets.size());
                                            }
                                            MP4Atom stts = stbl.lookup(MP4Atom.typeToInt("stts"), 0);
                                            if (stts != null) {
                                                log.debug("Time to sample atom found");
                                                Vector<MP4Atom.TimeSampleRecord> records = stts.getTimeToSamplesRecords();
                                                log.debug("Record count: {}", records.size());
                                                MP4Atom.TimeSampleRecord rec = records.firstElement();
                                                log.debug("Record data: Consecutive samples={} Duration={}", rec.getConsecutiveSamples(), rec.getSampleDuration());
                                                if (records.size() > 1) {
                                                    log.warn("Audio samples have differing durations, audio playback may fail");
                                                }
                                                audioSampleDuration = rec.getSampleDuration();
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        StringBuilder sb = new StringBuilder();
                        double clipTime = ((double) duration / (double) timeScale);
                        log.debug("Clip time: {}", clipTime);
                        int minutes = (int) (clipTime / 60);
                        if (minutes > 0) {
                            sb.append(minutes);
                            sb.append('.');
                        }
                        NumberFormat df = DecimalFormat.getInstance();
                        df.setMaximumFractionDigits(2);
                        sb.append(df.format((clipTime % 60)));
                        formattedDuration = sb.toString();
                        log.debug("Time: {}", formattedDuration);
                        break;
                    case 1835295092:
                        topAtoms++;
                        long dataSize = 0L;
                        MP4Atom mdat = atom;
                        dataSize = mdat.getSize();
                        log.debug("{}", ToStringBuilder.reflectionToString(mdat));
                        mdatOffset = fis.getOffset() - dataSize;
                        log.debug("File size: {} mdat size: {}", file.length(), dataSize);
                        break;
                    case 1718773093:
                    case 2003395685:
                        break;
                    default:
                        log.warn("Unexpected atom: {}", MP4Atom.intToType(atom.getType()));
                }
            }
            moovOffset += 8;
            mdatOffset += 8;
            log.debug("Offsets moov: {} mdat: {}", moovOffset, mdatOffset);
        } catch (IOException e) {
            log.error("Exception decoding header / atoms", e);
        }
    }
