    @PreAuthorize("hasRole('ENCUESTAME_USER')")
    @Listener("/service/tweetpoll/autosave")
    @SuppressWarnings("unchecked")
    public void processAutoSave(final ServerSession remote, final ServerMessage.Mutable message) {
        ;
        log.debug("--------- TweetPoll COMMET AUTOSAVE ----------");
        final Map<String, Object> inputMessage = message.getDataAsMap();
        Map<String, Object> outPutMessage = new HashedMap();
        if (log.isDebugEnabled()) {
            log.debug("Messages content:{" + inputMessage.toString());
            log.debug("Messages content JSON:{" + message.getJSON());
            log.debug("Messages content TweetPoll:{" + inputMessage.get("tweetPoll"));
        }
        final Map<String, Object> tweetPollJson = (Map<String, Object>) inputMessage.get("tweetPoll");
        List<String> hastagsArray = new ArrayList<String>();
        List<Long> answerArray = new ArrayList<Long>();
        final Object[] hashtags = (Object[]) tweetPollJson.get("hashtags");
        if (log.isDebugEnabled()) {
            log.debug("Array of hashtags: ---->" + tweetPollJson.get("hashtags"));
            log.debug("Array of hashtags: ---->" + hashtags);
            log.debug("Array of hashtags: ---->" + hashtags.length);
        }
        for (int i = 0; i < hashtags.length; i++) {
            HashMap<String, String> hashtagsMap = (HashMap<String, String>) hashtags[i];
            if (log.isDebugEnabled()) {
                log.debug("Hashtag: ---->" + hashtagsMap.get("label"));
                log.debug(hashtagsMap.get("newValue"));
            }
            if (hashtagsMap.get("label") != null) {
                hastagsArray.add(hashtagsMap.get("label"));
            }
        }
        final Object[] answers = (Object[]) tweetPollJson.get("answers");
        if (log.isDebugEnabled()) {
            log.debug("Array of Answer: ---->" + tweetPollJson.get("answers"));
            log.debug("Array of Answer: ---->" + answers.length);
        }
        for (int i = 0; i < answers.length; i++) {
            Long answersMap = (Long) answers[i];
            if (answersMap != null) {
                answerArray.add(Long.valueOf(answersMap));
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("review answerArray: " + answerArray.size());
            log.debug("review hastagsArray: " + hastagsArray.size());
        }
        final HashMap<String, String> questionMap = (HashMap<String, String>) tweetPollJson.get("question");
        final String question = filterValue(questionMap.get("value") == null ? "" : questionMap.get("value"));
        final Options options = new Options((tweetPollJson.get("options") == null ? new HashedMap() : (Map<String, Object>) tweetPollJson.get("options")));
        if (log.isDebugEnabled()) {
            log.debug("review options: " + options.toString());
        }
        try {
            final UserAccount user = getUserAccount();
            if (user != null) {
                final Long tweetPollId = tweetPollJson.get("tweetPollId") == null ? null : Long.valueOf(tweetPollJson.get("tweetPollId").toString());
                if (tweetPollId == null) {
                    final TweetPollBean tweetPollBean = this.fillTweetPoll(options, question, user, hastagsArray, null);
                    final TweetPoll tweetPoll = createTweetPoll(tweetPollBean);
                    outPutMessage.put("tweetPollId", tweetPoll.getTweetPollId());
                    log.debug("tweet poll created.");
                } else {
                    log.debug("updated tweetPoll:{" + tweetPollJson.get("tweetPollId"));
                    final TweetPollBean tweetPollBean = this.fillTweetPoll(options, question, user, hastagsArray, tweetPollId);
                    updateTweetPoll(tweetPollBean);
                    outPutMessage = inputMessage;
                    log.debug("updated tweetPoll:{" + tweetPollJson.get("tweetPollId"));
                }
            } else {
                log.warn("forbiden access");
            }
        } catch (EnMeExpcetion e) {
            log.error(e);
        } catch (ParseException e) {
            log.error(e);
        }
        log.debug("tweetPoll content:{" + outPutMessage);
        remote.deliver(getServerSession(), message.getChannel(), outPutMessage, null);
    }
