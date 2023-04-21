package com.hp.hpl.jena.query.darq.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.query.expr.Expr;
import de.hu_berlin.informatik.wbi.darq.mapping.Rule;

public class StringConcatServiceGroup extends ServiceGroup {

    Set<Rule> subjectRules = new HashSet<Rule>();

    Set<Rule> predicateRules = new HashSet<Rule>();

    Set<Rule> objectRules = new HashSet<Rule>();

    HashMap<Triple, Triple> scTriples = new HashMap<Triple, Triple>();

    Boolean concat = false;

    Boolean tripleInHead = false;

    HashMap<Integer, String> variablesOrderedByRule = new HashMap<Integer, String>();

    public StringConcatServiceGroup(RemoteService s, Set<Rule> subjectRules, Set<Rule> predicateRules, Set<Rule> objectRules) {
        super(s);
        this.subjectRules = subjectRules;
        this.predicateRules = predicateRules;
        this.objectRules = objectRules;
    }

    public Set<Rule> getSubjectRules() {
        return subjectRules;
    }

    public Set<Rule> getPredicateRules() {
        return predicateRules;
    }

    public Set<Rule> getObjectRules() {
        return objectRules;
    }

    public void addSubjectRules(Set<Rule> subjectRules) {
        subjectRules.addAll(subjectRules);
    }

    public void addPredicateRules(Set<Rule> predicateRules) {
        predicateRules.addAll(predicateRules);
    }

    public void addObjectRules(Set<Rule> objectRules) {
        objectRules.addAll(objectRules);
    }

    public void addallRules(Set<Rule> subjectRules, Set<Rule> predicateRules, Set<Rule> objectRules) {
        subjectRules.addAll(subjectRules);
        predicateRules.addAll(predicateRules);
        objectRules.addAll(objectRules);
    }

    public void addOrignalTriple(Triple triple, Triple originalTriple) {
        scTriples.put(triple, originalTriple);
    }

    public Triple getOriginalTriple(Triple triple) {
        return scTriples.get(triple);
    }

    public HashMap<Triple, Triple> getScTriples() {
        return scTriples;
    }

    public void setScTriples(HashMap<Triple, Triple> scTriples) {
        this.scTriples = scTriples;
    }

    @Override
    public ServiceGroup clone() {
        StringConcatServiceGroup sg = new StringConcatServiceGroup(service, subjectRules, predicateRules, objectRules);
        sg.triples = new ArrayList<Triple>(this.triples);
        sg.filters = new ArrayList<Expr>(this.filters);
        sg.usedVariables = new HashSet<String>(usedVariables);
        sg.scTriples = new HashMap<Triple, Triple>(scTriples);
        sg.subjectRules = new HashSet<Rule>(subjectRules);
        sg.predicateRules = new HashSet<Rule>(predicateRules);
        sg.objectRules = new HashSet<Rule>(objectRules);
        sg.concat = this.concat;
        sg.tripleInHead = this.tripleInHead;
        sg.variablesOrderedByRule = this.variablesOrderedByRule;
        return sg;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof StringConcatServiceGroup) {
            StringConcatServiceGroup otherGroup = (StringConcatServiceGroup) obj;
            if (service.equals(otherGroup.service) && triples.equals(otherGroup.triples) && filters.equals(otherGroup.filters) && subjectRules.equals(otherGroup.subjectRules) && predicateRules.equals(otherGroup.predicateRules) && objectRules.equals(otherGroup.objectRules) && scTriples.equals(otherGroup.scTriples) && concat.equals(otherGroup.concat) && tripleInHead.equals(otherGroup.tripleInHead) && variablesOrderedByRule.equals(otherGroup.variablesOrderedByRule)) return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hc = service.getUrl().hashCode() ^ triples.hashCode() ^ filters.hashCode() ^ scTriples.hashCode() ^ subjectRules.hashCode() ^ predicateRules.hashCode() ^ objectRules.hashCode() ^ concat.hashCode() ^ tripleInHead.hashCode() ^ variablesOrderedByRule.hashCode();
        if (service.getGraph() != null) {
            hc = hc ^ service.getGraph().hashCode();
        }
        return hc;
    }

    public Boolean isConcat() {
        return concat;
    }

    public void setConcat(Boolean concat) {
        this.concat = concat;
    }

    public Boolean getTripleInHead() {
        return tripleInHead;
    }

    public void setTripleInHead(Boolean tripleInHead) {
        this.tripleInHead = tripleInHead;
    }

    /**
	 * @return the variables of the binding in order of the StringConcat Rule
	 * */
    public HashMap<Integer, String> getVariablesOrderedByRule() {
        return variablesOrderedByRule;
    }

    public void setVariablesOrderedByRule(HashMap<Integer, String> splitVariables) {
        this.variablesOrderedByRule = splitVariables;
    }

    public void addVariableOrderedByRule(Integer index, String splitVariable) {
        variablesOrderedByRule.put(index, splitVariable);
    }

    public void removeVariableOrderedByRule(String splitVariable) {
        variablesOrderedByRule.remove(splitVariable);
    }
}
