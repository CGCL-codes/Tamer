    public void process(String[] args) throws Exception {
        String s;
        int p, t;
        int idx;
        Cue cue = new Cue();
        Track track = null;
        Track prevtrack = null;
        BufferedReader cuef;
        FileInputStream binf;
        printVersion();
        Params params = new Params();
        if (!params.parseArgs(args)) {
            return;
        }
        try {
            binf = new FileInputStream(params.getBinfile());
        } catch (IOException ex) {
            System.err.println("Could not open BIN " + params.getBinfile() + ": " + ex.getLocalizedMessage());
            return;
        } catch (NullPointerException ex) {
            System.err.println("No BIN file defined.");
            params.usage();
            return;
        }
        try {
            cuef = new BufferedReader(new InputStreamReader(new FileInputStream(params.getCuefile())));
        } catch (IOException ex) {
            System.err.println("Could not open CUE " + params.getCuefile() + ": " + ex.getLocalizedMessage());
            return;
        } catch (NullPointerException ex) {
            System.err.println("No CUE file defined.");
            params.usage();
            return;
        }
        System.out.println("Reading the CUE file:");
        if (cuef.readLine() == null) {
            System.err.println("Could not read first line from " + params.getCuefile());
            return;
        }
        while ((s = cuef.readLine()) != null) {
            while (((p = s.indexOf('\r')) != -1) || ((p = s.indexOf('\n')) != -1)) {
                s = s.substring(p + 1);
            }
            if ((p = s.indexOf("TRACK")) != -1) {
                System.out.print("\nTrack ");
                if ((p = s.indexOf(' ', p)) == -1) {
                    System.err.println("... ouch, no space after TRACK.");
                    continue;
                }
                p++;
                if ((t = s.indexOf(' ', p)) == -1) {
                    System.out.println("... ouch, no space after track number.");
                    continue;
                }
                prevtrack = track;
                track = new Track();
                cue.addTrack(track);
                track.setNum(Integer.parseInt(s.substring(p, t)));
                p = t + 1;
                s = s.substring(p);
                System.out.print(String.format("%2d: %-12.12s ", new Object[] { new Integer(track.getNum()), s }));
                track.setModes(s);
                cue.getTrackMode(track, s, params);
            } else if ((p = s.indexOf("INDEX")) != -1) {
                if (track == null) {
                    System.err.println("... ouch, misplaced INDEX.");
                    continue;
                }
                if ((p = s.indexOf(' ', p)) == -1) {
                    System.err.println("... ouch, no space after TRACK.");
                    continue;
                }
                p++;
                if ((t = s.indexOf(' ', p)) == -1) {
                    System.out.println("... ouch, no space after track number.");
                    continue;
                }
                idx = Integer.parseInt(s.substring(p, t));
                s = s.substring(t + 1);
                System.out.print(String.format(" %02d %s", new Object[] { new Integer(idx), s }));
                track.setStartsect(Helper.time2frames(s));
                track.setStart(track.getStartsect() * Constants.SECTLEN);
                if (params.isVerbose()) {
                    System.out.print(" (startsect " + track.getStartsect() + " ofs " + track.getStart());
                }
                if ((prevtrack != null) && (prevtrack.getStopsect() < 0)) {
                    prevtrack.setStopsect(track.getStartsect());
                    prevtrack.setStop(track.getStart() - 1);
                }
            }
        }
        if (track != null) {
            track.setStop((int) binf.getChannel().size());
            track.setStopsect(track.getStop() / Constants.SECTLEN);
        }
        System.out.println("\n");
        System.out.println("Writing tracks:\n");
        for (track = cue.getFirstTrack(); cue.hasMoreTracks(); track = cue.getNextTrack()) {
            writetrack(binf, track, params.getBasefile(), params);
        }
        binf.close();
        cuef.close();
    }
