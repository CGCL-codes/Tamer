    public Result detect(File file) throws IOException {
        Scanner sc0 = null;
        FileInputStream fstream = null;
        try {
            fstream = new FileInputStream(file);
            channel = fstream.getChannel();
            if (channel.size() == 0L) {
                result = new Result();
                result.setDescription("empty");
                result.setMime("application/x-empty");
                return result;
            }
            sc0 = new Scanner(magicInputStream);
            while (sc0.hasNextLine()) {
                String line = sc0.nextLine();
                if (line.indexOf("BBx") != -1) {
                    debug("breakpoint");
                }
                if (line.startsWith(">>(0x3c.l) string PE")) {
                    debug("breakpoint");
                }
                if (line.startsWith("#") || line.trim().isEmpty()) {
                    continue;
                }
                if (line.startsWith("!:")) {
                    if (line.startsWith("!:mime") && currentLevel >= 0 && lastTestSuccessful) {
                        String s = removeInlineComment(line);
                        result.setMime(s.substring("!:mime".length()).trim());
                    }
                } else if (line.startsWith(">")) {
                    int level = level(line);
                    if (currentLevel > 0 && level <= currentLevel || level == currentLevel + 1) {
                        processLine(line, level);
                    }
                } else {
                    if (currentLevel >= 0) {
                        if (fileDescription != null && !fileDescription.toString().trim().isEmpty()) {
                            break;
                        } else {
                            currentLevel = -1;
                            result = null;
                            currentPosition = 0;
                        }
                    }
                    processLine(line, 0);
                }
            }
            if (fileDescription != null && fileDescription.length() > 0) {
                result.setDescription(fileDescription.toString().trim().replace("\n", ""));
                if (result.getMime() == null) {
                    if (result.getDescription().indexOf("ASCII") != -1) {
                        result.setMime("text/plain");
                    }
                }
            }
            return result;
        } finally {
            if (fstream != null) {
                fstream.close();
            }
            if (channel != null) {
                channel.close();
            }
            if (sc0 != null) {
                sc0.close();
            }
            resetInstanceVariables();
        }
    }
