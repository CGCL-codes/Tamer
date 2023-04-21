    public static void writeSoundFile(File tempSoundFile, IProgress progress, ByteBuffer formatChunk, ByteBuffer dataChunk, ByteBuffer riffChunk) {
        try {
            FileOutputStream fout = new FileOutputStream(tempSoundFile);
            FileChannel out = fout.getChannel();
            riffChunk.flip();
            out.write(riffChunk);
            formatChunk.flip();
            out.write(formatChunk);
            dataChunk.position(dataChunk.limit());
            dataChunk.flip();
            out.write(dataChunk);
            out.close();
            fout.close();
            progress.setProgressNote("Sound computation complete");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            Dialogs.showErrorDialog(null, "Error " + e.getLocalizedMessage());
            e.printStackTrace();
        }
    }
