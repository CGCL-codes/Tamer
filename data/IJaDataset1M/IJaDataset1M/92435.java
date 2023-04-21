package iec61970.topology;

import iec61970.core.CorePackage;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * <!-- begin-model-doc -->
 * An extension to the Core Package that in association with the Terminal class models Connectivity, that is the physical definition of how equipment is connected together. In addition it models Topology, that is the logical definition of how equipment is connected via closed switches. The Topology definition is independent of the other electrical characteristics.
 * <!-- end-model-doc -->
 * @see iec61970.topology.TopologyFactory
 * @model kind="package"
 * @generated
 */
public interface TopologyPackage extends EPackage {

    /**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    String eNAME = "topology";

    /**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    String eNS_URI = "http:///iec61970/topology.ecore";

    /**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    String eNS_PREFIX = "iec61970.topology";

    /**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    TopologyPackage eINSTANCE = iec61970.topology.impl.TopologyPackageImpl.init();

    /**
	 * The meta object id for the '{@link iec61970.topology.impl.ConnectivityNodeImpl <em>Connectivity Node</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see iec61970.topology.impl.ConnectivityNodeImpl
	 * @see iec61970.topology.impl.TopologyPackageImpl#getConnectivityNode()
	 * @generated
	 */
    int CONNECTIVITY_NODE = 0;

    /**
	 * The feature id for the '<em><b>MRID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CONNECTIVITY_NODE__MRID = CorePackage.IDENTIFIED_OBJECT__MRID;

    /**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CONNECTIVITY_NODE__NAME = CorePackage.IDENTIFIED_OBJECT__NAME;

    /**
	 * The feature id for the '<em><b>Local Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CONNECTIVITY_NODE__LOCAL_NAME = CorePackage.IDENTIFIED_OBJECT__LOCAL_NAME;

    /**
	 * The feature id for the '<em><b>Path Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CONNECTIVITY_NODE__PATH_NAME = CorePackage.IDENTIFIED_OBJECT__PATH_NAME;

    /**
	 * The feature id for the '<em><b>Alias Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CONNECTIVITY_NODE__ALIAS_NAME = CorePackage.IDENTIFIED_OBJECT__ALIAS_NAME;

    /**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CONNECTIVITY_NODE__DESCRIPTION = CorePackage.IDENTIFIED_OBJECT__DESCRIPTION;

    /**
	 * The feature id for the '<em><b>Topological Node</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CONNECTIVITY_NODE__TOPOLOGICAL_NODE = CorePackage.IDENTIFIED_OBJECT_FEATURE_COUNT + 0;

    /**
	 * The feature id for the '<em><b>Terminals</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CONNECTIVITY_NODE__TERMINALS = CorePackage.IDENTIFIED_OBJECT_FEATURE_COUNT + 1;

    /**
	 * The feature id for the '<em><b>Member Of Equipment Container</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CONNECTIVITY_NODE__MEMBER_OF_EQUIPMENT_CONTAINER = CorePackage.IDENTIFIED_OBJECT_FEATURE_COUNT + 2;

    /**
	 * The number of structural features of the '<em>Connectivity Node</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CONNECTIVITY_NODE_FEATURE_COUNT = CorePackage.IDENTIFIED_OBJECT_FEATURE_COUNT + 3;

    /**
	 * The meta object id for the '{@link iec61970.topology.impl.TopologicalIslandImpl <em>Topological Island</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see iec61970.topology.impl.TopologicalIslandImpl
	 * @see iec61970.topology.impl.TopologyPackageImpl#getTopologicalIsland()
	 * @generated
	 */
    int TOPOLOGICAL_ISLAND = 1;

