    public boolean hasChangeWhenEdit(Baike baike) {
        HashCodeBuilder oldHashCode = new HashCodeBuilder();
        HashCodeBuilder newHashCode = new HashCodeBuilder();
        String name = baike.getName();
        if (StringUtils.isNotEmpty(name)) {
            oldHashCode.append(this.getName());
            newHashCode.append(name);
        }
        String enName = baike.getEnName();
        if (StringUtils.isNotEmpty(enName)) {
            oldHashCode.append(this.getEnName());
            newHashCode.append(enName);
        }
        String alias = baike.getAlias();
        if (StringUtils.isNotEmpty(alias)) {
            oldHashCode.append(this.getAlias());
            newHashCode.append(alias);
        }
        String foreignAlias = baike.getForeignAlias();
        if (StringUtils.isNotEmpty(foreignAlias)) {
            oldHashCode.append(this.getForeignAlias());
            newHashCode.append(foreignAlias);
        }
        String directors = baike.getDirectors();
        if (StringUtils.isNotEmpty(directors)) {
            oldHashCode.append(this.getDirectors());
            newHashCode.append(directors);
        }
        String writers = baike.getWriters();
        if (StringUtils.isNotEmpty(writers)) {
            oldHashCode.append(this.getWriters());
            newHashCode.append(writers);
        }
        String stars = baike.getStars();
        if (StringUtils.isNotEmpty(stars)) {
            oldHashCode.append(this.getStars());
            newHashCode.append(stars);
        }
        String releaseDate = baike.getReleaseDate();
        if (StringUtils.isNotEmpty(releaseDate)) {
            oldHashCode.append(this.getReleaseDate());
            newHashCode.append(releaseDate);
        }
        String releaseArea = baike.getReleaseArea();
        if (StringUtils.isNotEmpty(releaseArea)) {
            oldHashCode.append(this.getReleaseArea());
            newHashCode.append(releaseArea);
        }
        String language = baike.getLanguage();
        if (StringUtils.isNotEmpty(language)) {
            oldHashCode.append(this.getLanguage());
            newHashCode.append(language);
        }
        String length = baike.getLength();
        if (StringUtils.isNotEmpty(length)) {
            oldHashCode.append(this.getLength());
            newHashCode.append(length);
        }
        String playTime = baike.getPlayTime();
        if (StringUtils.isNotEmpty(playTime)) {
            oldHashCode.append(this.getPlayTime());
            newHashCode.append(playTime);
        }
        String summary = baike.getSummary();
        if (StringUtils.isNotEmpty(summary)) {
            oldHashCode.append(this.getSummary());
            newHashCode.append(summary);
        }
        String synopsis = baike.getSynopsis();
        if (StringUtils.isNotEmpty(synopsis)) {
            oldHashCode.append(this.getSynopsis());
            newHashCode.append(synopsis);
        }
        String behindTheScenes = baike.getBehindTheScenes();
        if (StringUtils.isNotEmpty(behindTheScenes)) {
            oldHashCode.append(this.getBehindTheScenes());
            newHashCode.append(behindTheScenes);
        }
        String feature = baike.getFeature();
        if (StringUtils.isNotEmpty(feature)) {
            oldHashCode.append(this.getFeature());
            newHashCode.append(feature);
        }
        String filmReview = baike.getFilmReview();
        if (StringUtils.isNotEmpty(filmReview)) {
            oldHashCode.append(this.getFilmReview());
            newHashCode.append(filmReview);
        }
        String tags = baike.getTags();
        if (StringUtils.isNotEmpty(tags)) {
            oldHashCode.append(this.getTags());
            newHashCode.append(tags);
        }
        String channelCode = baike.getChannelCode();
        if (StringUtils.isNotEmpty(channelCode)) {
            oldHashCode.append(this.getChannelCode());
            newHashCode.append(channelCode);
        }
        String channelName = baike.getChannelName();
        if (StringUtils.isNotEmpty(channelName)) {
            oldHashCode.append(this.getChannelName());
            newHashCode.append(channelName);
        }
        String year = baike.getYear();
        if (StringUtils.isNotEmpty(year)) {
            oldHashCode.append(this.getYear());
            newHashCode.append(year);
        }
        String genre = baike.getGenre();
        if (StringUtils.isNotEmpty(genre)) {
            oldHashCode.append(this.getGenre());
            newHashCode.append(genre);
        }
        String area = baike.getArea();
        if (StringUtils.isNotEmpty(area)) {
            oldHashCode.append(this.getArea());
            newHashCode.append(area);
        }
        float rating = baike.getRating();
        if (rating > 0.0f) {
            oldHashCode.append(this.getRating());
            newHashCode.append(rating);
        }
        long ratingCount = baike.getRatingCount();
        if (ratingCount > 0) {
            oldHashCode.append(this.getRatingCount());
            newHashCode.append(ratingCount);
        }
        long favoritedCount = baike.getFavoritedCount();
        if (favoritedCount > 0) {
            oldHashCode.append(this.getFavoritedCount());
            newHashCode.append(favoritedCount);
        }
        long wantToSeeCount = baike.getWantToSeeCount();
        if (wantToSeeCount > 0) {
            oldHashCode.append(this.getWantToSeeCount());
            newHashCode.append(wantToSeeCount);
        }
        String oriLogoUrl = baike.getOriLogoUrl();
        if (StringUtils.isNotEmpty(oriLogoUrl)) {
            oldHashCode.append(this.getOriLogoUrl());
            newHashCode.append(oriLogoUrl);
        }
        String oriLogoUrlOne = baike.getOriLogoUrlOne();
        if (StringUtils.isNotEmpty(oriLogoUrlOne)) {
            oldHashCode.append(this.getOriLogoUrlOne());
            newHashCode.append(oriLogoUrlOne);
        }
        String oriLogoUrlTwo = baike.getOriLogoUrlTwo();
        if (StringUtils.isNotEmpty(oriLogoUrlTwo)) {
            oldHashCode.append(this.getOriLogoUrlTwo());
            newHashCode.append(oriLogoUrlTwo);
        }
        String oriLogoUrlThree = baike.getOriLogoUrlThree();
        if (StringUtils.isNotEmpty(oriLogoUrlThree)) {
            oldHashCode.append(this.getOriLogoUrlThree());
            newHashCode.append(oriLogoUrlThree);
        }
        String reviewStatus = baike.getReviewStatus();
        if (StringUtils.isNotEmpty(reviewStatus)) {
            oldHashCode.append(this.getReviewStatus());
            newHashCode.append(reviewStatus);
        }
        String remarks = baike.getRemarks();
        if (StringUtils.isNotEmpty(remarks)) {
            oldHashCode.append(this.getRemarks());
            newHashCode.append(remarks);
        }
        String other1 = baike.getOther1();
        if (StringUtils.isNotEmpty(other1)) {
            oldHashCode.append(this.getOther1());
            newHashCode.append(other1);
        }
        String other2 = baike.getOther2();
        if (StringUtils.isNotEmpty(other2)) {
            oldHashCode.append(this.getOther2());
            newHashCode.append(other2);
        }
        String other3 = baike.getOther3();
        if (StringUtils.isNotEmpty(other3)) {
            oldHashCode.append(this.getOther3());
            newHashCode.append(other3);
        }
        String other4 = baike.getOther4();
        if (StringUtils.isNotEmpty(other4)) {
            oldHashCode.append(this.getOther4());
            newHashCode.append(other4);
        }
        String programId = baike.getProgramId();
        if (StringUtils.isNotEmpty(programId)) {
            oldHashCode.append(this.getProgramId());
            newHashCode.append(programId);
        }
        if (oldHashCode.hashCode() == newHashCode.hashCode()) {
            return false;
        } else {
            return true;
        }
    }
