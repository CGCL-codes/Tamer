package at.ofai.aaa.agent.jam;

import java.io.Serializable;

/**
 * The basic interface for any 'part of a plan' object.
 * Represents the basic procedural components within JAM agents
 * @author wrstlprmpft
 */
interface PlanConstruct extends Serializable {

    PlanRuntimeState newRuntimeState();
}
