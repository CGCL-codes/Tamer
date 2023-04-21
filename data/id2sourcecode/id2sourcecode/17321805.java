    private synchronized void exportData(List numericChannels, File dataFile, List multimediaChannels, File dataDirectory, double startTime, double endTime, ProgressListener listener) {
        if (listener == null) {
            listener = new ProgressListener() {

                public void postProgress(double progress) {
                }

                public void postCompletion() {
                }

                public void postError(String errorMessage) {
                }
            };
        }
        if (numericChannels == null) {
            numericChannels = new ArrayList();
        }
        if (multimediaChannels == null) {
            multimediaChannels = new ArrayList();
        }
        if (numericChannels.size() == 0 && multimediaChannels.size() == 0) {
            listener.postError("No channels were specified.");
            return;
        }
        if (numericChannels.size() > 0) {
            if (dataFile == null) {
                listener.postError("The data file is not specified.");
                return;
            } else if (dataFile.isDirectory()) {
                listener.postError("The data file is a directory.");
                return;
            }
        }
        if (multimediaChannels.size() > 0) {
            if (dataDirectory == null) {
                listener.postError("Data directory is invalid.");
                return;
            } else if (!dataDirectory.isDirectory()) {
                listener.postError("the data directory isn't a directory.");
                return;
            }
        }
        if (startTime > endTime) {
            listener.postError("The start time must be greater than the end time");
            return;
        }
        double time = startTime;
        try {
            Sink sink = new Sink();
            sink.OpenRBNBConnection(rbnbHostName + ":" + rbnbPortNumber, "RDVExport");
            ChannelMap cmap = new ChannelMap();
            for (int i = 0; i < numericChannels.size(); i++) {
                cmap.Add((String) numericChannels.get(i));
            }
            for (int i = 0; i < multimediaChannels.size(); i++) {
                cmap.Add((String) multimediaChannels.get(i));
            }
            BufferedWriter fileWriter = null;
            if (numericChannels.size() > 0) {
                fileWriter = new BufferedWriter(new FileWriter(dataFile));
            }
            if (fileWriter != null) {
                fileWriter.write("Start time: " + RBNBUtilities.secondsToISO8601(startTime) + "\r\n");
                fileWriter.write("End time: " + RBNBUtilities.secondsToISO8601(endTime) + "\r\n");
                fileWriter.write("Export time: " + RBNBUtilities.millisecondsToISO8601(System.currentTimeMillis()) + "\r\n");
                fileWriter.write("\r\n");
                fileWriter.write("Time\t");
                for (int i = 0; i < numericChannels.size(); i++) {
                    String channel = (String) numericChannels.get(i);
                    String[] channelParts = channel.split("/");
                    fileWriter.write(channelParts[channelParts.length - 1]);
                    if (i != numericChannels.size() - 1) {
                        fileWriter.write('\t');
                    }
                }
                fileWriter.write("\r\n");
                sink.RequestRegistration(cmap);
                ChannelMap rmap = sink.Fetch(-1);
                fileWriter.write("Seconds\t");
                for (int i = 0; i < numericChannels.size(); i++) {
                    String channel = (String) numericChannels.get(i);
                    String unit = null;
                    int index = rmap.GetIndex(channel);
                    String[] metadata = rmap.GetUserInfo(index).split("\t|,");
                    for (int j = 0; j < metadata.length; j++) {
                        String[] elements = metadata[j].split("=");
                        if (elements.length == 2 && elements[0].equals("units")) {
                            unit = elements[1];
                            break;
                        }
                    }
                    if (unit != null) {
                        fileWriter.write(unit);
                    }
                    fileWriter.write('\t');
                }
                fileWriter.write("\r\n");
            }
            listener.postProgress(0);
            double dataStartTime = -1;
            while (time < endTime && !cancelExport) {
                double duration = 2;
                if (time + duration > endTime) {
                    duration = endTime - time;
                }
                sink.Request(cmap, time, duration, "absolute");
                ChannelMap dmap = sink.Fetch(-1);
                ArrayList<Sample> samples = new ArrayList<Sample>();
                for (int i = 0; i < numericChannels.size(); i++) {
                    String channel = (String) numericChannels.get(i);
                    int index = dmap.GetIndex(channel);
                    if (index != -1) {
                        int type = dmap.GetType(index);
                        double[] times = dmap.GetTimes(index);
                        for (int j = 0; j < times.length; j++) {
                            Sample sample;
                            if (times[j] > startTime && times[j] <= time) {
                                continue;
                            }
                            switch(type) {
                                case ChannelMap.TYPE_INT32:
                                    sample = new Sample(channel, dmap.GetDataAsInt32(index)[j], times[j]);
                                    break;
                                case ChannelMap.TYPE_INT64:
                                    sample = new Sample(channel, dmap.GetDataAsInt64(index)[j], times[j]);
                                    break;
                                case ChannelMap.TYPE_FLOAT32:
                                    sample = new Sample(channel, dmap.GetDataAsFloat32(index)[j], times[j]);
                                    break;
                                case ChannelMap.TYPE_FLOAT64:
                                    sample = new Sample(channel, dmap.GetDataAsFloat64(index)[j], times[j]);
                                    break;
                                default:
                                    sample = new Sample(channel, "", times[j]);
                            }
                            samples.add(sample);
                        }
                    }
                }
                Collections.sort(samples, new SampleTimeComparator());
                Iterator it = samples.iterator();
                boolean end = false;
                Sample s = null;
                if (it.hasNext()) {
                    s = (Sample) it.next();
                } else {
                    end = true;
                }
                while (!end) {
                    double t = s.getTime();
                    if (dataStartTime == -1) {
                        dataStartTime = t;
                    }
                    fileWriter.write(Double.toString(t - dataStartTime) + "\t");
                    for (int i = 0; i < numericChannels.size(); i++) {
                        String c = (String) numericChannels.get(i);
                        if (c.equals(s.getChannel()) && t == s.getTime()) {
                            fileWriter.write(s.getData());
                            if (it.hasNext()) {
                                s = (Sample) it.next();
                            } else {
                                fileWriter.write("\r\n");
                                end = true;
                                break;
                            }
                        }
                        if (i == numericChannels.size() - 1) {
                            fileWriter.write("\r\n");
                        } else {
                            fileWriter.write('\t');
                        }
                    }
                }
                String videoChannel, fileName;
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH.mm.ss.SSS'Z'");
                dateFormat.setTimeZone(new SimpleTimeZone(0, "UTC"));
                for (int i = 0; i < multimediaChannels.size(); i++) {
                    videoChannel = (String) multimediaChannels.get(i);
                    int index = dmap.GetIndex(videoChannel);
                    if (index != -1) {
                        int type = dmap.GetType(index);
                        if (type == ChannelMap.TYPE_BYTEARRAY) {
                            double[] times = dmap.GetTimes(index);
                            byte[][] datas = dmap.GetDataAsByteArray(index);
                            for (int j = 0; j < times.length; j++) {
                                byte[] data = datas[j];
                                try {
                                    fileName = videoChannel;
                                    if (fileName.endsWith(".jpg")) {
                                        fileName = fileName.substring(0, fileName.length() - 4);
                                    }
                                    if (fileName.endsWith("/video")) {
                                        fileName = fileName.substring(0, fileName.length() - 6);
                                    }
                                    fileName = fileName.replace("/", "-");
                                    String timeStamp = dateFormat.format(new Date((long) (times[j] * 1000)));
                                    fileName += "_" + timeStamp + ".jpg";
                                    File outputFile = new File(dataDirectory, fileName);
                                    FileOutputStream out = new FileOutputStream(outputFile);
                                    out.write(data);
                                    out.close();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
                time += duration;
                listener.postProgress((time - startTime) / (endTime - startTime));
            }
            if (fileWriter != null) {
                fileWriter.close();
            }
            sink.CloseRBNBConnection();
            if (cancelExport) {
                dataFile.delete();
                listener.postError("Export canceled.");
            } else {
                listener.postCompletion();
            }
        } catch (Exception e) {
            e.printStackTrace();
            listener.postError("Error exporting data: " + e.getMessage());
            return;
        }
        cancelExport = false;
        return;
    }
