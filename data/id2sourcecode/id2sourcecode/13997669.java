    private static boolean readActor(BufferedReader actorFile, StringTokenizer tokenizer, E3DActor actor) throws IOException {
        String str;
        String line;
        boolean startBlockFound = false;
        boolean endBlockFound = false;
        E3DHashListMap orphanedBones = new E3DHashListMap();
        HashMap orphanedTriangleBoneAttachments = new HashMap();
        while (!endBlockFound && (line = actorFile.readLine()) != null) {
            tokenizer = new StringTokenizer(line, TOKENIZER_CHARS);
            while (!endBlockFound && tokenizer.hasMoreTokens()) {
                str = readNextValue(tokenizer);
                if (START_BLOCK.equals(str)) {
                    startBlockFound = true;
                } else if (ACTORTAG_BONE.equals(str)) {
                    if (!startBlockFound) return false;
                    String boneID = readNextValue(tokenizer);
                    String parentBoneID = readNextValue(tokenizer);
                    E3DBone bone = readBone(actor.getEngine(), actorFile, boneID);
                    if (bone != null) {
                        if (!actor.getSkeleton().addBone(bone, parentBoneID)) orphanedBones.put(parentBoneID, bone); else {
                            linkOrphanedBonesToNewBone(actor, bone, orphanedBones);
                        }
                    } else actor.getEngine().getLogger().writeLine(E3DEngineLogger.SEVERITY_WARNING, "Unable to read bone: " + E3DStringHelper.getNonNullableString(boneID) + ".  Possibly missing attributes or malformed definition.");
                } else if (ACTORTAG_TRIANGLE.equals(str)) {
                    if (!startBlockFound) return false;
                    TempTriangle tempTriangle = readTriangle(actor.getEngine(), actorFile);
                    if (tempTriangle != null) {
                        E3DTriangle triangle = tempTriangle.triangle;
                        VertexBoneInformation boneInformation = tempTriangle.vertexBoneInformation;
                        if (triangle != null) {
                            actor.getMesh().addTriangle(triangle);
                            if (boneInformation != null) orphanedTriangleBoneAttachments.put(triangle, boneInformation);
                        }
                    }
                } else if (ACTORTAG_ANIMATION.equals(str)) {
                    if (!startBlockFound) return false;
                    String animationName = readNextValue(tokenizer);
                    String fps = readNextValue(tokenizer);
                    double dFPS = 20.0;
                    try {
                        dFPS = Double.parseDouble(fps);
                    } catch (NumberFormatException e) {
                        actor.getEngine().getLogger().writeLine(E3DEngineLogger.SEVERITY_WARNING, "No FPS specified for animation.  20 FPS will be used by default (1 frame == 0.5s)");
                    }
                    if (dFPS == 0) dFPS = 20.0;
                    E3DAnimation animation = readAnimation(actor.getEngine(), animationName, dFPS, actorFile);
                    if (animation != null) actor.getSkeleton().addAnimation(animation);
                } else if (COMMENT.equals(str)) break; else if (END_BLOCK.equals(str)) endBlockFound = true; else actor.getEngine().getLogger().writeLine(E3DEngineLogger.SEVERITY_INFO, "Unknown tag in actor model definition: " + str);
            }
        }
        linkTrianglesToBones(actor, orphanedTriangleBoneAttachments);
        return endBlockFound;
    }
