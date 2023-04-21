    protected synchronized boolean load(final URL url) {
        if (!this.fMustLoad) {
            return this.fLoaded;
        }
        if (url != null) {
            InputStream stream = null;
            final int line = 0;
            try {
                stream = url.openStream();
                if (stream != null) {
                    String word = null;
                    final CharsetDecoder decoder = Charset.forName(this.getEncoding()).newDecoder();
                    decoder.onMalformedInput(CodingErrorAction.REPORT);
                    decoder.onUnmappableCharacter(CodingErrorAction.REPORT);
                    final BufferedReader reader = new BufferedReader(new InputStreamReader(stream, decoder));
                    boolean doRead = true;
                    while (doRead) {
                        try {
                            word = reader.readLine();
                        } catch (final MalformedInputException ex) {
                            decoder.onMalformedInput(CodingErrorAction.REPLACE);
                            decoder.reset();
                            word = reader.readLine();
                            decoder.onMalformedInput(CodingErrorAction.REPORT);
                            SpellActivator.log(ex);
                            doRead = word != null;
                            continue;
                        }
                        doRead = word != null;
                        if (doRead) {
                            this.hashWord(word);
                        }
                    }
                    return true;
                }
            } catch (final FileNotFoundException ex) {
                final String urlString = url.toString();
                final String lowercaseUrlString = urlString.toLowerCase();
                if (urlString.equals(lowercaseUrlString)) {
                    SpellActivator.log(ex);
                } else {
                    try {
                        return this.load(new URL(lowercaseUrlString));
                    } catch (final MalformedURLException e) {
                        SpellActivator.log(e);
                    }
                }
            } catch (final IOException exception) {
                if (line > 0) {
                    SpellActivator.log(exception);
                } else {
                    SpellActivator.log(exception);
                }
            } finally {
                this.fMustLoad = false;
                try {
                    if (stream != null) {
                        stream.close();
                    }
                } catch (final IOException x) {
                }
            }
        }
        return false;
    }