    /**
	 * The feature id for the '<em><b>MRID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int TOPOLOGICAL_ISLAND__MRID = CorePackage.IDENTIFIED_OBJECT__MRID;

    /**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int TOPOLOGICAL_ISLAND__NAME = CorePackage.IDENTIFIED_OBJECT__NAME;

    /**
	 * The feature id for the '<em><b>Local Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int TOPOLOGICAL_ISLAND__LOCAL_NAME = CorePackage.IDENTIFIED_OBJECT__LOCAL_NAME;

    /**
	 * The feature id for the '<em><b>Path Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int TOPOLOGICAL_ISLAND__PATH_NAME = CorePackage.IDENTIFIED_OBJECT__PATH_NAME;

    /**
	 * The feature id for the '<em><b>Alias Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int TOPOLOGICAL_ISLAND__ALIAS_NAME = CorePackage.IDENTIFIED_OBJECT__ALIAS_NAME;

    /**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int TOPOLOGICAL_ISLAND__DESCRIPTION = CorePackage.IDENTIFIED_OBJECT__DESCRIPTION;

    /**
	 * The feature id for the '<em><b>Topological Nodes</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int TOPOLOGICAL_ISLAND__TOPOLOGICAL_NODES = CorePackage.IDENTIFIED_OBJECT_FEATURE_COUNT + 0;

    /**
	 * The number of structural features of the '<em>Topological Island</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int TOPOLOGICAL_ISLAND_FEATURE_COUNT = CorePackage.IDENTIFIED_OBJECT_FEATURE_COUNT + 1;

    /**
	 * The meta object id for the '{@link iec61970.topology.impl.TopologicalNodeImpl <em>Topological Node</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see iec61970.topology.impl.TopologicalNodeImpl
	 * @see iec61970.topology.impl.TopologyPackageImpl#getTopologicalNode()
	 * @generated
	 */
    int TOPOLOGICAL_NODE = 2;

    /**
	 * The feature id for the '<em><b>MRID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int TOPOLOGICAL_NODE__MRID = CorePackage.IDENTIFIED_OBJECT__MRID;

    /**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int TOPOLOGICAL_NODE__NAME = CorePackage.IDENTIFIED_OBJECT__NAME;

    /**
	 * The feature id for the '<em><b>Local Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int TOPOLOGICAL_NODE__LOCAL_NAME = CorePackage.IDENTIFIED_OBJECT__LOCAL_NAME;

    /**
	 * The feature id for the '<em><b>Path Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int TOPOLOGICAL_NODE__PATH_NAME = CorePackage.IDENTIFIED_OBJECT__PATH_NAME;

    /**
	 * The feature id for the '<em><b>Alias Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int TOPOLOGICAL_NODE__ALIAS_NAME = CorePackage.IDENTIFIED_OBJECT__ALIAS_NAME;

    /**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int TOPOLOGICAL_NODE__DESCRIPTION = CorePackage.IDENTIFIED_OBJECT__DESCRIPTION;

    /**
	 * The feature id for the '<em><b>Energized</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int TOPOLOGICAL_NODE__ENERGIZED = CorePackage.IDENTIFIED_OBJECT_FEATURE_COUNT + 0;

    /**
	 * The feature id for the '<em><b>Load Carrying</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int TOPOLOGICAL_NODE__LOAD_CARRYING = CorePackage.IDENTIFIED_OBJECT_FEATURE_COUNT + 1;

    /**
	 * The feature id for the '<em><b>Net Injection Q</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int TOPOLOGICAL_NODE__NET_INJECTION_Q = CorePackage.IDENTIFIED_OBJECT_FEATURE_COUNT + 2;

    /**
	 * The feature id for the '<em><b>Net Injection P</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int TOPOLOGICAL_NODE__NET_INJECTION_P = CorePackage.IDENTIFIED_OBJECT_FEATURE_COUNT + 3;

    /**
	 * The feature id for the '<em><b>Observability Flag</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int TOPOLOGICAL_NODE__OBSERVABILITY_FLAG = CorePackage.IDENTIFIED_OBJECT_FEATURE_COUNT + 4;

    /**
	 * The feature id for the '<em><b>Phase Angle</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int TOPOLOGICAL_NODE__PHASE_ANGLE = CorePackage.IDENTIFIED_OBJECT_FEATURE_COUNT + 5;

    /**
	 * The feature id for the '<em><b>Voltage</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int TOPOLOGICAL_NODE__VOLTAGE = CorePackage.IDENTIFIED_OBJECT_FEATURE_COUNT + 6;

    /**
	 * The feature id for the '<em><b>Connectivity Nodes</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int TOPOLOGICAL_NODE__CONNECTIVITY_NODES = CorePackage.IDENTIFIED_OBJECT_FEATURE_COUNT + 7;

    /**
	 * The feature id for the '<em><b>Topological Island</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int TOPOLOGICAL_NODE__TOPOLOGICAL_ISLAND = CorePackage.IDENTIFIED_OBJECT_FEATURE_COUNT + 8;

    /**
	 * The number of structural features of the '<em>Topological Node</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int TOPOLOGICAL_NODE_FEATURE_COUNT = CorePackage.IDENTIFIED_OBJECT_FEATURE_COUNT + 9;

    /**
	 * The meta object id for the '{@link iec61970.topology.impl.TopologyVersionImpl <em>Version</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see iec61970.topology.impl.TopologyVersionImpl
	 * @see iec61970.topology.impl.TopologyPackageImpl#getTopologyVersion()
	 * @generated
	 */
    int TOPOLOGY_VERSION = 3;

