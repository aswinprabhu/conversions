package com.wavefront.labs.convert.converter.rrd.models.graph;

public enum GraphItemType {

	AREA(Area.class),
	COMMENT(DefaultGraphItem.class),
	GPRINT(DefaultGraphItem.class),
	HRULE(HRule.class),
	LINE(Line.class),
	PRINT(DefaultGraphItem.class),
	SHIFT(Shift.class),
	STACK(Stack.class),
	TEXTALIGN(DefaultGraphItem.class),
	TICK(DefaultGraphItem.class),
	VRULE(DefaultGraphItem.class),
	UNKNOWN(DefaultGraphItem.class);

	private final Class type;

	GraphItemType(Class type) {
		this.type = type;
	}

	public Class getType() {
		return type;
	}
}
