    public static void UpdateReview(String reviewId, String review, String dateRead, List<String> shelves, int rating) throws Exception {
        if (shelves.size() == 0) {
            throw new Exception("Select at least one shelf.");
        }
        if (rating < 1 || rating > 5) {
            throw new Exception("Review rating must be 1-5 stars.");
        }
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost post = new HttpPost("http://www.goodreads.com/review/" + reviewId + ".xml");
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        StringBuilder shelvesString = new StringBuilder();
        if (shelves.size() > 0) {
            shelvesString.append(shelves.get(0));
        }
        for (int i = 1; i < shelves.size(); i++) {
            shelvesString.append("|" + shelves.get(i));
        }
        parameters.add(new BasicNameValuePair("shelf", shelvesString.toString()));
        parameters.add(new BasicNameValuePair("review[review]", review));
        parameters.add(new BasicNameValuePair("review[read_at]", dateRead));
        parameters.add(new BasicNameValuePair("review[rating]", Integer.toString(rating)));
        post.setEntity(new UrlEncodedFormEntity(parameters));
        _Consumer.sign(post);
        HttpResponse response = httpClient.execute(post);
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode < 200 || statusCode > 299) {
            throw new Exception(response.getStatusLine().toString());
        }
    }
