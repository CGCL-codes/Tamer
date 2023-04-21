    private void searchSynopsis() {
        String targetReqId = "[ Retriever ] " + value;
        try {
            m_propertyChangeSupport.firePropertyChange("TARGET_REQUEST", "", targetReqId);
            String location = GOODREADS_WEB_PAGE + value;
            GetMethod searchPage = new GetMethod(location);
            m_client.executeMethod(searchPage);
            List<OptionRecord> list = processSearchResponse(searchPage);
            String linkToImage = getLinkToCoverImage(list);
            if (linkToImage != null) {
                GetMethod getImage = new GetMethod(linkToImage);
                m_client.executeMethod(getImage);
                InputStream imageStream = getImage.getResponseBodyAsStream();
                BufferedInputStream bufferedInput = new BufferedInputStream(imageStream);
                BufferedOutputStream bufferedOutput = new BufferedOutputStream(new FileOutputStream(filename));
                byte[] buffer = new byte[1024 * 16];
                int read = 0;
                while ((read = bufferedInput.read(buffer)) != -1) {
                    bufferedOutput.write(buffer, 0, read);
                }
                bufferedOutput.close();
                m_propertyChangeSupport.firePropertyChange(SUCCESS, "", targetReqId);
            } else {
                m_propertyChangeSupport.firePropertyChange(NOT_FOUND, "", value);
            }
        } catch (Exception e) {
            e.printStackTrace();
            m_propertyChangeSupport.firePropertyChange(FAIL, "", targetReqId);
        }
    }
