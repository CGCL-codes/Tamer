    public static void PostReview(String bookId, String review, String dateRead, List<String> shelves, int rating) throws Exception {
        if (shelves.size() == 0) {
            throw new Exception("Select at least one shelf.");
        }
        if (rating < 1 || rating > 5) {
            throw new Exception("Review rating must be 1-5 stars.");
        }
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost post = new HttpPost("http://www.goodreads.com/review.xml");
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(new BasicNameValuePair("shelf", shelves.get(0)));
        parameters.add(new BasicNameValuePair("review[review]", review));
        parameters.add(new BasicNameValuePair("review[read_at]", dateRead));
        parameters.add(new BasicNameValuePair("book_id", bookId));
        parameters.add(new BasicNameValuePair("review[rating]", Integer.toString(rating)));
        post.setEntity(new UrlEncodedFormEntity(parameters));
        _Consumer.sign(post);
        HttpResponse response = httpClient.execute(post);
        if (response.getStatusLine().getStatusCode() != 201) {
            throw new Exception(response.getStatusLine().toString());
        }
    }