    /**
	 * The feature id for the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int TOPOLOGY_VERSION__VERSION = 0;

    /**
	 * The feature id for the '<em><b>Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int TOPOLOGY_VERSION__DATE = 1;

    /**
	 * The number of structural features of the '<em>Version</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int TOPOLOGY_VERSION_FEATURE_COUNT = 2;

    /**
	 * Returns the meta object for class '{@link iec61970.topology.ConnectivityNode <em>Connectivity Node</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Connectivity Node</em>'.
	 * @see iec61970.topology.ConnectivityNode
	 * @generated
	 */
    EClass getConnectivityNode();

    /**
	 * Returns the meta object for the reference '{@link iec61970.topology.ConnectivityNode#getTopologicalNode <em>Topological Node</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Topological Node</em>'.
	 * @see iec61970.topology.ConnectivityNode#getTopologicalNode()
	 * @see #getConnectivityNode()
	 * @generated
	 */
    EReference getConnectivityNode_TopologicalNode();

    /**
	 * Returns the meta object for the reference list '{@link iec61970.topology.ConnectivityNode#getTerminals <em>Terminals</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Terminals</em>'.
	 * @see iec61970.topology.ConnectivityNode#getTerminals()
	 * @see #getConnectivityNode()
	 * @generated
	 */
    EReference getConnectivityNode_Terminals();

    /**
	 * Returns the meta object for the container reference '{@link iec61970.topology.ConnectivityNode#getMemberOf_EquipmentContainer <em>Member Of Equipment Container</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Member Of Equipment Container</em>'.
	 * @see iec61970.topology.ConnectivityNode#getMemberOf_EquipmentContainer()
	 * @see #getConnectivityNode()
	 * @generated
	 */
    EReference getConnectivityNode_MemberOf_EquipmentContainer();

    /**
	 * Returns the meta object for class '{@link iec61970.topology.TopologicalIsland <em>Topological Island</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Topological Island</em>'.
	 * @see iec61970.topology.TopologicalIsland
	 * @generated
	 */
    EClass getTopologicalIsland();

    /**
	 * Returns the meta object for the reference list '{@link iec61970.topology.TopologicalIsland#getTopologicalNodes <em>Topological Nodes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Topological Nodes</em>'.
	 * @see iec61970.topology.TopologicalIsland#getTopologicalNodes()
	 * @see #getTopologicalIsland()
	 * @generated
	 */
    EReference getTopologicalIsland_TopologicalNodes();

    /**
	 * Returns the meta object for class '{@link iec61970.topology.TopologicalNode <em>Topological Node</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Topological Node</em>'.
	 * @see iec61970.topology.TopologicalNode
	 * @generated
	 */
    EClass getTopologicalNode();

    /**
	 * Returns the meta object for the attribute '{@link iec61970.topology.TopologicalNode#getEnergized <em>Energized</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Energized</em>'.
	 * @see iec61970.topology.TopologicalNode#getEnergized()
	 * @see #getTopologicalNode()
	 * @generated
	 */
    EAttribute getTopologicalNode_Energized();

    /**
	 * Returns the meta object for the attribute '{@link iec61970.topology.TopologicalNode#getLoadCarrying <em>Load Carrying</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Load Carrying</em>'.
	 * @see iec61970.topology.TopologicalNode#getLoadCarrying()
	 * @see #getTopologicalNode()
	 * @generated
	 */
    EAttribute getTopologicalNode_LoadCarrying();

