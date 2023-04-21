package com.endlessloopsoftware.egonet.interfaces;

/**
 * Local home interface for QuestionEJB.
 */
public interface QuestionEJBLocalHome extends javax.ejb.EJBLocalHome {

    public static final String COMP_NAME = "java:comp/env/ejb/QuestionEJBLocal";

    public static final String JNDI_NAME = "QuestionEJBLocalHome";

    public com.endlessloopsoftware.egonet.interfaces.QuestionEJBLocal create(com.endlessloopsoftware.egonet.util.QuestionDataValue data) throws javax.ejb.CreateException;

    public com.endlessloopsoftware.egonet.interfaces.QuestionEJBLocal findByPrimaryKey(com.endlessloopsoftware.egonet.interfaces.QuestionEJBPK pk) throws javax.ejb.FinderException;
}
