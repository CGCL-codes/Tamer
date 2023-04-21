    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        final ServletInputStream sis = request.getInputStream();
        final byte[] buf = new byte[8192];
        int len = 0;
        String limiter = null;
        String line = null;
        String disposition = null;
        boolean dataReading = false;
        String name = null;
        String filename = null;
        String description = null;
        String meaning = null;
        String parameters = null;
        File file = new File();
        Long sProject = null;
        String locale = null;
        try {
            while ((len = sis.readLine(buf, 0, 8096)) >= 0) {
                line = getLine(buf, len);
                if (limiter == null) {
                    limiter = line;
                    continue;
                }
                if (line.startsWith("Content-Disposition: ")) {
                    disposition = line.substring(21);
                    final StringTokenizer st = new StringTokenizer(disposition, ";");
                    while (st.hasMoreTokens()) {
                        final String token = st.nextToken().trim();
                        if (token.indexOf("=") >= 0) {
                            final String key = token.substring(0, token.indexOf("="));
                            final String value = token.substring(token.indexOf("=") + 2, token.length() - 1);
                            if ("name".equals(key)) {
                                name = value;
                            } else if ("filename".equals(key)) {
                                filename = value;
                            }
                        }
                    }
                    continue;
                }
                if (line.startsWith("Content-Type: ")) {
                    continue;
                }
                if (!dataReading && line.equals("")) {
                    dataReading = true;
                    if ("uploader".equals(name)) {
                        file.setFilename(filename);
                        if (GenericValidator.isBlankOrNull(locale)) {
                            file = service.addFileInProject(file, sProject);
                        }
                    }
                    continue;
                }
                if (line.startsWith(limiter)) {
                    dataReading = false;
                    continue;
                }
                if ("project".equals(name)) {
                    sProject = Long.parseLong(line);
                    final Authentication token = SecurityContextHolder.getContext().getAuthentication();
                    final boolean participate = service.doesUserParticipateInProject(token, sProject, new Role[] { Role.ADMINISTRATOR, Role.DEVELOPER });
                    if (!participate) {
                        response.sendError(HttpServletResponse.SC_FORBIDDEN);
                    }
                    continue;
                }
                if ("locale".equals(name)) {
                    locale = line;
                    continue;
                }
                if ("uploader".equals(name)) {
                    line = line.trim();
                    if ((line.startsWith("#")) || (line.startsWith("//"))) {
                        line = line.substring(2);
                        if (line.startsWith("Description")) {
                            description = line.substring(13);
                        } else if (line.startsWith("Meaning")) {
                            meaning = line.substring(9);
                        } else if (line.startsWith("0=")) {
                            parameters = line;
                        }
                    } else {
                        final int pos = line.indexOf("=");
                        if (pos > 0) {
                            final String key = line.substring(0, pos);
                            final String value = line.substring(pos + 1);
                            final Project project = new Project();
                            project.setId(sProject);
                            final Entry entry = new Entry();
                            entry.setKey(key);
                            entry.setContent(value);
                            entry.setProject(project);
                            entry.setMeaning(meaning);
                            entry.setParameters(parameters);
                            entry.setDescription(description);
                            if (GenericValidator.isBlankOrNull(locale)) {
                                service.addEntryInFile(entry, file);
                            } else {
                                final Authentication token = SecurityContextHolder.getContext().getAuthentication();
                                final Translation translation = new Translation();
                                translation.setEntry(entry);
                                translation.setLocale(locale);
                                translation.setTranslation(value);
                                service.addTranslationByKey(translation, token);
                            }
                            description = null;
                            meaning = null;
                            parameters = null;
                        }
                    }
                }
            }
            response.getOutputStream().write("Upload successful.".getBytes());
        } catch (final IOException e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
            throw e;
        } catch (final TranslationAlreadyExistException e) {
            response.getOutputStream().write("Attempt to translate a term already translated.".getBytes());
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
    }