    /**
	 * Returns the meta object for the attribute '{@link iec61970.topology.TopologicalNode#getNetInjectionQ <em>Net Injection Q</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Net Injection Q</em>'.
	 * @see iec61970.topology.TopologicalNode#getNetInjectionQ()
	 * @see #getTopologicalNode()
	 * @generated
	 */
    EAttribute getTopologicalNode_NetInjectionQ();

    /**
	 * Returns the meta object for the attribute '{@link iec61970.topology.TopologicalNode#getNetInjectionP <em>Net Injection P</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Net Injection P</em>'.
	 * @see iec61970.topology.TopologicalNode#getNetInjectionP()
	 * @see #getTopologicalNode()
	 * @generated
	 */
    EAttribute getTopologicalNode_NetInjectionP();

    /**
	 * Returns the meta object for the attribute '{@link iec61970.topology.TopologicalNode#getObservabilityFlag <em>Observability Flag</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Observability Flag</em>'.
	 * @see iec61970.topology.TopologicalNode#getObservabilityFlag()
	 * @see #getTopologicalNode()
	 * @generated
	 */
    EAttribute getTopologicalNode_ObservabilityFlag();

    /**
	 * Returns the meta object for the attribute '{@link iec61970.topology.TopologicalNode#getPhaseAngle <em>Phase Angle</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Phase Angle</em>'.
	 * @see iec61970.topology.TopologicalNode#getPhaseAngle()
	 * @see #getTopologicalNode()
	 * @generated
	 */
    EAttribute getTopologicalNode_PhaseAngle();

    /**
	 * Returns the meta object for the attribute '{@link iec61970.topology.TopologicalNode#getVoltage <em>Voltage</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Voltage</em>'.
	 * @see iec61970.topology.TopologicalNode#getVoltage()
	 * @see #getTopologicalNode()
	 * @generated
	 */
    EAttribute getTopologicalNode_Voltage();

    /**
	 * Returns the meta object for the reference list '{@link iec61970.topology.TopologicalNode#getConnectivityNodes <em>Connectivity Nodes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Connectivity Nodes</em>'.
	 * @see iec61970.topology.TopologicalNode#getConnectivityNodes()
	 * @see #getTopologicalNode()
	 * @generated
	 */
    EReference getTopologicalNode_ConnectivityNodes();

    /**
	 * Returns the meta object for the reference '{@link iec61970.topology.TopologicalNode#getTopologicalIsland <em>Topological Island</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Topological Island</em>'.
	 * @see iec61970.topology.TopologicalNode#getTopologicalIsland()
	 * @see #getTopologicalNode()
	 * @generated
	 */
    EReference getTopologicalNode_TopologicalIsland();

    /**
	 * Returns the meta object for class '{@link iec61970.topology.TopologyVersion <em>Version</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Version</em>'.
	 * @see iec61970.topology.TopologyVersion
	 * @generated
	 */
    EClass getTopologyVersion();

    /**
	 * Returns the meta object for the attribute '{@link iec61970.topology.TopologyVersion#getVersion <em>Version</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Version</em>'.
	 * @see iec61970.topology.TopologyVersion#getVersion()
	 * @see #getTopologyVersion()
	 * @generated
	 */
    EAttribute getTopologyVersion_Version();

    /**
	 * Returns the meta object for the attribute '{@link iec61970.topology.TopologyVersion#getDate <em>Date</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Date</em>'.
	 * @see iec61970.topology.TopologyVersion#getDate()
	 * @see #getTopologyVersion()
	 * @generated
	 */
    EAttribute getTopologyVersion_Date();

    /**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
    TopologyFactory getTopologyFactory();

    /**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
    interface Literals {

        /**
		 * The meta object literal for the '{@link iec61970.topology.impl.ConnectivityNodeImpl <em>Connectivity Node</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see iec61970.topology.impl.ConnectivityNodeImpl
		 * @see iec61970.topology.impl.TopologyPackageImpl#getConnectivityNode()
		 * @generated
		 */
        EClass CONNECTIVITY_NODE = eINSTANCE.getConnectivityNode();

