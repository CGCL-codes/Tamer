    private void sayTweets() {
        if (tweetCache == null) {
            tweetCache = tweets;
            return;
        }
        int chanTweets = 0;
        for (int i = 0; i < tweets.size(); i++) {
            if (!tweets.get(i).user.screenName.equalsIgnoreCase(username) && !tweetIdExists(tweets.get(i).id)) {
                if (tweets.get(i).text.contains("@" + username)) {
                    String myTweet = "";
                    JMH_Brain temp = (JMH_Brain) parent.getModule("Brain");
                    if (temp != null) {
                        do {
                            myTweet = temp.getSentence();
                        } while (myTweet.length() > 150);
                        objTwitter.updateStatus("@" + tweets.get(i).user.screenName + " " + myTweet);
                    }
                } else if (chanTweets < maxTweets) {
                    String[][] tmp = getChannels();
                    for (int j = 0; j < tmp.length; j++) {
                        log(tweets.get(i).user.screenName + " says '" + tweets.get(i).text + "'");
                        parent.SendMessage(tmp[j][0], "" + tweets.get(i).user.screenName + " says '" + tweets.get(i).text + "'");
                    }
                    chanTweets++;
                }
            } else {
                break;
            }
        }
        tweetCache = tweets;
    }
