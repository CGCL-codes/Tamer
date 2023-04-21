    private void uploadFile(HttpURL url, File file, String relative) throws IOException {
        boolean putit = false;
        try {
            if (this.overwrite) {
                putit = true;
            } else {
                long remoteLastMod = Utils.getLastModified(getHttpClient(), url);
                long localLastMod = file.lastModified();
                putit = localLastMod > remoteLastMod;
            }
        } catch (HttpException e) {
            switch(e.getReasonCode()) {
                case HttpStatus.SC_NOT_FOUND:
                    putit = true;
                    break;
                default:
                    throw Utils.makeBuildException("Can't get lastmodified!?", e);
            }
        }
        if (putit) {
            log("Uploading: " + relative, ifVerbose());
            try {
                String contentType = Mimetypes.getMimeType(file, DEFAULT_CONTENT_TYPE);
                if (this.filterSets.hasFilters()) {
                    InputStreamReader reader = new InputStreamReader(new FileInputStream(file), this.encoding);
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    LineTokenizer tok = new LineTokenizer();
                    tok.setIncludeDelims(true);
                    for (String l = tok.getToken(reader); l != null; l = tok.getToken(reader)) {
                        out.write(this.filterSets.replaceTokens(l).getBytes(this.encoding));
                    }
                    Utils.putFile(getHttpClient(), url, new ByteArrayInputStream(out.toByteArray()), contentType, this.locktoken);
                } else {
                    Utils.putFile(getHttpClient(), url, new FileInputStream(file), contentType, this.locktoken);
                }
                this.countWrittenFiles++;
            } catch (HttpException e) {
                throw Utils.makeBuildException("Can't upload " + url, e);
            }
        } else {
            countOmittedFiles++;
            log("Omitted: " + relative + " (uptodate)", ifVerbose());
        }
    }
