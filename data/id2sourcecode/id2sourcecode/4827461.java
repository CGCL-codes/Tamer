        private int doSubmit(final int itineraryId, final String name, final String email, final String comment) {
            int result = OK;
            String reqString = getString(R.string.feedback_api);
            reqString += "itinerary=" + itineraryId;
            reqString += "&comments=" + URLEncoder.encode(comment);
            reqString += "&name=" + URLEncoder.encode(name);
            reqString += "&email=" + URLEncoder.encode(email);
            HttpUriRequest request = new HttpPut(reqString);
            request.addHeader("User-Agent", BikeRouteConsts.AGENT);
            try {
                final HttpResponse response = new MyHttpClient(Feedback.this).execute(request);
            } catch (Exception e) {
                result = -1;
            }
            return result;
        }
