package com.scf.web.comp.ace.ztree;
/**
 * Ztree的节点对象
 * @author wubin
 *
 */
public class NodeBean {
	
	private String id;//ID
	private String pId;//父Id
	private String name;//节点名称
	private Boolean open;//是否打开
	private Boolean isParent;//是否是父节点
	private String iconSkin;//图标
	private Boolean nocheck;//是否有checkbox
	private Boolean checked;//是否被checked
	private String t;//扩展字段
	
    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }

    public Boolean getNocheck() {
        return nocheck;
    }

    public void setNocheck(Boolean nocheck) {
        this.nocheck = nocheck;
    }

    public NodeBean(){
	    checked = false;
	    nocheck = false;
	    open = false;
	}
	
	public Boolean getIsParent() {
		return isParent;
	}

	public void setIsParent(Boolean isParent) {
		this.isParent = isParent;
	}
	
	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Boolean getOpen() {
		return open;
	}
	public void setOpen(Boolean open) {
		this.open = open;
	}
	public Boolean getChecked() {
		return checked;
	}
	public void setChecked(Boolean checked) {
		this.checked = checked;
	}


	public String getIconSkin() {
		return iconSkin;
	}

	public void setIconSkin(String iconSkin) {
		this.iconSkin = iconSkin;
	}
	


	
	
}