        /**
		 * The meta object literal for the '<em><b>Topological Node</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EReference CONNECTIVITY_NODE__TOPOLOGICAL_NODE = eINSTANCE.getConnectivityNode_TopologicalNode();

        /**
		 * The meta object literal for the '<em><b>Terminals</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EReference CONNECTIVITY_NODE__TERMINALS = eINSTANCE.getConnectivityNode_Terminals();

        /**
		 * The meta object literal for the '<em><b>Member Of Equipment Container</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EReference CONNECTIVITY_NODE__MEMBER_OF_EQUIPMENT_CONTAINER = eINSTANCE.getConnectivityNode_MemberOf_EquipmentContainer();

        /**
		 * The meta object literal for the '{@link iec61970.topology.impl.TopologicalIslandImpl <em>Topological Island</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see iec61970.topology.impl.TopologicalIslandImpl
		 * @see iec61970.topology.impl.TopologyPackageImpl#getTopologicalIsland()
		 * @generated
		 */
        EClass TOPOLOGICAL_ISLAND = eINSTANCE.getTopologicalIsland();

        /**
		 * The meta object literal for the '<em><b>Topological Nodes</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EReference TOPOLOGICAL_ISLAND__TOPOLOGICAL_NODES = eINSTANCE.getTopologicalIsland_TopologicalNodes();

        /**
		 * The meta object literal for the '{@link iec61970.topology.impl.TopologicalNodeImpl <em>Topological Node</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see iec61970.topology.impl.TopologicalNodeImpl
		 * @see iec61970.topology.impl.TopologyPackageImpl#getTopologicalNode()
		 * @generated
		 */
        EClass TOPOLOGICAL_NODE = eINSTANCE.getTopologicalNode();

        /**
		 * The meta object literal for the '<em><b>Energized</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute TOPOLOGICAL_NODE__ENERGIZED = eINSTANCE.getTopologicalNode_Energized();

        /**
		 * The meta object literal for the '<em><b>Load Carrying</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute TOPOLOGICAL_NODE__LOAD_CARRYING = eINSTANCE.getTopologicalNode_LoadCarrying();

        /**
		 * The meta object literal for the '<em><b>Net Injection Q</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute TOPOLOGICAL_NODE__NET_INJECTION_Q = eINSTANCE.getTopologicalNode_NetInjectionQ();

        /**
		 * The meta object literal for the '<em><b>Net Injection P</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute TOPOLOGICAL_NODE__NET_INJECTION_P = eINSTANCE.getTopologicalNode_NetInjectionP();

        /**
		 * The meta object literal for the '<em><b>Observability Flag</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute TOPOLOGICAL_NODE__OBSERVABILITY_FLAG = eINSTANCE.getTopologicalNode_ObservabilityFlag();

        /**
		 * The meta object literal for the '<em><b>Phase Angle</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute TOPOLOGICAL_NODE__PHASE_ANGLE = eINSTANCE.getTopologicalNode_PhaseAngle();

        /**
		 * The meta object literal for the '<em><b>Voltage</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute TOPOLOGICAL_NODE__VOLTAGE = eINSTANCE.getTopologicalNode_Voltage();

        /**
		 * The meta object literal for the '<em><b>Connectivity Nodes</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EReference TOPOLOGICAL_NODE__CONNECTIVITY_NODES = eINSTANCE.getTopologicalNode_ConnectivityNodes();

        /**
		 * The meta object literal for the '<em><b>Topological Island</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EReference TOPOLOGICAL_NODE__TOPOLOGICAL_ISLAND = eINSTANCE.getTopologicalNode_TopologicalIsland();

        /**
		 * The meta object literal for the '{@link iec61970.topology.impl.TopologyVersionImpl <em>Version</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see iec61970.topology.impl.TopologyVersionImpl
		 * @see iec61970.topology.impl.TopologyPackageImpl#getTopologyVersion()
		 * @generated
		 */
        EClass TOPOLOGY_VERSION = eINSTANCE.getTopologyVersion();

        /**
		 * The meta object literal for the '<em><b>Version</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute TOPOLOGY_VERSION__VERSION = eINSTANCE.getTopologyVersion_Version();

        /**
		 * The meta object literal for the '<em><b>Date</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute TOPOLOGY_VERSION__DATE = eINSTANCE.getTopologyVersion_Date();
    }
}
