    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (!isErrorResponse()) {
            if ("comment".equals(localName)) {
                inComment = false;
            } else if ("author".equals(localName)) {
                if (inComment) {
                    currentVideo.getCommentList().get(currentVideo.getCommentList().size() - 1).setAuthor(getCharacters(true));
                } else {
                    currentVideo.setAuthor(getCharacters(true));
                }
            } else if ("id".equals(localName)) {
                currentVideo.setId(getCharacters(true));
            } else if ("title".equals(localName)) {
                currentVideo.setTitle(getCharacters(true));
            } else if ("rating_avg".equals(localName)) {
                currentVideo.setRatingAverage(StringUtil.parseDouble(getCharacters(true)));
            } else if ("rating_count".equals(localName)) {
                currentVideo.setRatingCount(StringUtil.parseInt(getCharacters(true)));
            } else if ("tags".equals(localName)) {
                currentVideo.setTags(getCharacters(true));
            } else if ("description".equals(localName)) {
                currentVideo.setDescription(getCharacters(true));
            } else if ("update_time".equals(localName)) {
                String time = getCharacters(true);
                if (!StringUtil.isNullOrEmpty(time)) {
                    currentVideo.setTimeUpdated(new Date(StringUtil.parseLong(time) * 1000));
                }
            } else if ("upload_time".equals(localName)) {
                String time = getCharacters(true);
                if (!StringUtil.isNullOrEmpty(time)) {
                    currentVideo.setTimeUploaded(new Date(StringUtil.parseLong(time) * 1000));
                }
            } else if ("length_seconds".equals(localName)) {
                currentVideo.setLengthInSeconds(StringUtil.parseInt(getCharacters(true)));
            } else if ("recording_date".equals(localName)) {
                currentVideo.setRecordingDate(getCharacters(true));
            } else if ("recording_location".equals(localName)) {
                currentVideo.setRecordingLocation(getCharacters(true));
            } else if ("recording_country".equals(localName)) {
                currentVideo.setRecordingCountry(getCharacters(true));
            } else if ("text".equals(localName)) {
                currentVideo.getCommentList().get(currentVideo.getCommentList().size() - 1).setText(getCharacters(true));
            } else if ("thumbnail_url".equals(localName)) {
                currentVideo.setThumbnailUrl(getCharacters(true));
            } else if ("embed_status".equals(localName)) {
                currentVideo.setEmbedable("ok".equals(getCharacters(true)));
            } else if ("time".equals(localName)) {
                String time = getCharacters(true);
                if (!StringUtil.isNullOrEmpty(time)) {
                    currentVideo.getCommentList().get(currentVideo.getCommentList().size() - 1).setTime(new Date(StringUtil.parseLong(time)));
                }
            } else if ("channel".equals(localName)) {
                if (currentVideo.getChannelList() == null) {
                    currentVideo.setChannelList(new ArrayList<String>());
                }
                currentVideo.getChannelList().add(getCharacters(true));
            } else {
                super.endElement(uri, localName, qName);
            }
        } else {
            super.endElement(uri, localName, qName);
        }
    }
