    public void downloadAudioIncrement(String mediaUrl) throws IOException {
        final String TAG = "downloadAudioIncrement";
        Debug.startMethodTracing("mynpr");
        int bufsizeForDownload = 1024;
        int bufsizeForfile = 64 * 1024;
        stream = new BufferedInputStream(urlConn.getInputStream(), bufsizeForDownload);
        if (stream == null) {
            Log.e(TAG, "Unable to create InputStream for mediaUrl: " + mediaUrl);
            sendMessage(PlayListTab.TROUBLEWITHAUDIO);
            stop();
        }
        Log.d(TAG, "File name: " + downloadingMediaFile);
        BufferedOutputStream bout = new BufferedOutputStream(new FileOutputStream(downloadingMediaFile), bufsizeForfile);
        byte buf[] = new byte[bufsizeForDownload];
        int totalBytesRead = 0, incrementalBytesRead = 0, numread = 0;
        if (stopping == true) {
            stream = null;
            Log.d(TAG, "null out stream ");
        }
        do {
            if (bout == null) {
                counter++;
                Log.d(TAG, "FileOutputStream is null, Create new one: " + DOWNFILE + counter);
                downloadingMediaFile = new File(context.getCacheDir(), DOWNFILE + counter);
                downloadingMediaFile.deleteOnExit();
                bout = new BufferedOutputStream(new FileOutputStream(downloadingMediaFile), bufsizeForfile);
            }
            try {
                numread = stream.read(buf);
            } catch (IOException e) {
                Log.e(TAG, e.toString());
                Log.d(TAG, "Bad read. Let's quit.");
                sendMessage(PlayListTab.TROUBLEWITHAUDIO);
                stop();
            } catch (NullPointerException e) {
                break;
            }
            if (numread < 0) {
                Log.e(TAG, "Bad read from stream. We got some number less than 0: " + numread + " Let's quit");
                sendMessage(PlayListTab.TROUBLEWITHAUDIO);
                stop();
                break;
            } else if (numread >= 1) {
                bout.write(buf, 0, numread);
                totalBytesRead += numread;
                incrementalBytesRead += numread;
                totalKbRead = totalBytesRead / 1000;
            }
            if (totalKbRead >= INTIAL_KB_BUFFER && stopping != true) {
                sendMessage(PlayListTab.CHECKRIORITY);
                Log.v(TAG, "Reached Buffer amount we want: " + "totalKbRead: " + totalKbRead + " INTIAL_KB_BUFFER: " + INTIAL_KB_BUFFER);
                bout.flush();
                bout.close();
                bout = null;
                setupplayer(downloadingMediaFile);
                totalBytesRead = 0;
            }
        } while (stream != null);
        Log.d(TAG, "Done with streaming");
        Debug.stopMethodTracing();
    }
