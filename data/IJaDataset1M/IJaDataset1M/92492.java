////////////////////////////////////////////
// 
// Generated by RuleDesigner
// 
////////////////////////////////////////////

package uk.ac.lkl.migen.system.ai.reasoning;

// TODO: Put all imports here

public abstract class ReasonerTemplate
    extends ConstructionExpressionMultipleRulesReasoner {
    
    private String LOOKED_LIKE_SOLUTION_BEFORE = "Looked like construction before";
    private static boolean notify_messup = false; 
    
    public ReasonerTemplate(
	    ReasonerType type,
	    FeedbackGenerator fbGenerator,
	    ShortTermLearnerModel um,
	    Map<String, Detector> detectors,
	    Map<String, Evaluator> evaluators,
	    Map<String, Verifier> verifiers, 
	    ReasoningTrigger trigger,
	    ConstructionExpressionTask task) {
	super(type,fbGenerator,um,detectors,evaluators,verifiers,trigger,task);
    }
    
    // TODO: additional support functions like addressFeedbackRequest, maybe by inheritance
    
    private void evaluateGoalAccomplishmentRules() {
        // Goal rules:
        GOAL-RULES;
        
        // Feedback rules:
        FEEDBACK-RULES;
    }
}
