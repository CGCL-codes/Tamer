    public static void readActor(BufferedReader actorFile, E3DActor actor) throws Exception {
        StringTokenizer tokenizer = null;
        String line = "";
        String str;
        while ((line = actorFile.readLine()) != null) {
            tokenizer = new StringTokenizer(line, TOKENIZER_CHARS);
            while (tokenizer.hasMoreTokens()) {
                str = readNextValue(tokenizer);
                if (ACTORTAG_TEXTURESET.equals(str)) {
                    String path = tokenizer.nextToken();
                    String jar = tokenizer.nextToken();
                    actor.getWorld().loadTextureSet(path);
                } else if (ACTORTAG_MODEL.equals(str)) {
                    if (!readActor(actorFile, tokenizer, actor)) actor.getEngine().getLogger().writeLine(E3DEngineLogger.SEVERITY_ERROR, "Unable to read actor model: " + actorFile);
                } else if (COMMENT.equalsIgnoreCase(str)) {
                    break;
                }
            }
        }
    }
