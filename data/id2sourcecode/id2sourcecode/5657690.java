    public void write(Movie qualities) throws IOException {
        qualities = correctTimescale(qualities);
        IsoFile isoFile = ismvBuilder.build(qualities);
        if (writeSingleFile) {
            File allQualities = new File(outputDirectory, "all-qualities.mp4");
            FileOutputStream allQualis = new FileOutputStream(allQualities);
            isoFile.getBox(allQualis.getChannel());
            allQualis.close();
        }
        for (Track track : qualities.getTracks()) {
            String bitrate = Long.toString(manifestWriter.getBitrate(track));
            long trackId = track.getTrackMetaData().getTrackId();
            Iterator<Box> boxIt = isoFile.getBoxes().iterator();
            File mediaOutDir;
            if (track.getMediaHeaderBox() instanceof SoundMediaHeaderBox) {
                mediaOutDir = new File(outputDirectory, "audio");
            } else if (track.getMediaHeaderBox() instanceof VideoMediaHeaderBox) {
                mediaOutDir = new File(outputDirectory, "video");
            } else {
                System.err.println("Skipping Track with handler " + track.getHandler() + " and " + track.getMediaHeaderBox().getClass().getSimpleName());
                continue;
            }
            File bitrateOutputDir = new File(mediaOutDir, bitrate);
            bitrateOutputDir.mkdirs();
            LOG.finer("Created : " + bitrateOutputDir.getCanonicalPath());
            long[] fragmentTimes = manifestWriter.calculateFragmentDurations(track, qualities);
            long startTime = 0;
            int currentFragment = 0;
            while (boxIt.hasNext()) {
                Box b = boxIt.next();
                if (b instanceof MovieFragmentBox) {
                    assert ((MovieFragmentBox) b).getTrackCount() == 1;
                    if (((MovieFragmentBox) b).getTrackNumbers()[0] == trackId) {
                        FileOutputStream fos = new FileOutputStream(new File(bitrateOutputDir, Long.toString(startTime)));
                        startTime += fragmentTimes[currentFragment++];
                        FileChannel fc = fos.getChannel();
                        Box mdat = boxIt.next();
                        assert mdat.getType().equals("mdat");
                        b.getBox(fc);
                        mdat.getBox(fc);
                        fc.truncate(fc.position());
                        fc.close();
                    }
                }
            }
        }
        FileWriter fw = new FileWriter(new File(outputDirectory, "Manifest"));
        fw.write(manifestWriter.getManifest(qualities));
        fw.close();
    }
