package com.volantis.map.ics.imageprocessor.parameters;

import com.volantis.map.common.param.MutableParameters;

/**
 * General implementation of the ParamBuilder, this param builder defines
 * useful staff for other param builders.
 */
public abstract class AbstractParamBuilder {

    /**
     * Sets int parameter. The value -1 treated as invalid value and isn't
     * set.
     *
     * @param paramName  - parameter name.
     * @param paramValue - parameter value.
     * @param params     - parameter container to set parameter to.
     * @throws ParameterBuilderException if it is impossible to set parameter.
     */
    protected void setIntParam(String paramName, int paramValue, MutableParameters params) throws ParameterBuilderException {
        if (paramValue != -1) {
            params.setParameterValue(paramName, Integer.toString(paramValue));
        }
    }

    /**
     * Sets long parameter. The value -1 treated as invalid value and isn't
     * set.
     *
     * @param paramName  - parameter name.
     * @param paramValue - parameter value.
     * @param params     - parameter container to set parameter to.
     * @throws ParameterBuilderException if it is impossible to set parameter.
     */
    protected void setIntParam(String paramName, long paramValue, MutableParameters params) throws ParameterBuilderException {
        if (paramValue != -1) {
            params.setParameterValue(paramName, Long.toString(paramValue));
        }
    }

    /**
     * Sets boolean parameter.
     *
     * @param paramName  - parameter name.
     * @param paramValue - parameter value.
     * @param params     - parameter container to set parameter to.
     * @throws ParameterBuilderException if it is impossible to set parameter.
     */
    protected void setBooleanParam(String paramName, boolean paramValue, MutableParameters params) throws ParameterBuilderException {
        params.setParameterValue(paramName, Boolean.toString(paramValue));
    }

    /**
     * Sets string parameter. null and empty string treated as invalid value
     * and isn't set.
     *
     * @param paramName  - parameter name.
     * @param paramValue - parameter value.
     * @param params     - parameter container to set parameter to.
     * @throws com.volantis.map.ics.imageprocessor.parameters.ParameterBuilderException if it is impossible to set parameter.
     */
    protected void setStringParam(String paramName, String paramValue, MutableParameters params) throws ParameterBuilderException {
        if (paramValue != null && paramValue != "") {
            params.setParameterValue(paramName, paramValue);
        }
    }
}
