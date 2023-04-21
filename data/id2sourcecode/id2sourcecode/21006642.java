    public static LevelData loadLevelData(BufferedReader in) throws BadLevelDataException {
        int lineNumber = 0;
        int lastSubjectRelatedLineNumber = 0;
        try {
            String header = in.readLine();
            ++lineNumber;
            if (!header.trim().equals(LEVELFILE_HEADER)) throw new BadLevelDataException(lineNumber);
            LevelData ldata = new LevelData();
            LinkedList<ObjectEntry> entries = new LinkedList<ObjectEntry>();
            while (in.ready()) {
                String currentLine = in.readLine().trim();
                ++lineNumber;
                String[] tokens = currentLine.split("\\s");
                if (currentLine.equals("")) continue; else if (tokens.length > 9 && tokens[0].equalsIgnoreCase("layer:")) {
                    int type = Integer.parseInt(tokens[3]);
                    ObjectEntry newEntry;
                    if (tokens.length == 10 && type != SpriteType.DOOR && type != SpriteType.CLOSED_DOOR) newEntry = new ObjectEntry(); else if (tokens.length == 11 && (type == SpriteType.DOOR || type == SpriteType.CLOSED_DOOR)) {
                        DoorEntry de = new DoorEntry();
                        de.nextLevelRelativeFilename = tokens[10];
                        newEntry = de;
                    } else if (tokens.length >= 12 && type == SpriteType.BOX_QUESTION) {
                        LegacyQuestionEntry lqe = new LegacyQuestionEntry();
                        lqe.question = tokens[10].replace('_', ' ');
                        lqe.correctAnswerIndex = Integer.parseInt(tokens[11]);
                        lqe.answers = new String[tokens.length - 12];
                        for (int i = 12; i < tokens.length; i++) lqe.answers[i - 12] = tokens[i].replace('_', ' ');
                        newEntry = lqe;
                    } else throw new BadLevelDataException(lineNumber);
                    newEntry.layer = Integer.parseInt(tokens[1]);
                    newEntry.type = type;
                    newEntry.x = Integer.parseInt(tokens[5]);
                    newEntry.y = Integer.parseInt(tokens[7]);
                    newEntry.imageRelativeFilename = tokens[9];
                    if (tokens[9].trim().equals("plattform/spikes.png")) newEntry.loadTranslucent = true; else newEntry.loadTranslucent = false;
                    entries.add(newEntry);
                } else if (tokens.length == 4 && tokens[0].equalsIgnoreCase("width:")) {
                    ldata.width = Integer.parseInt(tokens[1]);
                    ldata.height = Integer.parseInt(tokens[3]);
                } else if (tokens.length >= 1 && tokens[0].equalsIgnoreCase("mathematics:")) {
                    String[] subjects = new String[tokens.length - 1];
                    for (int i = 0; i < subjects.length; i++) subjects[i] = tokens[i + 1];
                    ldata.subjectNames = subjects;
                    lastSubjectRelatedLineNumber = lineNumber;
                } else if (tokens.length == 5 && tokens[0].equalsIgnoreCase("player:")) {
                    ldata.playerx = Integer.parseInt(tokens[2]);
                    ldata.playery = Integer.parseInt(tokens[4]);
                } else if (tokens.length == 3 && (tokens[0].equalsIgnoreCase("time") && tokens[1].equalsIgnoreCase("limit:"))) {
                    ldata.timeLimit = Integer.parseInt(tokens[2]);
                } else if (tokens.length == 3 && (tokens[0].equalsIgnoreCase("game") && tokens[1].equalsIgnoreCase("tune:"))) {
                    ldata.musicFilename = tokens[2];
                } else if (tokens.length == 3 && (tokens[0].equalsIgnoreCase("key") && tokens[1].equalsIgnoreCase("limit:"))) {
                    ldata.keyLimit = Integer.parseInt(tokens[2]);
                } else if (tokens.length >= 2 && (tokens[0].equalsIgnoreCase("subject") && tokens[1].equalsIgnoreCase("weights:"))) {
                    int[] subjWeights = new int[tokens.length - 2];
                    for (int i = 0; i < subjWeights.length; i++) {
                        subjWeights[i] = Integer.parseInt(tokens[i + 2]);
                    }
                    ldata.subjectWeights = subjWeights;
                    lastSubjectRelatedLineNumber = lineNumber;
                } else if (tokens.length == 4 && (tokens[0].equalsIgnoreCase("degree") && tokens[1].equalsIgnoreCase("of") && tokens[2].equalsIgnoreCase("difficulty:"))) {
                    ldata.difficultyLevel = Integer.parseInt(tokens[3]);
                } else if (tokens.length == 6 && (tokens[0].equalsIgnoreCase("distribution") && tokens[1].equalsIgnoreCase("of") && tokens[2].equalsIgnoreCase("boxes:"))) {
                    int[] parts = new int[3];
                    ldata.questionBoxPercentage = Float.parseFloat(tokens[3]);
                    ldata.pointsBoxPercentage = Float.parseFloat(tokens[4]);
                    ldata.heartBoxPercentage = Float.parseFloat(tokens[5]);
                    if ((ldata.questionBoxPercentage + ldata.pointsBoxPercentage + ldata.heartBoxPercentage) != 100.0) throw new BadLevelDataException(lineNumber);
                } else if (tokens.length == 3 && (tokens[0].equalsIgnoreCase("force") && tokens[1].equalsIgnoreCase("limit:"))) {
                    ldata.forceLimit = Integer.parseInt(tokens[2]);
                } else if (tokens.length == 3 && (tokens[0].equalsIgnoreCase("force") && tokens[1].equalsIgnoreCase("interval:"))) {
                    ldata.forceInterval = Integer.parseInt(tokens[2]);
                } else if (tokens.length == 3 && (tokens[0].equalsIgnoreCase("background") && tokens[1].equalsIgnoreCase("image:"))) {
                    ldata.backgroundImage = tokens[2];
                } else {
                    System.err.println("tokens.length = " + tokens.length);
                    for (String s : tokens) System.err.println("  " + s);
                    throw new BadLevelDataException(lineNumber);
                }
            }
            if ((ldata.subjectWeights != null && ldata.subjectWeights != null) && (ldata.subjectWeights.length != ldata.subjectWeights.length)) throw new BadLevelDataException(lastSubjectRelatedLineNumber);
            ldata.entries = entries.toArray(new ObjectEntry[entries.size()]);
            return ldata;
        } catch (NumberFormatException nfe) {
            throw new BadLevelDataException(lineNumber);
        } catch (IOException nfe) {
            throw new BadLevelDataException(lineNumber);
        }
    }
