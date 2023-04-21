    public byte[] sendGetRequest(final HttpClient cl, final String link) throws HttpStatusException, IOException {
        try {
            final GetMethod gm = new GetMethod(link);
            this.getLog().info("Downloading from Bugzilla at: " + link);
            gm.setFollowRedirects(true);
            cl.executeMethod(gm);
            final StatusLine sl = gm.getStatusLine();
            if (sl == null) {
                this.getLog().error("Unknown error validating link: " + link);
                throw new HttpStatusException("UNKNOWN STATUS");
            }
            if (gm.getStatusCode() == HttpStatus.SC_MOVED_TEMPORARILY) {
                final Header locationHeader = gm.getResponseHeader("Location");
                if (locationHeader == null) {
                    this.getLog().warn("Site sent redirect, but did not set Location header");
                } else {
                    final String newLink = locationHeader.getValue();
                    this.getLog().debug("Following redirect to " + newLink);
                    this.sendGetRequest(cl, newLink);
                }
            }
            if (gm.getStatusCode() == HttpStatus.SC_OK) {
                final InputStream is = gm.getResponseBodyAsStream();
                final ByteArrayOutputStream baos = new ByteArrayOutputStream();
                final byte[] buff = new byte[256];
                int readed = is.read(buff);
                while (readed != -1) {
                    baos.write(buff, 0, readed);
                    readed = is.read(buff);
                }
                this.getLog().debug("Downloading from Bugzilla was successful");
                return baos.toByteArray();
            } else {
                this.getLog().warn("Downloading from Bugzilla failed. Received: [" + gm.getStatusCode() + "]");
                throw new HttpStatusException("WRONG STATUS");
            }
        } catch (final HttpException e) {
            if (this.getLog().isDebugEnabled()) {
                this.getLog().error("Error downloading issues from Bugzilla:", e);
            } else {
                this.getLog().error("Error downloading issues from Bugzilla url: " + e.getLocalizedMessage());
            }
            throw e;
        } catch (final IOException e) {
            if (this.getLog().isDebugEnabled()) {
                this.getLog().error("Error downloading issues from Bugzilla:", e);
            } else {
                this.getLog().error("Error downloading issues from Bugzilla. Cause is " + e.getLocalizedMessage());
            }
            throw e;
        }
    }
