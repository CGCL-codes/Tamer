    public static SyndFeed readFeed(final SubProgressMonitor subProgressMonitor, final int ticks) throws FeedReaderException {
        subProgressMonitor.beginTask(getMessages().bind("syndFeedRead.monitor.name", getDefault().getControl().getCurrentFeedDescription().getTitle(), getDefault().getControl().getCurrentFeedDescription().getLink()), ticks);
        subProgressMonitor.subTask(getMessages().bind("syndFeedRead.subtask.name", getDefault().getControl().getCurrentFeedDescription().getTitle(), getDefault().getControl().getCurrentFeedDescription().getLink()));
        HttpClient httpClient = null;
        try {
            URI feedUri = getDefault().getControl().getCurrentFeedDescription().getLink().toURI();
            httpClient = createHttpClient(feedUri);
            HttpGet feedMethod = new HttpGet(feedUri);
            HttpResponse response = httpClient.execute(feedMethod);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != OK_STATUS_CODE) {
                IStatus status = getStatuses().create(WARNING, STATUS_CODE_ERROR_HTTP_STATUS, null, statusCode, feedUri);
                throw new FeedReaderException(status);
            }
            BufferedInputStream is = null;
            try {
                is = new BufferedInputStream(response.getEntity().getContent());
                SyndFeedInput syndFeedInput = new SyndFeedInput();
                SyndFeed syndFeed = syndFeedInput.build(new XmlReader(is));
                return syndFeed;
            } catch (FeedException fe) {
                IStatus status = getStatuses().create(ERROR, STATUS_CODE_ERROR_FEED_READING, fe, feedUri);
                throw new FeedReaderException(status);
            } finally {
                if (is != null) {
                    is.close();
                }
            }
        } catch (IOException ioe) {
            IStatus status = getStatuses().create(ERROR, STATUS_CODE_ERROR_FEED_READING, ioe, getDefault().getControl().getCurrentFeedDescription().getLink());
            throw new FeedReaderException(status);
        } catch (URISyntaxException use) {
            IStatus status = getStatuses().create(ERROR, STATUS_CODE_ERROR_FEED_URL, use, getDefault().getControl().getCurrentFeedDescription().getLink());
            throw new FeedReaderException(status);
        } finally {
            if (httpClient != null && httpClient.getConnectionManager() != null) {
                httpClient.getConnectionManager().shutdown();
                subProgressMonitor.worked(ticks);
                subProgressMonitor.done();
            }
        }
    }
