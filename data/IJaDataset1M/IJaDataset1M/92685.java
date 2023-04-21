package org.jabusuite.cms.news.employee;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import org.jabusuite.address.employee.JobGroup;
import org.jabusuite.core.language.JbsLanguage;
import org.jabusuite.core.utils.EJbsObject;
import org.jabusuite.core.utils.JbsObject;
import org.jabusuite.logging.Logger;

/**
 *
 * @author hilwers
 */
@Entity
public class EmployeeNewsEntry extends JbsObject implements Serializable {

    private Logger logger = Logger.getLogger(EmployeeNewsEntry.class);

    private Set<EmployeeNewsText> newsTexts;

    private Calendar newsDate;

    private JobGroup jobGroup;

    private int priority;

    @Override
    protected void setStandardValues() {
        super.setStandardValues();
        this.setNewsTexts(null);
        this.setNewsDate(Calendar.getInstance());
        this.setJobGroup(null);
    }

    @Override
    public void checkData(boolean doAdditionalChecks) throws EJbsObject {
        super.checkData(doAdditionalChecks);
        if (this.getJobGroup() == null) throw new EEmployeeNews(EEmployeeNews.ET_JOBGROUP);
        if (this.getNewsDate() == null) throw new EEmployeeNews(EEmployeeNews.ET_DATE);
    }

    @Transient
    public void setHeadline(JbsLanguage language, String headline) {
        logger.debug("Setting headline");
        if (this.getNewsTexts() == null) {
            this.setNewsTexts(new HashSet<EmployeeNewsText>());
        }
        EmployeeNewsText text = (EmployeeNewsText) this.getLangObject(language, this.getNewsTexts());
        if ((text == null) && (headline != null) && (!headline.equals(""))) {
            text = new EmployeeNewsText(language);
            text.setEmployeeNewsEntry(this);
            this.getNewsTexts().add(text);
        }
        if (text != null) {
            text.setHeadline(headline);
        }
    }

    @Transient
    public String getHeadline(JbsLanguage language) {
        if (this.getNewsTexts() != null) {
            EmployeeNewsText text = (EmployeeNewsText) this.getLangObject(language, this.getNewsTexts());
            if (text != null) {
                return text.getHeadline();
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Transient
    public void setLongText(JbsLanguage language, String longText) {
        logger.debug("Setting text");
        if (this.getNewsTexts() == null) {
            this.setNewsTexts(new HashSet<EmployeeNewsText>());
        }
        EmployeeNewsText text = (EmployeeNewsText) this.getLangObject(language, this.getNewsTexts());
        if ((text == null) && (longText != null) && (!longText.equals(""))) {
            text = new EmployeeNewsText(language);
            text.setEmployeeNewsEntry(this);
            this.getNewsTexts().add(text);
        }
        if (text != null) {
            text.setLongText(longText);
        }
    }

    @Transient
    public String getLongText(JbsLanguage language) {
        if (this.getNewsTexts() != null) {
            EmployeeNewsText text = (EmployeeNewsText) this.getLangObject(language, this.getNewsTexts());
            if (text != null) {
                return text.getLongText();
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @ManyToOne
    public JobGroup getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(JobGroup jobGroup) {
        this.jobGroup = jobGroup;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Temporal(javax.persistence.TemporalType.DATE)
    public Calendar getNewsDate() {
        return newsDate;
    }

    public void setNewsDate(Calendar newsDate) {
        this.newsDate = newsDate;
    }

    @OneToMany(mappedBy = "employeeNewsEntry", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    public Set<EmployeeNewsText> getNewsTexts() {
        return newsTexts;
    }

    public void setNewsTexts(Set<EmployeeNewsText> newsTexts) {
        this.newsTexts = newsTexts;
    }
}
