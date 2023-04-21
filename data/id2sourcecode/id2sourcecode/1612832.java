    public void fillInputData(InputData inputData) throws TransferFailedException, ResourceDoesNotExistException, AuthorizationException {
        Resource resource = inputData.getResource();
        String visitingUrl = buildUrl(resource.getName());
        try {
            List<String> visitedUrls = new ArrayList<String>();
            for (int redirectCount = 0; redirectCount < MAX_REDIRECTS; redirectCount++) {
                if (visitedUrls.contains(visitingUrl)) {
                    throw new TransferFailedException("Cyclic http redirect detected. Aborting! " + visitingUrl);
                }
                visitedUrls.add(visitingUrl);
                URL url = new URL(visitingUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Accept-Encoding", "gzip");
                if (!useCache) {
                    urlConnection.setRequestProperty("Pragma", "no-cache");
                }
                addHeaders(urlConnection);
                int responseCode = urlConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_FORBIDDEN || responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                    throw new AuthorizationException("Access denied to: " + buildUrl(resource.getName()));
                }
                if (responseCode == HttpURLConnection.HTTP_MOVED_PERM || responseCode == HttpURLConnection.HTTP_MOVED_TEMP) {
                    visitingUrl = urlConnection.getHeaderField("Location");
                    continue;
                }
                InputStream is = urlConnection.getInputStream();
                String contentEncoding = urlConnection.getHeaderField("Content-Encoding");
                boolean isGZipped = contentEncoding != null && "gzip".equalsIgnoreCase(contentEncoding);
                if (isGZipped) {
                    is = new GZIPInputStream(is);
                }
                inputData.setInputStream(is);
                resource.setLastModified(urlConnection.getLastModified());
                resource.setContentLength(urlConnection.getContentLength());
                break;
            }
        } catch (MalformedURLException e) {
            throw new ResourceDoesNotExistException("Invalid repository URL: " + e.getMessage(), e);
        } catch (FileNotFoundException e) {
            throw new ResourceDoesNotExistException("Unable to locate resource in repository", e);
        } catch (IOException e) {
            StringBuilder message = new StringBuilder("Error transferring file: ");
            message.append(e.getMessage());
            message.append(" from " + visitingUrl);
            throw new TransferFailedException(message.toString(), e);
        }
    }
