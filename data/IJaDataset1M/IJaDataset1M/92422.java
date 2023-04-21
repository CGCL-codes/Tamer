package org.dbe.composer.wfengine.bpel.server.deploy.validate;

import org.dbe.composer.wfengine.SdlException;
import org.dbe.composer.wfengine.bpel.ISdlConstants;
import org.dbe.composer.wfengine.bpel.def.validation.ISdlBaseErrorReporter;
import org.dbe.composer.wfengine.bpel.server.deploy.bpr.IBprFile;

/**
 * Perform validation on the IBprFile before attempting to
 * create deployment objects.
 */
public interface IPredeploymentValidator extends ISdlConstants {

    /** location attribute */
    public static final String LOCATION_ATTR = "location";

    /** namespace attribute */
    public static final String NAMESPACE_ATTR = "namespace";

    /** name attribute */
    public static final String NAME_ATTR = "name";

    /** service attribute */
    public static final String SERVICE_ATTR = "service";

    /** classpath attribute */
    public static final String CLASSPATH_ATTR = "classpath";

    /** targetNamespace attribute */
    public static final String TARGET_NAMESPACE_ATTR = "targetNamespace";

    /** myRole element constant */
    public static final String MYROLE_ELEMENT = "myRole";

    /** partnerRole element constant */
    public static final String PARTNERROLE_ELEMENT = "partnerRole";

    /** ServiceName element constant */
    public static final String SERVICE_NAME_ELEMENT = "ServiceName";

    /** schemaEntry element constant */
    public static final String SCHEMA_ENTRY_ELEMENT = "schemaEntry";

    /** import element constant */
    public static final String IMPORT_ELEMENT = "import";

    /** definitions element constant */
    public static final String DEFINITIONS_ELEMENT = "definitions";

    /** definitions element constant */
    public static final String DEFINITION_ELEMENT = "definition";

    /** partnerLink element constant */
    public static final String PARTNER_LINK_ELEMENT = "partnerLink";

    /** sdl element constant */
    public static final String SDL_ELEMENT = "sdl";

    /** sdlEntry element constant */
    public static final String SDL_ENTRY_ELEMENT = "sdlEntry";

    /** wsdl element constant */
    public static final String WSDL_ELEMENT = "wsdl";

    /** wsdlEntry element constant */
    public static final String WSDL_ENTRY_ELEMENT = "wsdlEntry";

    /**
     * Validate the bpr file.
     * @param aBprFile the deployment bpr
     * @param aReporter absorbs error and warning messages
     * @throws SdlException
     */
    public void validate(IBprFile aBprFile, ISdlBaseErrorReporter aReporter) throws SdlException;
}
