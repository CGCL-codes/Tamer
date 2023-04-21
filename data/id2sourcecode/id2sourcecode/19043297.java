    @Override
    public STATUS checkAvailability(String isbn) {
        HttpGet get = null;
        try {
            HttpClient client = new DefaultHttpClient();
            get = new HttpGet(String.format(this.isbnSearchUrl, isbn));
            HttpResponse resp = client.execute(get);
            Scanner s = new Scanner(resp.getEntity().getContent());
            String pattern = s.findWithinHorizon(holdRegex, 0);
            if (pattern != null) {
                MatchResult match = s.match();
                if (match.groupCount() == totalGroups) {
                    if (match.group(numHoldsGroup) == null) {
                        return STATUS.AVAILABLE;
                    }
                    int numHolds = Integer.parseInt(match.group(numHoldsGroup));
                    int numCopies = Integer.parseInt(match.group(numCopiesGroup));
                    if (numHolds < numCopies) {
                        return STATUS.SHORT_WAIT;
                    } else if (numHolds >= numCopies && numHolds <= (2 * numCopies)) {
                        return STATUS.WAIT;
                    } else {
                        return STATUS.LONG_WAIT;
                    }
                }
            }
            return STATUS.NO_MATCH;
        } catch (Exception e) {
            Log.e(this.getClass().getName(), e.getMessage(), e);
            return null;
        } finally {
            if (get != null) {
                get.abort();
            }
        }
    }
