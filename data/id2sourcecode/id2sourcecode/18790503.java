        public OggPlayer(String filepath, MusicFactory caller) {
            this.caller = caller;
            pUrl = MusicFactory.class.getResource(filepath);
            try {
                urlConnection = pUrl.openConnection();
            } catch (UnknownServiceException exception) {
                System.err.println("The protocol does not support input.");
            } catch (IOException exception) {
                System.err.println("An I/O error occoured while trying create the " + "URL connection.");
            }
            if (urlConnection != null) {
                try {
                    inputStream = urlConnection.getInputStream();
                } catch (IOException exception) {
                    System.err.println("An I/O error occoured while trying to get an " + "input stream from the URL.");
                    System.err.println(exception);
                }
            }
        }
