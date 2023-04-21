package org.jowidgets.api.widgets.blueprint.builder;

import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.widgets.builder.IFrameSetupBuilderCommon;

public interface IFrameSetupBuilder<INSTANCE_TYPE extends IFrameSetupBuilder<?>> extends ITitledWindowSetupBuilder<INSTANCE_TYPE>, IContainerSetupBuilder<INSTANCE_TYPE>, IFrameSetupBuilderCommon<INSTANCE_TYPE> {

    INSTANCE_TYPE setMinSize(Dimension size);
}
