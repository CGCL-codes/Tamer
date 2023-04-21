    public byte[] sendPostRequest(final HttpClient cl, final String link, final String parameters) throws HttpStatusException, IOException {
        try {
            final PostMethod pm = new PostMethod(link);
            if (parameters != null) {
                final String[] params = parameters.split("&");
                for (final String param : params) {
                    final String[] pair = param.split("=");
                    if (pair.length == 2) {
                        pm.addParameter(pair[0], pair[1]);
                    }
                }
            }
            this.getLog().info("Downloading from Bugzilla at: " + link);
            cl.executeMethod(pm);
            final StatusLine sl = pm.getStatusLine();
            if (sl == null) {
                this.getLog().error("Unknown error validating link: " + link);
                throw new HttpStatusException("UNKNOWN STATUS");
            }
            if (pm.getStatusCode() == HttpStatus.SC_MOVED_TEMPORARILY) {
                this.getLog().debug("Attempt to redirect ");
                throw new HttpStatusException("Attempt to redirect");
            }
            if (pm.getStatusCode() == HttpStatus.SC_OK) {
                final InputStream is = pm.getResponseBodyAsStream();
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
                this.getLog().warn("Downloading from Bugzilla failed. Received: [" + pm.getStatusCode() + "]");
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
