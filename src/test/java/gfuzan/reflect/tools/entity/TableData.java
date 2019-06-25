package gfuzan.reflect.tools.entity;

/**
 * 平铺数据表
 *
 */
public class TableData {

    /**
     * 一级名
     */
    private String aName;
    /**
     * 一级code
     */
    private String aCode;
    /**
     * 二级名
     */
    private String bName;
    /**
     * 二级code
     */
    private String bCode;
    /**
     * 三级名
     */
    private String cName;

    /**
     * 三级code
     */
    private String cCode;
    /**
     * 四级名
     */
    private String dName;
    /**
     * 四级code
     */
    private String dCode;

    public String getaName() {
        return aName;
    }

    public void setaName(String aName) {
        this.aName = aName;
    }

    public String getaCode() {
        return aCode;
    }

    public void setaCode(String aCode) {
        this.aCode = aCode;
    }

    public String getbName() {
        return bName;
    }

    public void setbName(String bName) {
        this.bName = bName;
    }

    public String getbCode() {
        return bCode;
    }

    public void setbCode(String bCode) {
        this.bCode = bCode;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public String getcCode() {
        return cCode;
    }

    public void setcCode(String cCode) {
        this.cCode = cCode;
    }

    public TableData(String aName, String aCode, String bName, String bCode, String cName, String cCode, String dName,
            String dCode) {
        this.aName = aName;
        this.aCode = aCode;
        this.bName = bName;
        this.bCode = bCode;
        this.cName = cName;
        this.cCode = cCode;
        this.dName = dName;
        this.dCode = dCode;
    }

    public String getdName() {
        return dName;
    }

    public void setdName(String dName) {
        this.dName = dName;
    }

    public String getdCode() {
        return dCode;
    }

    public void setdCode(String dCode) {
        this.dCode = dCode;
    }

}
