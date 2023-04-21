   private InputStream readHtmlPageFromWebSite(String url)
   {
      try {
         return new URL(url).openStream();
      }
      catch (IOException e) {
         throw new RuntimeException(e);
      }
   }
