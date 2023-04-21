        @Override
        protected Void doInBackground(final Request... params) {
            final Request request = params[0];
            request.success = false;
            this.request = request;
            clickIntent.putExtra(EXTRA_ID, request.id);
            updateDownloadProgress(null);
            final Application app = (Application) getApplicationContext();
            final HttpGet get = new HttpGet(request.uri.toString());
            final HttpClient client = app.getHttpClientsPool().getHttpClient();
            File destination;
            try {
                destination = new File(new URI(request.destinationUri.toString()));
            } catch (final URISyntaxException e) {
                Log.e(TAG, "Bad URI", e);
                return null;
            }
            InputStream input = null;
            OutputStream output = null;
            try {
                final HttpResponse response = client.execute(get);
                final HttpEntity entity = response.getEntity();
                final long length = entity.getContentLength();
                if (length > 0) {
                    updateDownloadProgress(0f);
                }
                destination.createNewFile();
                output = new FileOutputStream(destination);
                input = new PoolableBufferedInputStream(entity.getContent(), DEFAULT_BUFFER_SIZE, buffersPool);
                int count;
                final byte[] buffer = buffersPool.get(DEFAULT_BUFFER_SIZE);
                int total = 0;
                float prevProgress = 0;
                final float minDelta = 0.05f;
                do {
                    count = input.read(buffer);
                    if (count > 0) {
                        output.write(buffer, 0, count);
                        if (length > 0) {
                            total += count;
                            final float progress = (float) total / length;
                            if (progress - prevProgress >= minDelta) {
                                updateDownloadProgress(progress);
                                prevProgress = progress;
                            }
                        }
                    }
                } while (count != -1);
                request.success = true;
                return null;
            } catch (final Exception e) {
                Log.e(TAG, "Cannot download " + request.uri, e);
                return null;
            } finally {
                try {
                    if (input != null) {
                        input.close();
                    }
                    if (output != null) {
                        output.close();
                    }
                } catch (final IOException e) {
                    Log.e(TAG, "Cannot close the " + (input == null ? "output" : "input") + " stream for " + request.uri, e);
                }
                app.getHttpClientsPool().releaseHttpClient(client);
            }
        }
