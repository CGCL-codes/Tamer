    public static void parseVideoDetails(Video video, Document document) {
        Element root = document.getRootElement();
        Element showing = root.element("showing");
        if (showing != null) {
            Element element = showing;
            if (element != null) {
                Element node = element.element("partCount");
                if (node != null) try {
                    video.setPartCount(Integer.parseInt(node.getText()));
                } catch (Exception ex) {
                    log.error("Could not set part count", ex);
                }
                node = element.element("partIndex");
                if (node != null) try {
                    video.setPartIndex(Integer.parseInt(node.getText()));
                } catch (Exception ex) {
                    log.error("Could not set part index", ex);
                }
                Element program = element.element("program");
                if (program != null) {
                    node = program.element("vActor");
                    if (node != null) video.setActors(getElements(node));
                    node = program.element("vAdvisory");
                    if (node != null) video.setAdvisories(getElements(node));
                    node = program.element("vChoreographer");
                    if (node != null) video.setChoreographers(getElements(node));
                    node = program.element("vChoreographer");
                    if (node != null) video.setChoreographers(getElements(node));
                    node = program.element("colorCode");
                    if (node != null) {
                        video.setColor(node.getText());
                        try {
                            video.setColorCode(Integer.parseInt(node.attribute("value").getText()));
                        } catch (Exception ex) {
                            log.error("Could not set color code", ex);
                        }
                    }
                    node = program.element("description");
                    if (node != null) video.setDescription(node.getTextTrim());
                    node = program.element("vDirector");
                    if (node != null) video.setDirectors(getElements(node));
                    node = program.element("episodeNumber");
                    if (node != null) try {
                        video.setEpisodeNumber(Integer.parseInt(node.getTextTrim()));
                    } catch (Exception ex) {
                        log.error("Could not episode number", ex);
                    }
                    node = program.element("episodeTitle");
                    if (node != null) video.setEpisodeTitle(node.getTextTrim());
                    node = program.element("vExecProducer");
                    if (node != null) video.setExecProducers(getElements(node));
                    node = program.element("vProgramGenre");
                    if (node != null) video.setProgramGenre(getElements(node));
                    node = program.element("vGuestStar");
                    if (node != null) video.setGuestStars(getElements(node));
                    node = program.element("vHost");
                    if (node != null) video.setHosts(getElements(node));
                    node = program.element("isEpisode");
                    if (node != null) video.setEpisodic(Boolean.valueOf(node.getTextTrim()));
                    node = program.element("originalAirDate");
                    if (node != null) {
                        ParsePosition pos = new ParsePosition(0);
                        Date date = smTimeDateFormat.parse(node.getTextTrim(), pos);
                        if (date == null) date = new Date(0);
                        video.setOriginalAirDate(date);
                    }
                    node = program.element("vProducer");
                    if (node != null) video.setProducers(getElements(node));
                    Element series = program.element("series");
                    if (series != null) {
                        node = series.element("vSeriesGenre");
                        if (node != null) video.setSeriesGenre(getElements(node));
                        node = series.element("seriesTitle");
                        if (node != null) video.setSeriesTitle(node.getTextTrim());
                    }
                    node = program.element("showType");
                    if (node != null) {
                        video.setShowType(node.getTextTrim());
                        try {
                            video.setShowTypeValue(Integer.parseInt(node.attribute("value").getText()));
                        } catch (Exception ex) {
                            log.error("Could not set showtype value", ex);
                        }
                    }
                    node = program.element("title");
                    if (node != null) video.setTitle(node.getTextTrim());
                    node = program.element("vWriter");
                    if (node != null) video.setWriters(getElements(node));
                }
                Element channel = element.element("channel");
                if (channel != null) {
                    node = channel.element("displayMajorNumber");
                    if (node != null) try {
                        video.setChannelMajorNumber(Integer.parseInt(node.getTextTrim()));
                    } catch (Exception ex) {
                        log.error("Could not set channel major number", ex);
                    }
                    node = channel.element("displayMinorNumber");
                    if (node != null) try {
                        video.setChannelMinorNumber(Integer.parseInt(node.getTextTrim()));
                    } catch (Exception ex) {
                        log.error("Could not set channel minor number", ex);
                    }
                    node = channel.element("callsign");
                    if (node != null) video.setCallsign(node.getTextTrim());
                }
                Element rating = element.element("tvRating");
                if (rating != null) {
                    video.setRating(rating.getTextTrim());
                    try {
                        video.setRatingValue(Integer.parseInt(rating.attribute("value").getText()));
                    } catch (Exception ex) {
                        log.error("Could not set rating value", ex);
                    }
                }
            }
        }
        showing = root.element("vActualShowing");
        if (showing != null) {
            Iterator<Element> iterator = showing.elementIterator("element");
            while (iterator.hasNext()) {
                Element element = iterator.next();
                Element node = null;
                Element channel = element.element("channel");
                if (channel != null) {
                    node = channel.element("displayMajorNumber");
                    if (node != null) try {
                        int value = Integer.parseInt(node.getTextTrim());
                        if (value != video.getChannelMajorNumber()) break;
                        video.setChannelMajorNumber(value);
                    } catch (Exception ex) {
                        log.error("Could not set channel major number", ex);
                    }
                    node = channel.element("displayMinorNumber");
                    if (node != null) try {
                        int value = Integer.parseInt(node.getTextTrim());
                        if (value != video.getChannelMinorNumber()) break;
                        video.setChannelMinorNumber(value);
                    } catch (Exception ex) {
                        log.error("Could not set channel minor number", ex);
                    }
                    node = channel.element("callsign");
                    if (node != null && video.getCallsign() == null) video.setCallsign(node.getTextTrim());
                }
                node = element.element("partCount");
                if (node != null && video.getPartCount() == 0) try {
                    video.setPartCount(Integer.parseInt(node.getText()));
                } catch (Exception ex) {
                    log.error("Could not set part count", ex);
                }
                node = element.element("partIndex");
                if (node != null && video.getPartIndex() == 0) try {
                    video.setPartIndex(Integer.parseInt(node.getText()));
                } catch (Exception ex) {
                    log.error("Could not set part index", ex);
                }
                Element program = element.element("program");
                if (program != null) {
                    node = program.element("vActor");
                    if (node != null && video.getActors() == null) video.setActors(getElements(node));
                    node = program.element("vAdvisory");
                    if (node != null && video.getAdvisories() == null) video.setAdvisories(getElements(node));
                    node = program.element("vChoreographer");
                    if (node != null && video.getChoreographers() == null) video.setChoreographers(getElements(node));
                    node = program.element("vChoreographer");
                    if (node != null && video.getChoreographers() == null) video.setChoreographers(getElements(node));
                    node = program.element("colorCode");
                    if (node != null && video.getColor() == null) {
                        video.setColor(node.getText());
                        try {
                            video.setColorCode(Integer.parseInt(node.attribute("value").getText()));
                        } catch (Exception ex) {
                            log.error("Could not set color code", ex);
                        }
                    }
                    node = program.element("description");
                    if (node != null && video.getDescription() == null) video.setDescription(node.getTextTrim());
                    node = program.element("vDirector");
                    if (node != null && video.getDirectors() == null) video.setDirectors(getElements(node));
                    node = program.element("episodeNumber");
                    if (node != null && video.getEpisodeNumber() != 0) try {
                        video.setEpisodeNumber(Integer.parseInt(node.getTextTrim()));
                    } catch (Exception ex) {
                        log.error("Could not episode number", ex);
                    }
                    node = program.element("episodeTitle");
                    if (node != null && video.getEpisodeTitle() == null) video.setEpisodeTitle(node.getTextTrim());
                    node = program.element("vExecProducer");
                    if (node != null && video.getExecProducers() == null) video.setExecProducers(getElements(node));
                    node = program.element("vProgramGenre");
                    if (node != null && video.getProgramGenre() == null) video.setProgramGenre(getElements(node));
                    node = program.element("vGuestStar");
                    if (node != null && video.getGuestStars() == null) video.setGuestStars(getElements(node));
                    node = program.element("vHost");
                    if (node != null && video.getHosts() == null) video.setHosts(getElements(node));
                    node = program.element("isEpisode");
                    if (node != null && video.getEpisodic() == null) video.setEpisodic(Boolean.valueOf(node.getTextTrim()));
                    node = program.element("originalAirDate");
                    if (node != null && video.getOriginalAirDate() == null) {
                        ParsePosition pos = new ParsePosition(0);
                        Date date = smTimeDateFormat.parse(node.getTextTrim(), pos);
                        if (date == null) date = new Date(0);
                        video.setOriginalAirDate(date);
                    }
                    node = program.element("vProducer");
                    if (node != null && video.getProducers() == null) video.setProducers(getElements(node));
                    Element series = program.element("series");
                    if (series != null && video.getSeriesGenre() == null) {
                        node = series.element("vSeriesGenre");
                        if (node != null) video.setSeriesGenre(getElements(node));
                        node = series.element("seriesTitle");
                        if (node != null) video.setSeriesTitle(node.getTextTrim());
                    }
                    node = program.element("showType");
                    if (node != null && video.getShowType() == null) {
                        video.setShowType(node.getTextTrim());
                        try {
                            video.setShowTypeValue(Integer.parseInt(node.attribute("value").getText()));
                        } catch (Exception ex) {
                            log.error("Could not set showtype value", ex);
                        }
                    }
                    node = program.element("title");
                    if (node != null && video.getTitle() == null) video.setTitle(node.getTextTrim());
                    node = program.element("vWriter");
                    if (node != null && video.getWriters() == null) video.setWriters(getElements(node));
                }
                Element rating = element.element("tvRating");
                if (rating != null && video.getRating() == null) {
                    video.setRating(rating.getTextTrim());
                    try {
                        video.setRatingValue(Integer.parseInt(rating.attribute("value").getText()));
                    } catch (Exception ex) {
                        log.error("Could not set rating value", ex);
                    }
                }
            }
        }
        Element node = root.element("vBookmark");
        if (node != null) {
            StringBuffer buffer = new StringBuffer();
            int counter = 0;
            for (Iterator<Element> iterator = node.elementIterator("element"); iterator.hasNext(); ) {
                Element bookmarkElement = iterator.next();
                if (counter++ > 0) buffer.append(";");
                buffer.append(Tools.getAttribute(bookmarkElement, "time"));
            }
            video.setBookmarks(buffer.toString());
        }
        Element quality = root.element("recordingQuality");
        if (quality != null) {
            video.setRecordingQuality(quality.getTextTrim());
            try {
                video.setRecordingQualityValue(Integer.parseInt(quality.attribute("value").getText()));
            } catch (Exception ex) {
                log.error("Could not set quality value", ex);
            }
        }
        Element time = root.element("startTime");
        if (time != null) {
            ParsePosition pos = new ParsePosition(0);
            Date date = smTimeDateFormat.parse(time.getTextTrim(), pos);
            if (date == null) date = new Date(0);
            video.setStartTime(date);
        }
        time = root.element("stopTime");
        if (time != null) {
            ParsePosition pos = new ParsePosition(0);
            Date date = smTimeDateFormat.parse(time.getTextTrim(), pos);
            if (date == null) date = new Date(0);
            video.setStopTime(date);
        }
        time = root.element("expirationTime");
        if (time != null) {
            ParsePosition pos = new ParsePosition(0);
            Date date = smTimeDateFormat.parse(time.getTextTrim(), pos);
            if (date == null) date = new Date(0);
            video.setExpirationTime(date);
        }
    }
