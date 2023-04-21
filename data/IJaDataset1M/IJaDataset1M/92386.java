package issrg.ontology;

import java.util.*;
import edu.stanford.smi.protegex.owl.model.OWLIndividual;

public class ActionInstance extends OntologyInstance {

    public static final String PARAMETER_PROPERTY = "Action_has_Parameter";

    protected ActionInstance(OWLIndividual individual) {
        super(individual);
    }

    public ParameterInstance[] getParameters() {
        Collection paraInstances = getObjectPropertieValues(PARAMETER_PROPERTY);
        if (paraInstances == null || paraInstances.size() <= 0) return null;
        ParameterInstance[] parameters = new ParameterInstance[paraInstances.size()];
        paraInstances.toArray(parameters);
        return parameters;
    }
}
