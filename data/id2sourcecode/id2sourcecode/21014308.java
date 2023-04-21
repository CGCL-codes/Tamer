    public static void AddBookToShelf(String bookId, String shelfName) throws Exception {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost post = new HttpPost("http://www.goodreads.com/shelf/add_to_shelf.xml");
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(new BasicNameValuePair("book_id", bookId));
        parameters.add(new BasicNameValuePair("name", shelfName));
        post.setEntity(new UrlEncodedFormEntity(parameters));
        _Consumer.sign(post);
        HttpResponse response = httpClient.execute(post);
        if (response.getStatusLine().getStatusCode() != 201) {
            throw new Exception(response.getStatusLine().toString());
        }
    }
