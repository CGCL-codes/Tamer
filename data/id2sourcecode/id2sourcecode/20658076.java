    public void run() {
        try {
            isAlive = true;
            cancel = false;
            strName = "Loading: " + url.toString();
            strStatus = "Opening connection.";
            value = 0;
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
            strStatus = "Creating new document.";
            value = 1;
            Thread.yield();
            HTMLDocument htmlDocument = new HTMLDocument(styleSheet);
            strStatus = "Reading into document.";
            value = 2;
            Thread.yield();
            htmlEditorKit.read(bufferedReader, htmlDocument, 0);
            strStatus = "Closing connection.";
            value = 3;
            Thread.yield();
            bufferedReader.close();
            strStatus = "Setting document base.";
            value = 4;
            Thread.yield();
            htmlDocument.setBase(url);
            strStatus = "Displaying document.";
            value = 5;
            Thread.yield();
            setDocument(htmlDocument);
            strStatus = "Finishing up.";
            value = 6;
            Thread.yield();
            strStatus = "Done";
            isAlive = false;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }
