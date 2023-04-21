    private static void save(AClip c, File f, boolean completeClip) throws IOException {
        try {
            GZIPOutputStream zos = new GZIPOutputStream(new BufferedOutputStream(new FileOutputStream(f)));
            XmlOutputStream os = new XmlOutputStream(zos);
            os.appendSystemTag();
            os.appendCR();
            HashMap<String, String> attr = new HashMap<String, String>();
            attr.put("version", Laoe.version);
            attr.put("fileformat", "one");
            if (completeClip) {
                attr.put("samples", "below");
            } else {
                attr.put("samples", "extern");
            }
            os.appendBeginTag("laoe", attr);
            os.appendCR();
            attr.clear();
            attr.put("name", c.getName());
            attr.put("samplerate", Float.toString(c.getSampleRate()));
            attr.put("samplewidth", Integer.toString(c.getSampleWidth()));
            attr.put("comment", c.getComments());
            os.appendTab(1);
            os.appendBeginTag("clip", attr);
            os.appendCR();
            Audio aud = c.getAudio();
            if (aud != null) {
                attr.clear();
                attr.put("loopStartPointer", Integer.toString(aud.getLoopStartPointer()));
                attr.put("loopEndPointer", Integer.toString(aud.getLoopEndPointer()));
                os.appendTab(2);
                os.appendBeginEndTag("audio", attr);
                os.appendCR();
            }
            AClipPlotter cplt = c.getPlotter();
            if (cplt != null) {
                attr.clear();
                attr.put("bgColor", Integer.toString(c.getPlotter().getBgColor().getRGB(), 16));
                attr.put("gridColor", Integer.toString(c.getPlotter().getGridColor().getRGB(), 16));
                os.appendTab(2);
                os.appendBeginEndTag("clipPlotter", attr);
                os.appendCR();
            }
            for (int i = 0; i < c.getNumberOfLayers(); i++) {
                ALayer l = c.getLayer(i);
                attr.clear();
                attr.put("index", Integer.toString(i));
                attr.put("name", l.getName());
                switch(l.getType()) {
                    case ALayer.PARAMETER_LAYER:
                        attr.put("type", "parameterLayer");
                        break;
                    default:
                        attr.put("type", "audioLayer");
                        break;
                }
                switch(l.getPlotType()) {
                    case ALayer.SAMPLE_CURVE_TYPE:
                        attr.put("plotType", "sampleCurve");
                        break;
                    case ALayer.SPECTROGRAM_TYPE:
                        attr.put("plotType", "spectrogram");
                        break;
                }
                os.appendTab(2);
                os.appendBeginTag("layer", attr);
                os.appendCR();
                for (int j = 0; j < l.getNumberOfChannels(); j++) {
                    AChannel ch = l.getChannel(j);
                    attr.clear();
                    attr.put("index", Integer.toString(j));
                    attr.put("name", ch.getName());
                    attr.put("id", ch.getChangeId());
                    attr.put("audible", String.valueOf(ch.isAudible()));
                    os.appendTab(3);
                    os.appendBeginTag("channel", attr);
                    os.appendCR();
                    attr.clear();
                    attr.put("length", Integer.toString(ch.getSampleLength()));
                    if (!completeClip) {
                        attr.put("location", ch.getChangeId());
                    }
                    os.appendTab(4);
                    os.appendBeginEndTag("samples", attr);
                    os.appendCR();
                    AChannelSelection sel = ch.getSelection();
                    if (sel != null) {
                        attr.clear();
                        attr.put("name", sel.getName());
                        attr.put("offset", Integer.toString(sel.getOffset()));
                        attr.put("length", Integer.toString(sel.getLength()));
                        os.appendTab(4);
                        os.appendBeginTag("selection", attr);
                        os.appendCR();
                        ArrayList<Point> intensity = sel.getIntensityPoints();
                        for (int k = 0; k < intensity.size(); k++) {
                            attr.clear();
                            attr.put("x", Float.toString((float) ((AChannelSelection.Point) intensity.get(k)).x));
                            attr.put("y", Float.toString((float) ((AChannelSelection.Point) intensity.get(k)).y));
                            os.appendTab(5);
                            os.appendBeginEndTag("intensity", attr);
                            os.appendCR();
                        }
                        os.appendTab(4);
                        os.appendEndTag("intensity");
                        os.appendCR();
                    }
                    AChannelMask chMask = ch.getMask();
                    if (chMask != null) {
                        attr.clear();
                        attr.put("name", chMask.getName());
                        os.appendTab(4);
                        os.appendBeginTag("mask", attr);
                        os.appendCR();
                        GEditableSegments seg = chMask.getSegments();
                        for (int k = 0; k < seg.getNumberOfPoints(); k++) {
                            attr.clear();
                            attr.put("x", Float.toString((float) seg.getPointX(k)));
                            attr.put("y", Float.toString((float) seg.getPointY(k)));
                            os.appendTab(5);
                            os.appendBeginEndTag("volumePoint", attr);
                            os.appendCR();
                        }
                        os.appendTab(4);
                        os.appendEndTag("mask");
                        os.appendCR();
                    }
                    AChannelMarker chMarker = ch.getMarker();
                    if (chMask != null) {
                        attr.clear();
                        attr.put("name", chMarker.getName());
                        os.appendTab(4);
                        os.appendBeginTag("markers", attr);
                        os.appendCR();
                        for (int k = 0; k < chMarker.getNumberOfMarkers(); k++) {
                            attr.clear();
                            attr.put("x", Integer.toString(chMarker.getMarkerX(k)));
                            os.appendTab(5);
                            os.appendBeginEndTag("markerPoint", attr);
                            os.appendCR();
                        }
                        os.appendTab(4);
                        os.appendEndTag("markers");
                        os.appendCR();
                    }
                    AChannelPlotter plt = ch.getPlotter();
                    if (plt != null) {
                        attr.clear();
                        attr.put("xOffset", Float.toString((float) plt.getXOffset()));
                        attr.put("xLength", Float.toString((float) plt.getXLength()));
                        attr.put("yOffset", Float.toString(plt.getYOffset()));
                        attr.put("yLength", Float.toString(plt.getYLength()));
                        attr.put("color", Integer.toString(l.getPlotter().getColor().getRGB(), 16));
                        os.appendTab(4);
                        os.appendBeginEndTag("plotter", attr);
                        os.appendCR();
                    }
                    if (ch.getGraphicObjects() != null) {
                        ch.getGraphicObjects().toXmlElement(os);
                    }
                    os.appendTab(3);
                    os.appendEndTag("channel");
                    os.appendCR();
                }
                os.appendTab(2);
                os.appendEndTag("layer");
                os.appendCR();
            }
            os.appendTab(1);
            os.appendEndTag("clip");
            os.appendCR();
            os.appendEndTag("laoe");
            if (completeClip) {
                ObjectOutputStream oos = new ObjectOutputStream(zos);
                LProgressViewer.getInstance().entrySubProgress(0.3, "clip " + f.getName());
                for (int i = 0; i < c.getNumberOfLayers(); i++) {
                    if (LProgressViewer.getInstance().setProgress(1.0 * (i + 1) / c.getNumberOfLayers())) return;
                    ALayer l = c.getLayer(i);
                    LProgressViewer.getInstance().entrySubProgress(0.3, "layer " + i);
                    for (int j = 0; j < l.getNumberOfChannels(); j++) {
                        if (LProgressViewer.getInstance().setProgress(1.0 * (j + 1) / l.getNumberOfChannels())) return;
                        AChannel ch = l.getChannel(j);
                        LProgressViewer.getInstance().entrySubProgress(0.3, "channel " + j);
                        saveSamples(ch, oos);
                        LProgressViewer.getInstance().exitSubProgress();
                    }
                    LProgressViewer.getInstance().exitSubProgress();
                }
                oos.close();
                LProgressViewer.getInstance().exitSubProgress();
            } else {
                os.appendCR();
                os.close();
            }
        } catch (IOException ioe) {
            Debug.printStackTrace(5, ioe);
        }
    }
