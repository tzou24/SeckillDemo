package org.cckj.validatefile;

/**
 * @deprecated: 文件类型对应二进制文件头，文件头128位
 * @author Administrator
 *
 */
public enum FileType {

	/**
	 * JEPG. 前几个字节内容 "FFD8FF"
	 */
	JPEG("FFD8FF"),
	
	/**
	 * PNG.
	 */
	PNG("89504E47"),
	
	/**
	 * GIF.
	 */
	GIF("47494638"),
	
	/**
	 * TIFF.
	 */
	TIFF("49492A00"),
	
	/**
	 * Windows Bitmap
	 */
	BMP("424D"),
	
	/**
	 * CAD.
	 */
	DWG("41433130"),
	
	/**
	 * Adobe Photoshop
	 */
	PSD("38425053"),
	
	/**
	 * Adobe Acrobat
	 */
	PDF("255044462D312E"),
	
	/**
	 * XML.
	 */
	XML("3C3F786D6C"),
	
	/**
	 * HTML.
	 */
	HTML("68746D6C3E"),
	
	/**
	 * ZIP Archive
	 */
	ZIP("504B0304"),
	
	/**
	 * RAR Archive
	 */
	RAR("52617221"),
	
	/**
	 * Wave.
	 */
	WAV("57415645"),
	
	/**
	 * AVI.
	 */
	AVI("41564920");
	
	private String value = "";
	
	private FileType(String value){
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}
