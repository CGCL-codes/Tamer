package org.zend.webapi.internal.core.connection.request;

import java.util.Date;
import org.restlet.Request;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.representation.Representation;
import org.zend.webapi.core.connection.data.IResponseData.ResponseType;
import org.zend.webapi.core.connection.data.values.WebApiVersion;
import org.zend.webapi.core.connection.response.ResponseCode;

/**
 * Disable the two directives necessary for creating tracing dumps, this action
 * does not disable the code-tracing component. This action unsets the special
 * zend_monitor.developer_mode & zend_monitor.event_generate_trace_file
 * directives. Note that this action does not deactivate the component or any
 * other setting.
 * <p>
 * Limitations:
 * </p>
 * <p>
 * This action explicitly does not work on Zend Server 5.6.0 for IBMi.
 * </p>
 * 
 * Request Parameters:
 * <table border="1">
 * <tr>
 * <th>Parameter</th>
 * <th>Type</th>
 * <th>Required</th>
 * <th>Description</th>
 * </tr>
 * <tr>
 * <td>restartNow</td>
 * <td>Boolean</td>
 * <td>No</td>
 * <td>Perform a php restart as part of the action to apply the new settings,
 * defaults to true.</td>
 * </tr>
 * </table>
 * 
 * @author Wojciech Galanciak, 2012
 * 
 */
public class CodeTracingEnableRequest extends AbstractRequest {

    public static final MediaType FORM = MediaType.register("application/x-www-form-urlencoded", "Form");

    private static final ResponseCode[] RESPONSE_CODES = new ResponseCode[] { ResponseCode.OK, ResponseCode.ACCEPTED };

    public CodeTracingEnableRequest(WebApiVersion version, Date date, String keyName, String userAgent, String host, String secretKey) {
        super(version, date, keyName, userAgent, host, secretKey);
    }

    public String getUri() {
        return "/ZendServerManager/Api/codetracingEnable";
    }

    public Method getMethod() {
        return Method.POST;
    }

    /**
	 * Perform a php restart as part of the action to apply the new settings,
	 * defaults to true.
	 * 
	 * @param boolean value
	 */
    public void setRestartNow(boolean value) {
        addParameter("restartNow", value);
    }

    @Override
    protected ResponseCode[] getValidResponseCode() {
        return RESPONSE_CODES;
    }

    public ResponseType getExpectedResponseDataType() {
        return ResponseType.CODE_TRACING_STATUS;
    }

    @Override
    public void applyParameters(Request request) {
        Representation rep = new MultipartRepresentation(getParameters(), FORM);
        request.setEntity(rep);
    }
}
