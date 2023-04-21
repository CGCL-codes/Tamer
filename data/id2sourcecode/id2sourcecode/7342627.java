    public void getFrontEndData(String mFilename, Properties mProps) {
        if (gToggledOverlays == null) gToggledOverlays = new HashMap<Character, SpecOverlay>();
        if (mProps.getProperty("recognizercommandline") != null) {
            String tRecCmd = mProps.getProperty("recognizercommandline");
            String tRecBin = tRecCmd.split(" ")[0];
            String tPhoneTag = mProps.getProperty("phonetag");
            HashMap<Integer, Phone> tPhoneMap = new HashMap<Integer, Phone>();
            HashMap<String, Character> tAnnotTagMap = new HashMap<String, Character>();
            HashMap<Character, Color> tAnnotColorMap = new HashMap<Character, Color>();
            for (char c = '0'; c <= '9'; c++) {
                if (mProps.getProperty("annottag" + c) != null) tAnnotTagMap.put(mProps.getProperty("annottag" + c), c);
                if (mProps.getProperty("annotcolor" + c) != null) tAnnotColorMap.put(c, new Color(Integer.parseInt(mProps.getProperty("annotcolor" + c), 16)));
            }
            Scanner tScanner = null;
            Scanner tErrScanner = null;
            BufferedOutputStream tOut = null;
            RawSoundFile tSoundFile = null;
            if (!(new File(tRecBin)).exists()) {
                System.err.println("Error, cannot find binary " + tRecBin);
                return;
            }
            Process tProc = null;
            try {
                System.out.println("Attempting to run: " + tRecCmd);
                tProc = Runtime.getRuntime().exec(tRecCmd);
            } catch (IOException mIOE) {
                System.err.println("Errors running frontend! Attempting to run workaround script...");
                System.err.println("Ack! Can't run workaround! I give up!");
                return;
            }
            tScanner = new Scanner(tProc.getInputStream());
            tErrScanner = new Scanner(tProc.getErrorStream());
            tOut = new BufferedOutputStream(tProc.getOutputStream());
            try {
                tSoundFile = new RawSoundFile(mFilename);
                int b;
                while ((b = tSoundFile.read()) != -1) tOut.write(b);
                tOut.close();
            } catch (IOException mIOE) {
                System.err.println("Error piping sound file to recognizer!");
                System.err.println("Bailing...");
                return;
            }
            Pattern tPattern = Pattern.compile("([A-Z0-9]*) *([0-9]*)> *([\\S&&[^:]]*):? *([0-9-]*)");
            while (tScanner.hasNextLine()) {
                Matcher tMatcher = tPattern.matcher(tScanner.nextLine());
                if (tMatcher.matches()) {
                    if (tMatcher.group(1).equals(tPhoneTag)) {
                        tPhoneMap.put(Integer.parseInt(tMatcher.group(2)), new Phone(tMatcher.group(3), gVoicedPhoneList.contains(tMatcher.group(3)), gPhoneColorMap.get(tMatcher.group(3))));
                    } else {
                        if (tAnnotTagMap.containsKey(tMatcher.group(1))) {
                            if (!gToggledOverlays.containsKey(tAnnotTagMap.get(tMatcher.group(1)))) gToggledOverlays.put(tAnnotTagMap.get(tMatcher.group(1)), new SpecOverlay(tAnnotColorMap.get(tAnnotTagMap.get(tMatcher.group(1))), tMatcher.group(1)));
                            gToggledOverlays.get(tAnnotTagMap.get(tMatcher.group(1))).add(new Point(Integer.parseInt(tMatcher.group(2)), Integer.parseInt(tMatcher.group(3))));
                        }
                    }
                }
            }
            while (tErrScanner.hasNextLine()) System.err.println("Rec stderr: " + tErrScanner.nextLine());
            if (tPhoneMap.size() != 0) gPhoneTracks.add(new HypothPhoneTrack(tPhoneMap, gClipInfo.getAudioLength(), tPhoneTag));
        }
    }
