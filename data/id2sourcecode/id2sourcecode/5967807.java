    @NotNull
    @Override
    public FaceProvider loadFacesCollection(@NotNull final ErrorView errorView, @NotNull final File collectedDirectory) {
        final String faceFile = ActionBuilderUtils.getString(ACTION_BUILDER, "configSource.image.name");
        final String treeFile = ActionBuilderUtils.getString(ACTION_BUILDER, "configSource.facetree.name");
        final File tmpFaceFile;
        try {
            tmpFaceFile = IOUtils.getFile(collectedDirectory, faceFile);
        } catch (final IOException ex) {
            errorView.addWarning(ErrorViewCategory.FACES_FILE_INVALID, new File(collectedDirectory, faceFile) + ": " + ex.getMessage());
            return new EmptyFaceProvider();
        }
        final CollectedFaceProvider collectedFaceProvider;
        int faces = 0;
        final ErrorViewCollector faceFileErrorViewCollector = new ErrorViewCollector(errorView, tmpFaceFile);
        try {
            collectedFaceProvider = new CollectedFaceProvider(tmpFaceFile);
            final byte[] data = getFileContents(tmpFaceFile);
            final ErrorViewCollector treeFileErrorViewCollector = new ErrorViewCollector(errorView, new File(collectedDirectory, treeFile));
            BufferedReader treeIn = null;
            try {
                final URL url = IOUtils.getResource(collectedDirectory, treeFile);
                final InputStream inputStream2 = url.openStream();
                final Reader reader = new InputStreamReader(inputStream2, IOUtils.MAP_ENCODING);
                treeIn = new BufferedReader(reader);
            } catch (final FileNotFoundException ignored) {
                treeFileErrorViewCollector.addWarning(ErrorViewCategory.FACES_FILE_INVALID);
            }
            final byte[] tag = "IMAGE ".getBytes();
            final StringBuilder faceB = new StringBuilder();
            try {
                final Pattern pattern = Pattern.compile(ActionBuilderUtils.getString(ACTION_BUILDER, "configSource.facetree.input"));
                int offset = 0;
                while (offset < data.length) {
                    if (!ArrayUtils.contains(data, offset, tag)) {
                        throw new IOException("expecting 'IMAGE' at position " + offset);
                    }
                    offset += 6;
                    if (includeFaceNumbers) {
                        while (data[offset++] != 0x20) {
                        }
                    }
                    int size = 0;
                    while (true) {
                        final char c = (char) data[offset++];
                        if (c == ' ') {
                            break;
                        }
                        if (c < '0' || c > '9') {
                            throw new IOException("syntax error at position " + offset + ": not a digit");
                        }
                        size *= 10;
                        size += (int) c - (int) '0';
                    }
                    faceB.setLength(0);
                    while (true) {
                        final char c = (char) data[offset++];
                        if (c == '\n') {
                            break;
                        }
                        if (c == '/') {
                            faceB.setLength(0);
                        } else {
                            faceB.append(c);
                        }
                    }
                    final String faceName = faceB.toString().intern();
                    if (offset + size > data.length) {
                        throw new IOException("truncated at position " + offset);
                    }
                    if (treeIn != null) {
                        final String originalFilename = treeIn.readLine();
                        if (originalFilename == null) {
                            log.warn(ACTION_BUILDER.format("logFaceObjectWithoutOriginalName", faceName));
                        } else {
                            final Matcher matcher = pattern.matcher(originalFilename);
                            if (matcher.matches()) {
                                try {
                                    addFaceObject(faceName, matcher.group(1), offset, size);
                                } catch (final DuplicateFaceException ex) {
                                    faceFileErrorViewCollector.addWarning(ErrorViewCategory.FACES_ENTRY_INVALID, "duplicate face: " + ex.getDuplicate().getFaceName());
                                } catch (final IllegalFaceException ex) {
                                    faceFileErrorViewCollector.addWarning(ErrorViewCategory.FACES_ENTRY_INVALID, "invalid face: " + ex.getFaceObject().getFaceName());
                                }
                            } else {
                                treeFileErrorViewCollector.addWarning(ErrorViewCategory.FACES_ENTRY_INVALID, "syntax error: " + originalFilename);
                            }
                        }
                    }
                    collectedFaceProvider.addInfo(faceName, offset, size);
                    faces++;
                    offset += size;
                }
            } finally {
                if (treeIn != null) {
                    treeIn.close();
                }
            }
        } catch (final IOException ex) {
            faceFileErrorViewCollector.addWarning(ErrorViewCategory.FACES_FILE_INVALID, ex.getMessage());
            return new EmptyFaceProvider();
        }
        if (log.isInfoEnabled()) {
            log.info("Loaded " + faces + " faces from '" + tmpFaceFile + "'.");
        }
        return collectedFaceProvider;
    }
