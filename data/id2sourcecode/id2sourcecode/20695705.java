    public static boolean load(AClip c, File f) throws IOException {
        ALayer l = null;
        AChannel ch = null;
        AChannelSelection chSel = null;
        AChannelMask chMask = null;
        AChannelMarker chMarker = null;
        int loopStartPointer = 0;
        int loopEndPointer = 0;
        try {
            GZIPInputStream zis = new GZIPInputStream(new BufferedInputStream(new FileInputStream(f)));
            XmlInputStream is = new XmlInputStream(zis);
            boolean xmlHeader = true;
            boolean samplesBelow = true;
            while (xmlHeader) {
                int t = is.read();
                switch(t) {
                    case XmlInputStream.SYSTEM_TAG:
                        break;
                    case XmlInputStream.BEGIN_TAG:
                        if (is.getTagName().equals("laoe")) {
                            if (is.getAttribute("samples").equals("below")) {
                                samplesBelow = true;
                            } else {
                                samplesBelow = false;
                            }
                        } else if (is.getTagName().equals("clip")) {
                            c.removeAll();
                            c.setName(is.getAttribute("name"));
                            c.setSampleRate(Float.parseFloat(is.getAttribute("samplerate")));
                            c.setSampleWidth(Integer.parseInt(is.getAttribute("samplewidth")));
                            c.setComments(is.getAttribute("comment"));
                        } else if (is.getTagName().equals("layer")) {
                            l = new ALayer();
                            l.setName(is.getAttribute("name"));
                            String ty = is.getAttribute("type");
                            if (ty.equals("audioLayer")) {
                                l.setType(ALayer.AUDIO_LAYER);
                            } else if (ty.equals("parameterLayer")) {
                                l.setType(ALayer.PARAMETER_LAYER);
                            }
                            String pt = is.getAttribute("plotType");
                            if (pt != null) {
                                if (pt.equals("sampleCurve")) {
                                    l.setPlotType(ALayer.SAMPLE_CURVE_TYPE);
                                } else if (pt.equals("spectrogram")) {
                                    l.setPlotType(ALayer.SPECTROGRAM_TYPE);
                                }
                            }
                            c.add(l);
                        } else if (is.getTagName().equals("channel")) {
                            ch = new AChannel();
                            if (!samplesBelow) {
                                ch.setChangeId(is.getAttribute("id"));
                            }
                            ch.setName(is.getAttribute("name"));
                            ch.setAudible(is.getAttribute("audible").equals("true"));
                            l.add(ch);
                        } else if (is.getTagName().equals("selection")) {
                            chSel = new AChannelSelection();
                            ch.setSelection(chSel);
                            chSel.setChannel(ch);
                            chSel.setName(is.getAttribute("name"));
                            chSel.setOffset(Integer.parseInt(is.getAttribute("offset")));
                            chSel.setLength(Integer.parseInt(is.getAttribute("length")));
                        } else if (is.getTagName().equals("mask")) {
                            chMask = ch.getMask();
                            chMask.setName(is.getAttribute("name"));
                        } else if (is.getTagName().equals("markers")) {
                            chMarker = ch.getMarker();
                            chMarker.setName(is.getAttribute("name"));
                        } else if (is.getTagName().equals("graphicObjects")) {
                            ch.getGraphicObjects().fromXmlElement(is);
                        }
                        break;
                    case XmlInputStream.END_TAG:
                        if (is.getTagName().equals("laoe")) {
                            xmlHeader = false;
                        }
                        break;
                    case XmlInputStream.BEGIN_END_TAG:
                        if (is.getTagName().equals("selection")) {
                            chSel = new AChannelSelection();
                            ch.setSelection(chSel);
                            chSel.setChannel(ch);
                            chSel.setName(is.getAttribute("name"));
                            chSel.setOffset(Integer.parseInt(is.getAttribute("offset")));
                            chSel.setLength(Integer.parseInt(is.getAttribute("length")));
                        } else if (is.getTagName().equals("plotter")) {
                            ch.getPlotter().setXRange(Float.parseFloat(is.getAttribute("xOffset")), Float.parseFloat(is.getAttribute("xLength")));
                            ch.getPlotter().setYRange(Float.parseFloat(is.getAttribute("yOffset")), Float.parseFloat(is.getAttribute("yLength")));
                            if (is.containsAttribute("color")) {
                                l.getPlotter().setColor(new Color(Integer.parseInt(is.getAttribute("color"), 16)));
                            }
                        } else if (is.getTagName().equals("clipPlotter")) {
                            if (is.containsAttribute("bgColor")) {
                                c.getPlotter().setBgColor(new Color(Integer.parseInt(is.getAttribute("bgColor"), 16)));
                            }
                            if (is.containsAttribute("gridColor")) {
                                c.getPlotter().setGridColor(new Color(Integer.parseInt(is.getAttribute("gridColor"), 16)));
                            }
                        } else if (is.getTagName().equals("audio")) {
                            loopEndPointer = Integer.parseInt(is.getAttribute("loopEndPointer"));
                            loopStartPointer = Integer.parseInt(is.getAttribute("loopStartPointer"));
                        } else if (is.getTagName().equals("samples")) {
                            ch.setSamples(new MMArray(Integer.parseInt(is.getAttribute("length")), 0));
                        } else if (is.getTagName().equals("intensity")) {
                            float x = Float.parseFloat(is.getAttribute("x"));
                            float y = Float.parseFloat(is.getAttribute("y"));
                            chSel.addIntensityPoint(x, y);
                        } else if (is.getTagName().equals("volumePoint")) {
                            float x = Float.parseFloat(is.getAttribute("x"));
                            float y = Float.parseFloat(is.getAttribute("y"));
                            chMask.getSegments().addPoint(x, y);
                        } else if (is.getTagName().equals("markerPoint")) {
                            int x = Integer.parseInt(is.getAttribute("x"));
                            chMarker.addMarker(x);
                        }
                        break;
                    case XmlInputStream.DATA_CHUNK:
                        break;
                    case XmlInputStream.EOF:
                        xmlHeader = false;
                        break;
                }
            }
            c.getAudio().setLoopEndPointer(loopEndPointer);
            c.getAudio().setLoopStartPointer(loopStartPointer);
            c.getAudio().setEncoding(AudioFormat.Encoding.PCM_SIGNED);
            c.getAudio().setFileType(Audio.fileTypeLaoe);
            if (samplesBelow) {
                ObjectInputStream ois = new ObjectInputStream(zis);
                LProgressViewer.getInstance().entrySubProgress(0.9, "clip " + f.getName());
                for (int i = 0; i < c.getNumberOfLayers(); i++) {
                    if (LProgressViewer.getInstance().setProgress(1.0 * (i + 1) / c.getNumberOfLayers())) return false;
                    l = c.getLayer(i);
                    LProgressViewer.getInstance().entrySubProgress(0.3, "layer " + i);
                    for (int j = 0; j < l.getNumberOfChannels(); j++) {
                        if (LProgressViewer.getInstance().setProgress(1.0 * (j + 1) / l.getNumberOfChannels())) return false;
                        ch = l.getChannel(j);
                        LProgressViewer.getInstance().entrySubProgress(0.3, "channel " + j);
                        ch.setSamples(loadSamples(ois));
                        LProgressViewer.getInstance().exitSubProgress();
                    }
                    LProgressViewer.getInstance().exitSubProgress();
                }
                LProgressViewer.getInstance().exitSubProgress();
                ois.close();
            } else {
                is.close();
            }
        } catch (IOException ioe) {
            Debug.printStackTrace(5, ioe);
        }
        return true;
    }
