package edu.harvard.fas.rregan.requel.project.impl;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import org.hibernate.annotations.IndexColumn;
import edu.harvard.fas.rregan.requel.project.ProjectOrDomain;
import edu.harvard.fas.rregan.requel.project.Scenario;
import edu.harvard.fas.rregan.requel.project.ScenarioType;
import edu.harvard.fas.rregan.requel.project.Step;
import edu.harvard.fas.rregan.requel.project.UseCase;
import edu.harvard.fas.rregan.requel.user.User;
import edu.harvard.fas.rregan.requel.user.UserRepository;
import edu.harvard.fas.rregan.requel.utils.jaxb.UnmarshallerListener;

/**
 * @author ron
 */
@Entity
@DiscriminatorValue(value = "edu.harvard.fas.rregan.requel.project.Scenario")
@XmlRootElement(name = "scenario", namespace = "http://www.people.fas.harvard.edu/~rregan/requel")
@XmlType(name = "scenario", namespace = "http://www.people.fas.harvard.edu/~rregan/requel")
public class ScenarioImpl extends StepImpl implements Scenario {

    static final long serialVersionUID = 0L;

    private List<Step> steps = new ArrayList<Step>();

    private Set<UseCase> usedByUseCases = new TreeSet<UseCase>();

    /**
	 * Create a scenario.
	 * 
	 * @param projectOrDomain
	 * @param createdBy
	 * @param name
	 * @param text -
	 *            short text description
	 * @param scenarioType
	 */
    public ScenarioImpl(ProjectOrDomain projectOrDomain, User createdBy, String name, String text, ScenarioType scenarioType) {
        super(Scenario.class.getName(), projectOrDomain, createdBy, name, text, scenarioType);
        projectOrDomain.getScenarios().add(this);
    }

    protected ScenarioImpl() {
    }

    @Override
    @XmlTransient
    @ManyToOne(targetEntity = AbstractProjectOrDomain.class, cascade = { CascadeType.PERSIST, CascadeType.REFRESH }, optional = false)
    @JoinColumn(insertable = false, updatable = false)
    public ProjectOrDomain getProjectOrDomain() {
        return super.getProjectOrDomain();
    }

    @Override
    protected void setProjectOrDomain(ProjectOrDomain projectOrDomain) {
        super.setProjectOrDomain(projectOrDomain);
    }

    /**
	 * @see edu.harvard.fas.rregan.requel.Describable#getDescription()
	 */
    @XmlTransient
    @Transient
    @Override
    public String getDescription() {
        return "Scenario: " + getName();
    }

    @Override
    @XmlTransient
    @OneToMany(targetEntity = UseCaseImpl.class, cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH }, fetch = FetchType.LAZY, mappedBy = "scenario")
    public Set<UseCase> getUsingUseCases() {
        return usedByUseCases;
    }

    protected void setUsingUseCases(Set<UseCase> usingUseCases) {
        this.usedByUseCases = usingUseCases;
    }

    @Override
    @XmlElementWrapper(name = "steps", namespace = "http://www.people.fas.harvard.edu/~rregan/requel")
    @XmlIDREF
    @XmlElement(name = "stepRef", type = StepImpl.class, namespace = "http://www.people.fas.harvard.edu/~rregan/requel")
    @ManyToMany(targetEntity = StepImpl.class, cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH }, fetch = FetchType.LAZY)
    @JoinTable(name = "scenario_steps", joinColumns = { @JoinColumn(name = "scenario_id") }, inverseJoinColumns = { @JoinColumn(name = "step_id") })
    @IndexColumn(name = "step_index", base = 0)
    public List<Step> getSteps() {
        return steps;
    }

    protected void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    @Override
    public boolean usesStep(Step stepToFind) {
        if (this.equals(stepToFind)) {
            return true;
        }
        Deque<Scenario> scenariosToExamine = new LinkedList<Scenario>();
        scenariosToExamine.add(this);
        while (!scenariosToExamine.isEmpty()) {
            Scenario scenario = scenariosToExamine.pop();
            for (Step step : scenario.getSteps()) {
                if (stepToFind.equals(step)) {
                    return true;
                }
                if (step instanceof Scenario) {
                    scenariosToExamine.add((Scenario) step);
                }
            }
        }
        return false;
    }

    /**
	 * This is for JAXB to patchup the parent/child relationship and to patchup
	 * existing persistent objects for the objects that are attached directly to
	 * this object.
	 * 
	 * @param userRepository
	 * @param defaultCreatedByUser -
	 *            the user to be set as the created by if no user is supplied.
	 * @param parent
	 * @see UnmarshallerListener
	 */
    @Override
    public void afterUnmarshal(UserRepository userRepository, User defaultCreatedByUser, Object parent) {
        super.afterUnmarshal(userRepository, defaultCreatedByUser, parent);
        if (parent instanceof ProjectOrDomain) {
            afterUnmarshalFixupChildren(this, (ProjectOrDomain) parent);
        }
    }

    private void afterUnmarshalFixupChildren(Scenario scenario, ProjectOrDomain projectOrDomain) {
        for (Step step : scenario.getSteps()) {
            ((StepImpl) step).setProjectOrDomain(projectOrDomain);
            ((StepImpl) step).getUsingScenarios().add(this);
            if (step instanceof Scenario) {
                afterUnmarshalFixupChildren((Scenario) step, projectOrDomain);
            }
        }
    }

    /**
	 * This class is used by JAXB to convert the id of an entity into an xml id
	 * string that will be distinct from other entity xml id strings by the use
	 * of a prefix.
	 * 
	 * @author ron
	 */
    @XmlTransient
    public static class IdAdapter extends XmlAdapter<String, Long> {

        private static final String prefix = "SCN_";

        @Override
        public Long unmarshal(String id) throws Exception {
            return null;
        }

        @Override
        public String marshal(Long id) throws Exception {
            if (id != null) {
                return prefix + id.toString();
            }
            return "";
        }
    }
}
