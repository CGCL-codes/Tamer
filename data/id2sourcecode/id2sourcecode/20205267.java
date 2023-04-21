            @Override
            public void run() {
                RandomAccessRecording in = null;
                FileOutputStream out = null;
                try {
                    in = new RandomAccessRecording(RecordingFormat.getAbsolutePaths(new File(recording.getPath()), recording.getParts(), recording.isTs()));
                    out = new FileOutputStream(inputPipe);
                    byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
                    int read;
                    while ((read = in.read(buffer)) > 0) {
                        out.write(buffer, 0, read);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        out.close();
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
