package javax.resource.cci;

/**
 * ConnectionSpec is used by an application component to pass connection
 * request-specific properties to the ConnectionFactory. getConnection method.
 * 
 * <p>
 * It is recommended that the ConnectionSpec interface be implemented as a
 * JavaBean to support tools. The properties on the ConnectionSpec
 * implementation class must be defined through the getter and setter methods
 * pattern.
 * 
 * <p>
 * The CCI specification defines a set of standard properties for an
 * ConnectionSpec. The properties are defined either on a derived interface or
 * an implementation class of an empty ConnectionSpec interface. In addition, a
 * resource adapter may define additional properties specific to its underlying
 * EIS.
 * 
 * @author Rahul Sharma
 * @version 1.0 Public Draft 1
 * @see javax.resource.cci.ConnectionFactory
 **/
public interface ConnectionSpec {
}
