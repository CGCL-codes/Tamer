    public void load(@NotNull final ErrorView errorView, @NotNull final URL url) {
        final ErrorViewCollector errorViewCollector = new ErrorViewCollector(errorView, url);
        try {
            final InputStream inputStream = url.openStream();
            try {
                final Reader reader = new InputStreamReader(inputStream, IOUtils.MAP_ENCODING);
                try {
                    final BufferedReader bufferedReader = new BufferedReader(reader);
                    try {
                        final LineNumberReader lnr = new LineNumberReader(bufferedReader);
                        try {
                            int yp = 0;
                            boolean hasErrors = false;
                            while (true) {
                                final String inputLine = lnr.readLine();
                                if (inputLine == null) {
                                    break;
                                }
                                final String line = inputLine.trim();
                                if (line.length() == 0 || line.startsWith("#")) {
                                    continue;
                                }
                                if (yp >= Y_DIM) {
                                    errorViewCollector.addWarning(ErrorViewCategory.ARCHDEF_ENTRY_INVALID, lnr.getLineNumber(), "too many entries");
                                    break;
                                }
                                final String[] numbers = StringUtils.PATTERN_SPACE.split(line);
                                if (numbers.length != 2) {
                                    errorViewCollector.addError(ErrorViewCategory.ARCHDEF_ENTRY_INVALID, lnr.getLineNumber(), "syntax error");
                                    hasErrors = true;
                                    continue;
                                }
                                final Dimension d = new Dimension();
                                try {
                                    d.width = Integer.parseInt(numbers[0]);
                                } catch (final NumberFormatException ignored) {
                                    errorViewCollector.addError(ErrorViewCategory.ARCHDEF_ENTRY_INVALID, lnr.getLineNumber(), "invalid number: " + numbers[0]);
                                    hasErrors = true;
                                    continue;
                                }
                                try {
                                    d.height = Integer.parseInt(numbers[1]);
                                } catch (final NumberFormatException ignored) {
                                    errorViewCollector.addError(ErrorViewCategory.ARCHDEF_ENTRY_INVALID, lnr.getLineNumber(), "invalid number: " + numbers[1]);
                                    hasErrors = true;
                                    continue;
                                }
                                data[yp++] = new MultiPositionEntry(isoMapSquareInfo, d);
                            }
                            if (yp < Y_DIM && !hasErrors) {
                                errorViewCollector.addWarning(ErrorViewCategory.ARCHDEF_ENTRY_INVALID, "missing " + (Y_DIM - yp) + " entries");
                            }
                            if (log.isInfoEnabled()) {
                                log.info("Loaded multi-part position data from '" + url + "'");
                            }
                        } finally {
                            lnr.close();
                        }
                    } finally {
                        bufferedReader.close();
                    }
                } finally {
                    reader.close();
                }
            } finally {
                inputStream.close();
            }
        } catch (final IOException ex) {
            errorViewCollector.addWarning(ErrorViewCategory.ARCHDEF_FILE_INVALID, ex.getMessage());
        }
    }
