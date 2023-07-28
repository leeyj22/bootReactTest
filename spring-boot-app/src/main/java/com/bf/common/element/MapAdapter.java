package com.bf.common.element;

import com.bf.common.element.MapElements;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.HashMap;
import java.util.Map;

public class MapAdapter extends XmlAdapter<MapElements[], Map<String, String>> {
	public MapElements[] marshal(Map<String, String> arg0) throws Exception {
		if (arg0 == null) {
			return new MapElements[0];
		}
		MapElements[] mapElements = new MapElements[arg0.size()];
		int i = 0;
		for (Map.Entry<String, String> entry : arg0.entrySet()) {
			mapElements[i++] = new MapElements(entry.getKey(), entry.getValue());
		}
		return mapElements;
	}
	
	public Map<String, String> unmarshal(MapElements[] arg0) throws Exception {
		Map<String, String> r = new HashMap<String, String>();
		if (arg0 == null) {
			return r;
		}
		for(MapElements mapElements : arg0) {
			r.put(mapElements.key, mapElements.value);
		}
		return r;
	}
}
