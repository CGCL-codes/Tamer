package org.bejug.javacareers.jobs.model;

/**
 *
 * @author Sven Schauwvliege (Last modified by $Author: shally $)
 * @version $Revision: 1.4 $ - $Date: 2005/12/20 15:36:45 $
 */
public class QAndA extends AbstractPersistableObject {

    /**
	 * The question String.
	 */
    private String question;

    /**
	 * The answer String.
	 */
    private String answer;

    /**
	 * The queuePoint String.
	 */
    private String queuePoint;

    /**
	 * @return Returns the answer.
	 */
    public String getAnswer() {
        return answer;
    }

    /**
	 * @param answer The answer to set.
	 */
    public void setAnswer(String answer) {
        this.answer = answer;
    }

    /**
	 * @return Returns the question.
	 */
    public String getQuestion() {
        return question;
    }

    /**
	 * @param question The question to set.
	 */
    public void setQuestion(String question) {
        this.question = question;
    }

    /**
	 * @return Returns the queuePoint.
	 */
    public String getQueuePoint() {
        return queuePoint;
    }

    /**
	 * @param url is the queuePoint to set.
	 */
    public void setQueuePoint(String url) {
        this.queuePoint = url;
    }
}
