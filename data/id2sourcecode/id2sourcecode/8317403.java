    protected synchronized boolean load(final URL url) {
        if (!fMustLoad) return fLoaded;
        if (url != null) {
            InputStream stream = null;
            int line = 0;
            try {
                stream = url.openStream();
                if (stream != null) {
                    String word = null;
                    CharsetDecoder decoder = Charset.forName(System.getProperty("file.encoding")).newDecoder();
                    decoder.replaceWith("?");
                    decoder.onMalformedInput(CodingErrorAction.REPORT);
                    decoder.onUnmappableCharacter(CodingErrorAction.REPORT);
                    final BufferedReader reader = new BufferedReader(new InputStreamReader(stream, decoder));
                    boolean doRead = true;
                    while (doRead) {
                        try {
                            word = reader.readLine();
                        } catch (MalformedInputException ex) {
                            decoder.onMalformedInput(CodingErrorAction.REPLACE);
                            decoder.reset();
                            word = reader.readLine();
                            decoder.onMalformedInput(CodingErrorAction.REPORT);
                            String message = Messages.format(RubyUIMessages.AbstractSpellingDictionary_encodingError, new String[] { word, decoder.replacement(), url.toString() });
                            IStatus status = new Status(IStatus.ERROR, RubyUI.ID_PLUGIN, IStatus.OK, message, ex);
                            RubyPlugin.log(status);
                            doRead = word != null;
                            continue;
                        }
                        doRead = word != null;
                        if (doRead) hashWord(word);
                    }
                    return true;
                }
            } catch (IOException exception) {
                if (line > 0) {
                    String message = Messages.format(RubyUIMessages.AbstractSpellingDictionary_encodingError, new Object[] { new Integer(line), url.toString() });
                    IStatus status = new Status(IStatus.ERROR, RubyUI.ID_PLUGIN, IStatus.OK, message, exception);
                    RubyPlugin.log(status);
                } else RubyPlugin.log(exception);
            } finally {
                fMustLoad = false;
                try {
                    if (stream != null) stream.close();
                } catch (IOException x) {
                }
            }
        }
        return false;
    }
