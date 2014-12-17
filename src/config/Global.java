package config;

import java.awt.Color;

public class Global

{
	public static final int	width			= 1280;
	public static final int	height			= 720;

	public static final int	WHITE_VALUE		= -1;
	public static final int	BLACK_VALUE		= -16777216;

	public static int		SAMPLE_DIST		= 10;

	public static float		BRUSH_WDITH		= 20;

	public static String[]	strokelibaries	= { "charcoal1", "charcoal2", "dry_watercolor", "fingercolor1", "glitter", "lipcolor1", "lipcolor2", "lipcolor3",
			"lipstick1", "marker1", "mixedGel", "oil0", "oil2", "pastel1", "pencil", "pencilSmudged", "playdoh", "toothpaste1", "watercolor1", "watercolor2" };

	public static String	Libary			= "charcoal1";

	public static String[]	colorStrings	= { "BLUE", "BLACK", "CYAN", "DARK_GRAY", "GRAY", "GREEN", "LIGHT_GRAY", "MAGENTA", "ORANGE", "PINK", "RED",
			"YELLOW"						};

	public static Color[]	brushColors		= { Color.BLACK, Color.BLUE, Color.CYAN, Color.DARK_GRAY, Color.GRAY, Color.GREEN, Color.LIGHT_GRAY, Color.MAGENTA,
			Color.ORANGE, Color.PINK, Color.RED, Color.YELLOW };

	public static Color		BRUSH_COLOR		= brushColors[0];

}
