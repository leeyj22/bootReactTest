package com.bf.common.element;

import javax.xml.bind.annotation.XmlElement;

public class MapElements {
	@XmlElement public String key;
	@XmlElement public String value;
	
	private MapElements() {}
	public MapElements(String key, String value) {
		this.key = key;
		this.value = value;
	}
}
