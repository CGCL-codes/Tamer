package org.eclipse.emf.compare.sidiffconfig.impl;

import org.eclipse.emf.compare.sidiffconfig.*;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class SidiffconfigFactoryImpl extends EFactoryImpl implements SidiffconfigFactory {

    /**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public static SidiffconfigFactory init() {
        try {
            SidiffconfigFactory theSidiffconfigFactory = (SidiffconfigFactory) EPackage.Registry.INSTANCE.getEFactory("sidiffconfig");
            if (theSidiffconfigFactory != null) {
                return theSidiffconfigFactory;
            }
        } catch (Exception exception) {
            EcorePlugin.INSTANCE.log(exception);
        }
        return new SidiffconfigFactoryImpl();
    }

    /**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public SidiffconfigFactoryImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public EObject create(EClass eClass) {
        switch(eClass.getClassifierID()) {
            case SidiffconfigPackage.CONFIGURATION:
                return createConfiguration();
            case SidiffconfigPackage.CONFIGURATION_ELEMENT:
                return createConfigurationElement();
            case SidiffconfigPackage.EQUALITY:
                return createEquality();
            case SidiffconfigPackage.SIMILARITY:
                return createSimilarity();
            default:
                throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
        }
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Configuration createConfiguration() {
        ConfigurationImpl configuration = new ConfigurationImpl();
        return configuration;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ConfigurationElement createConfigurationElement() {
        ConfigurationElementImpl configurationElement = new ConfigurationElementImpl();
        return configurationElement;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Equality createEquality() {
        EqualityImpl equality = new EqualityImpl();
        return equality;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Similarity createSimilarity() {
        SimilarityImpl similarity = new SimilarityImpl();
        return similarity;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public SidiffconfigPackage getSidiffconfigPackage() {
        return (SidiffconfigPackage) getEPackage();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
    @Deprecated
    public static SidiffconfigPackage getPackage() {
        return SidiffconfigPackage.eINSTANCE;
    }
}
