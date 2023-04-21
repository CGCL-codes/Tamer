package model.templates;

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
 * @see model.templates.TemplatesFactory
 * @model kind="package"
 * @generated
 */
public interface TemplatesPackage extends EPackage {

    /**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    String eNAME = "templates";

    /**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    String eNS_URI = "tpl";

    /**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    String eNS_PREFIX = "tpl";

    /**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    TemplatesPackage eINSTANCE = model.templates.impl.TemplatesPackageImpl.init();

    /**
	 * The meta object id for the '{@link model.templates.impl.TemplateImpl <em>Template</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see model.templates.impl.TemplateImpl
	 * @see model.templates.impl.TemplatesPackageImpl#getTemplate()
	 * @generated
	 */
    int TEMPLATE = 0;

    /**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int TEMPLATE__NAME = 0;

    /**
	 * The number of structural features of the '<em>Template</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int TEMPLATE_FEATURE_COUNT = 1;

    /**
	 * The meta object id for the '{@link model.templates.impl.LayoutImpl <em>Layout</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see model.templates.impl.LayoutImpl
	 * @see model.templates.impl.TemplatesPackageImpl#getLayout()
	 * @generated
	 */
    int LAYOUT = 1;

    /**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int LAYOUT__NAME = 0;

    /**
	 * The feature id for the '<em><b>Sections</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int LAYOUT__SECTIONS = 1;

    /**
	 * The number of structural features of the '<em>Layout</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int LAYOUT_FEATURE_COUNT = 2;

    /**
	 * The meta object id for the '{@link model.templates.impl.SectionImpl <em>Section</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see model.templates.impl.SectionImpl
	 * @see model.templates.impl.TemplatesPackageImpl#getSection()
	 * @generated
	 */
    int SECTION = 2;

    /**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int SECTION__NAME = 0;

    /**
	 * The number of structural features of the '<em>Section</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int SECTION_FEATURE_COUNT = 1;

    /**
	 * Returns the meta object for class '{@link model.templates.Template <em>Template</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Template</em>'.
	 * @see model.templates.Template
	 * @generated
	 */
    EClass getTemplate();

    /**
	 * Returns the meta object for the attribute '{@link model.templates.Template#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see model.templates.Template#getName()
	 * @see #getTemplate()
	 * @generated
	 */
    EAttribute getTemplate_Name();

    /**
	 * Returns the meta object for class '{@link model.templates.Layout <em>Layout</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Layout</em>'.
	 * @see model.templates.Layout
	 * @generated
	 */
    EClass getLayout();

    /**
	 * Returns the meta object for the attribute '{@link model.templates.Layout#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see model.templates.Layout#getName()
	 * @see #getLayout()
	 * @generated
	 */
    EAttribute getLayout_Name();

    /**
	 * Returns the meta object for the reference '{@link model.templates.Layout#getSections <em>Sections</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Sections</em>'.
	 * @see model.templates.Layout#getSections()
	 * @see #getLayout()
	 * @generated
	 */
    EReference getLayout_Sections();

    /**
	 * Returns the meta object for class '{@link model.templates.Section <em>Section</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Section</em>'.
	 * @see model.templates.Section
	 * @generated
	 */
    EClass getSection();

    /**
	 * Returns the meta object for the attribute '{@link model.templates.Section#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see model.templates.Section#getName()
	 * @see #getSection()
	 * @generated
	 */
    EAttribute getSection_Name();

    /**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
    TemplatesFactory getTemplatesFactory();

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
		 * The meta object literal for the '{@link model.templates.impl.TemplateImpl <em>Template</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see model.templates.impl.TemplateImpl
		 * @see model.templates.impl.TemplatesPackageImpl#getTemplate()
		 * @generated
		 */
        EClass TEMPLATE = eINSTANCE.getTemplate();

        /**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute TEMPLATE__NAME = eINSTANCE.getTemplate_Name();

        /**
		 * The meta object literal for the '{@link model.templates.impl.LayoutImpl <em>Layout</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see model.templates.impl.LayoutImpl
		 * @see model.templates.impl.TemplatesPackageImpl#getLayout()
		 * @generated
		 */
        EClass LAYOUT = eINSTANCE.getLayout();

        /**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute LAYOUT__NAME = eINSTANCE.getLayout_Name();

        /**
		 * The meta object literal for the '<em><b>Sections</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EReference LAYOUT__SECTIONS = eINSTANCE.getLayout_Sections();

        /**
		 * The meta object literal for the '{@link model.templates.impl.SectionImpl <em>Section</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see model.templates.impl.SectionImpl
		 * @see model.templates.impl.TemplatesPackageImpl#getSection()
		 * @generated
		 */
        EClass SECTION = eINSTANCE.getSection();

        /**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute SECTION__NAME = eINSTANCE.getSection_Name();
    }
}
