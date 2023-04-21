package it.aco.mandragora.vo;

import java.util.Collection;

public class CopyNode21VO extends ValueObject {

    private String idCopyNode21;

    private String description;

    private Collection<CopyNode211VO> copyNode211VOs;

    public String getIdCopyNode21() {
        return idCopyNode21;
    }

    public void setIdCopyNode21(String idCopyNode21) {
        this.idCopyNode21 = idCopyNode21;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Collection<CopyNode211VO> getCopyNode211VOs() {
        return copyNode211VOs;
    }

    public void setCopyNode211VOs(Collection<CopyNode211VO> copyNode211VOs) {
        this.copyNode211VOs = copyNode211VOs;
    }
}
