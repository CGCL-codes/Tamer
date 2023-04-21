    public static CueSheet open(java.io.File cueFile) throws IOException {
        CueSheet cueSheet = new CueSheet("", "");
        BufferedReader reader = new BufferedReader(new FileReader(cueFile));
        CueItem currentItem = cueSheet;
        for (String line, token; (line = reader.readLine()) != null; ) {
            Scanner tok = new Scanner(line);
            if (!tok.hasNext()) {
                continue;
            }
            token = tok.next();
            if ("CATALOG".equalsIgnoreCase(token)) {
            } else if ("CDTEXTFILE".equalsIgnoreCase(token)) {
            } else if ("FLAGS".equalsIgnoreCase(token)) {
            } else if ("INDEX".equalsIgnoreCase(token) && currentItem instanceof Track) {
                Integer.parseInt(tok.next());
                Scanner sc = tok.useDelimiter(Pattern.compile(":| "));
                int minutes = sc.nextInt(), seconds = sc.nextInt(), frames = sc.nextInt();
                Index index = new Index(minutes, seconds, frames);
                ((Track) currentItem).getIndices().add(index);
            } else if ("ISRC".equalsIgnoreCase(token)) {
            } else if ("TITLE".equalsIgnoreCase(token)) {
                String nextToken = tok.next();
                if (nextToken.startsWith(QUOTATION_MARK)) {
                    if (nextToken.endsWith(QUOTATION_MARK)) {
                        nextToken = nextToken.substring(1, nextToken.length() - 1);
                    } else {
                        nextToken = nextToken.substring(1) + tok.useDelimiter(QUOTATION_MARK).next();
                    }
                }
                currentItem.setTitle(nextToken);
            } else if ("PERFORMER".equalsIgnoreCase(token)) {
                String nextToken = tok.next();
                if (nextToken.startsWith(QUOTATION_MARK)) {
                    if (nextToken.endsWith(QUOTATION_MARK)) {
                        nextToken = nextToken.substring(1, nextToken.length() - 1);
                    } else {
                        nextToken = nextToken.substring(1) + tok.useDelimiter(QUOTATION_MARK).next();
                    }
                }
                currentItem.setPerformer(nextToken);
            } else if ("FILE".equalsIgnoreCase(token) && currentItem instanceof CueSheet) {
                String nextToken = tok.next();
                if (nextToken.startsWith(QUOTATION_MARK)) {
                    if (nextToken.endsWith(QUOTATION_MARK)) {
                        nextToken = nextToken.substring(1, nextToken.length() - 1);
                    } else {
                        nextToken = nextToken.substring(1) + tok.useDelimiter(QUOTATION_MARK).next();
                    }
                }
                File file = new File(nextToken, File.Type.getType(tok.next()));
                currentItem.setFile(file);
            } else if ("TRACK".equalsIgnoreCase(token)) {
                int trackNo = tok.nextInt();
                String nextToken = tok.nextLine().trim().toUpperCase();
                currentItem = new Track(trackNo, Mode.getMode(nextToken));
                cueSheet.getTracks().add((Track) currentItem);
            } else if ("POSTGAP".equalsIgnoreCase(token)) {
            } else if ("PREGAP".equalsIgnoreCase(token)) {
            } else if ("REM".equalsIgnoreCase(token) && tok.hasNextLine()) {
                System.out.println(tok.nextLine());
            } else if ("SONGWRITER".equalsIgnoreCase(token)) {
            }
        }
        reader.close();
        return cueSheet;
    }
