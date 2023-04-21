    private void updatePanel() {
        clearPanel();
        if (channel == null || ctree == null) {
            return;
        } else if (channel == ROOT_CHANNEL) {
            StringBuffer s = new StringBuffer();
            s.append("<strong>" + rbnb.getServerName() + "</strong><br>");
            s.append("<em>Data Server</em>");
            s.append("<p style=\"font-size: 10px\">" + children + " Data Source");
            if (children == 0 || children > 1) {
                s.append("s");
            }
            s.append("</p>");
            infoTextArea.setText(s.toString());
        } else {
            StringBuffer s = new StringBuffer();
            ChannelTree.Node node = ctree.findNode(channel);
            if (node.getType() == ChannelTree.CHANNEL) {
                Channel channelMetadata = (Channel) rbnb.getChannel(channel);
                String mime = channelMetadata.getMetadata("mime");
                String unit = channelMetadata.getMetadata("units");
                String description = channelMetadata.getMetadata("description");
                double start = Double.parseDouble(channelMetadata.getMetadata("start"));
                double duration = Double.parseDouble(channelMetadata.getMetadata("duration"));
                int size = Integer.parseInt(channelMetadata.getMetadata("size"));
                s.append("<strong>" + channel + "</strong>");
                if (unit != null) {
                    s.append(" (" + unit + ")");
                }
                if (description != null) {
                    s.append("<br>" + description);
                }
                if (mime != null) {
                    s.append("<br>");
                    if (mime.equals("application/octet-stream")) {
                        s.append("<em>Numeric Data</em>");
                        String sampleRate = channelMetadata.getMetadata("samplerate");
                        if (sampleRate != null) {
                            s.append("<br>" + sampleRate + " Hz");
                        }
                    } else if (mime.equals("image/jpeg")) {
                        s.append("<em>JPEG Images</em>");
                        String width = channelMetadata.getMetadata("width");
                        String height = channelMetadata.getMetadata("height");
                        if (width != null && height != null) {
                            s.append("<br>" + width + " x " + height);
                            String sampleRate = channelMetadata.getMetadata("framerate");
                            if (sampleRate != null) {
                                s.append(", " + sampleRate + " fps");
                            }
                        }
                    } else if (mime.equals("video/jpeg")) {
                        s.append("<em>JPEG Video</em>");
                    } else if (mime.equals("text/plain")) {
                        s.append("<em>Text</em>");
                    } else if (mime.startsWith("audio/")) {
                        s.append("<em>Audio</em>");
                    } else {
                        s.append("<em>" + mime + "</em>");
                    }
                }
                s.append("<p style=\"font-size: 10px\">Begins " + DataViewer.formatDateSmart(start) + "<br>");
                s.append("Lasts " + DataViewer.formatSeconds(duration));
                if (size != -1) {
                    s.append("<br>" + DataViewer.formatBytes(size));
                }
                if (mime != null) {
                    if (mime.startsWith("audio/")) {
                        String encoding = channelMetadata.getMetadata("encoding");
                        String channels = channelMetadata.getMetadata("channels");
                        String sampleRate = channelMetadata.getMetadata("samplerate");
                        String sampleSize = channelMetadata.getMetadata("samplesize");
                        String signed = channelMetadata.getMetadata("signed").equals("1") ? "s" : "u";
                        String endian = channelMetadata.getMetadata("endian").equals("1") ? "be" : "le";
                        if (encoding != null && channels != null && sampleRate != null && sampleSize != null && endian != null) {
                            s.append("<br>" + encoding + " (" + Float.parseFloat(sampleRate) / 1000 + "kHz");
                            s.append("/" + channels + "ch");
                            s.append("/" + sampleSize + "b");
                            if (Integer.parseInt(sampleSize) > 1) {
                                s.append("/" + endian);
                            }
                            s.append("/" + signed);
                            s.append(")");
                        }
                    }
                }
                s.append("</p>");
            } else {
                s.append("<strong>" + channel + "</strong><br>");
                if (node.getType() == ChannelTree.SERVER) {
                    s.append("<em>Child Server</em>");
                } else if (node.getType() == ChannelTree.SOURCE) {
                    s.append("<em>Data Source</em>");
                } else {
                    s.append("<em>" + node.getType() + "</em>");
                }
                s.append("<p style=\"font-size: 10px\">" + children + " Children</p>");
            }
            infoTextArea.setText(s.toString());
        }
        infoTextArea.setCaretPosition(0);
    }
