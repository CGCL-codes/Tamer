    private void setTitles(GWTMediaResource res2) {
        GWTAiringDetails airing = (res instanceof GWTMediaFile) ? ((GWTMediaFile) res2).getAiringDetails() : null;
        titles.clear();
        Label title1 = new Label();
        if (airing != null) {
            if (airing.getYear() > 0) {
                title1.setText(res2.getTitle() + " (" + airing.getYear() + ")");
            } else {
                title1.setText(res2.getTitle());
            }
        } else {
            title1.setText(res2.getTitle());
        }
        titles.add(title1);
        titles.setCellHorizontalAlignment(title1, HasHorizontalAlignment.ALIGN_CENTER);
        title1.setStyleName("MediaItem-Title1");
        String epTitle = res2.getMinorTitle();
        if (!StringUtils.isEmpty(epTitle) && !epTitle.equals(res.getTitle())) {
            Label title2 = new Label(epTitle);
            titles.add(title2);
            titles.setCellHorizontalAlignment(title2, HasHorizontalAlignment.ALIGN_CENTER);
            title2.setStyleName("MediaItem-Title2");
        }
        if (res2 instanceof GWTMediaFile && ((GWTMediaFile) res2).getAiringDetails() != null) {
            GWTAiringDetails det = ((GWTMediaFile) res2).getAiringDetails();
            Label title = new Label(DateFormatUtil.formatAiredDate(det.getStartTime()));
            titles.add(title);
            titles.setCellHorizontalAlignment(title, HasHorizontalAlignment.ALIGN_CENTER);
            title.setStyleName("MediaItem-Title2");
            String s = "on channel " + det.getChannel() + " (" + det.getNetwork() + ")";
            title = new Label(s);
            titles.add(title);
            titles.setCellHorizontalAlignment(title, HasHorizontalAlignment.ALIGN_CENTER);
            title.setStyleName("MediaItem-Title2");
        }
    }
