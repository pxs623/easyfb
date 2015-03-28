package com.pxs;

import java.io.Serializable;

public class viewBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String title;
	private String titleType;
	private String module;
	private String buttonText;
	private String functionName;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTitleType() {
		return titleType;
	}
	public void setTitleType(String titleType) {
		this.titleType = titleType;
	}
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
	}
	public String getButtonText() {
		return buttonText;
	}
	public void setButtonText(String buttonText) {
		this.buttonText = buttonText;
	}
	public String getFunctionName() {
		return functionName;
	}
	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}
	@Override
	public String toString() {
		return "viewBean [title=" + title + ", titleType=" + titleType
				+ ", module=" + module + ", buttonText=" + buttonText
				+ ", functionName=" + functionName + "]";
	}
}
