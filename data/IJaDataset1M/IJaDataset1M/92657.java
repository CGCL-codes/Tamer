package com.dotmarketing.cms.polls.ajax;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import uk.ltd.getahead.dwr.WebContextFactory;
import com.dotmarketing.cms.polls.business.PollsAPI;
import com.dotmarketing.cms.polls.business.PollsAPILiferayImpl;
import com.dotmarketing.util.WebKeys;
import com.dotmarketing.viewtools.PollsWebAPI;
import com.liferay.counter.ejb.CounterManagerUtil;
import com.liferay.portal.model.User;
import com.liferay.portlet.polls.model.PollsQuestion;
import com.liferay.util.servlet.SessionMessages;

public class PollsAjax {

    private PollsAPI pollsAPI;

    /**
	 * 
	 * @return
	 */
    public PollsAPI getPollsAPI() {
        return new PollsAPILiferayImpl();
    }

    /**
	 * 
	 * @param pollsAPI
	 */
    public void setPollsAPI(PollsAPILiferayImpl pollsAPI) {
        this.pollsAPI = pollsAPI;
    }

    /**
	 * 
	 * @param questionId
	 * @param choiceId
	 * @param showVotes
	 * @return
	 */
    public String vote(String questionId, String choiceId, boolean showVotes) {
        PollsAPI pollsAPI = getPollsAPI();
        HttpSession session = WebContextFactory.get().getSession();
        HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
        User user = (User) session.getAttribute(WebKeys.CMS_USER);
        String userId = null;
        if (user != null) {
            userId = user.getUserId();
        } else {
            try {
                userId = Long.toString(CounterManagerUtil.increment(PollsQuestion.class.getName() + ".anonymous"));
            } catch (Exception e2) {
            }
        }
        String voteUserId = "";
        boolean hasVoted = false;
        voteUserId = userId;
        if (pollsAPI.hasVoted(questionId) || (session.getAttribute(PollsQuestion.class.getName() + "." + questionId + "._voted") != null)) {
            hasVoted = true;
        }
        if (!hasVoted) {
            pollsAPI.vote(voteUserId, questionId, choiceId);
            SessionMessages.add(request, "vote_added");
            session.setAttribute(PollsQuestion.class.getName() + "." + questionId + "._voted", new Boolean(true));
        }
        return displayPollResults(questionId, showVotes);
    }

    /**
	 * 
	 * @param questionId
	 * @param showVotes
	 * @return
	 */
    public String displayPollResults(String questionId, boolean showVotes) {
        return PollsWebAPI.displayPollResults(questionId, showVotes);
    }
}
