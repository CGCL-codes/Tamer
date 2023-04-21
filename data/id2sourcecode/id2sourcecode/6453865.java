    private void extractSingleRecording(final File outputDirectory, final ProgressMonitor monitor, final Recording current) {
        parent.setSelectedEnry(current);
        LOGGER.fine("creating Output-file and writing");
        String outputFileName = current.getOutputFile();
        int[] extractMarkers = current.getMarkersToExtract();
        try {
            List<Long> clusterList = current.getClusterList();
            ByteBuffer b = current.getInfoBlock();
            setTotalClusterCount(current.getExtractSize());
            boolean[] lut = new boolean[MAX_STREAMS];
            int[] map = new int[MAX_STREAMS];
            int c = 0;
            for (int j = 0; j < extractMarkers.length; j++) {
                lut[extractMarkers[j]] = true;
                map[extractMarkers[j]] = c++;
                LOGGER.fine("Marker " + j + ", extract: true");
            }
            FileChannel[] outputChannels = new FileChannel[extractMarkers.length];
            for (int j = 0; j < outputChannels.length; j++) {
                String fileExt = "";
                fileExt = current.getMediaportExtensionForStream(extractMarkers[j]);
                File outputFile = new File(outputDirectory, outputFileName + fileExt);
                if (outputFile.createNewFile()) {
                    LOGGER.fine("created new file: " + outputFile.getAbsolutePath());
                } else {
                    LOGGER.fine("file already existed: " + outputFile.getAbsolutePath());
                }
                FileOutputStream fos = new FileOutputStream(outputFile);
                outputChannels[j] = fos.getChannel();
            }
            FileChannel inputChannel = getImg().getImageChannel();
            LOGGER.fine("creating Cluster List, length: " + clusterList.size());
            InfoBlockEntry entry = new InfoBlockEntry();
            Long cl;
            int fileNr;
            do {
                entry.parseData(b);
                switch(entry.getType()) {
                    case CLUSTER_LABEL1:
                    case CLUSTER_LABEL2:
                    case CLUSTER_LABEL3:
                        if ((entry.getStreamID() < lut.length) && (lut[entry.getStreamID()] || current.isHD())) {
                            cl = clusterList.get(entry.getData());
                            if (current.isHD()) {
                                fileNr = 0;
                            } else {
                                fileNr = map[entry.getStreamID()];
                            }
                            LOGGER.finer("writing Cluster: " + cl);
                            inputbuff.rewind();
                            outputbuff.rewind();
                            inputChannel.read(inputbuff, (long) cl * AbstractPVRFileSystem.CLUSTER_SIZE + getImg().getOffset());
                            inputbuff.rewind();
                            if (getImg().getImageVersion().equals(FileSystemVersion.TSD_V1)) {
                                while (inputbuff.remaining() > 0) {
                                    outputbuff.putShort(inputbuff.getShort());
                                }
                                outputbuff.rewind();
                                if (outputChannels[fileNr].write(outputbuff) != AbstractPVRFileSystem.CLUSTER_SIZE) {
                                    LOGGER.severe("not enough bytes written!");
                                }
                            } else {
                                if (outputChannels[fileNr].write(inputbuff) != AbstractPVRFileSystem.CLUSTER_SIZE) {
                                    LOGGER.severe("not enough bytes written!");
                                }
                                if (entry.getType() == CLUSTER_LABEL2) {
                                    long newpos = outputChannels[fileNr].position() - CLUSTER_SLACK;
                                    outputChannels[fileNr].position(newpos);
                                }
                            }
                            monitor.setProgress(progress());
                        }
                        break;
                    default:
                        break;
                }
                while (getpause() && !getCancel()) {
                    pause();
                }
                yield();
            } while ((entry.getType() != END_LABEL) && (b.remaining() >= InfoBlockEntry.MIN_ENTRY_LEN));
            LOGGER.fine("Closing output-Files");
            for (int j = 0; j < outputChannels.length; j++) {
                outputChannels[j].close();
            }
        } catch (Exception ioe) {
            ErrorMessage.showExceptionMessage(parent, ioe);
            LOGGER.log(Level.SEVERE, "Error during Extraction: ", ioe);
        }
    }
