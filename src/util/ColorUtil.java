package util;

import java.awt.Color;

public class ColorUtil
{
	static double	BLACK	= 20;
	static double	YELLOW	= 70;

	public static void printColor(Color color)
	{
		System.out.println("Red:" + color.getGreen() + " Green:" + color.getGreen() + "Blue:" + color.getBlue());
	}


	public static Color getAlphaMergeColor(Color color1, Color color2)
	{
		int r = (color1.getRed() + color2.getRed()) / 2;
		int g = (color1.getGreen() + color2.getGreen()) / 2;
		int b = (color1.getBlue() + color2.getBlue()) / 2;
		return new Color(r, g, b);
	}

	public static double[] RGB2Lab2(Color color)
	{
		int r = color.getRed();
		int g = color.getGreen();
		int b = color.getBlue();
		return RGB2Lab(r, g, b);
	}

	public static Color Lab2RGB2(double value[])
	{
		return Lab2RGB(value[0], value[1], value[2]);
	}

	static double[] RGB2Lab(double R, double G, double B)
	{
		double X, Y, Z, fX, fY, fZ, L, a, b;

		X = 0.412453 * R + 0.357580 * G + 0.180423 * B;
		Y = 0.212671 * R + 0.715160 * G + 0.072169 * B;
		Z = 0.019334 * R + 0.119193 * G + 0.950227 * B;

		X /= (255 * 0.950456);
		Y /= 255;
		Z /= (255 * 1.088754);

		if (Y > 0.008856)
		{
			fY = Math.pow(Y, 1.0 / 3.0);
			L = 116.0 * fY - 16.0;
		}
		else
		{
			fY = 7.787 * Y + 16.0 / 116.0;
			L = 903.3 * Y;
		}

		if (X > 0.008856)
			fX = Math.pow(X, 1.0 / 3.0);
		else
			fX = 7.787 * X + 16.0 / 116.0;

		if (Z > 0.008856)
			fZ = Math.pow(Z, 1.0 / 3.0);
		else
			fZ = 7.787 * Z + 16.0 / 116.0;

		a = 500.0 * (fX - fY);
		b = 200.0 * (fY - fZ);

		if (L < BLACK)
		{
			a *= Math.exp((L - BLACK) / (BLACK / 4));
			b *= Math.exp((L - BLACK) / (BLACK / 4));
			L = BLACK;
		}
		if (b > YELLOW)
			b = YELLOW;

		double[] value = new double[3];
		value[0] = L;
		value[1] = a;
		value[2] = b;
		return value;
	}

	static Color Lab2RGB(double L, double a, double b)
	{
		double R, G, B;
		double X, Y, Z, fX, fY, fZ;
		double RR, GG, BB;

		fY = Math.pow((L + 16.0) / 116.0, 3.0);
		if (fY < 0.008856)
			fY = L / 903.3;
		Y = fY;

		if (fY > 0.008856)
			fY = Math.pow(fY, 1.0 / 3.0);
		else
			fY = 7.787 * fY + 16.0 / 116.0;

		fX = a / 500.0 + fY;
		if (fX > 0.206893)
			X = Math.pow(fX, 3.0);
		else
			X = (fX - 16.0 / 116.0) / 7.787;

		fZ = fY - b / 200.0;
		if (fZ > 0.206893)
			Z = Math.pow(fZ, 3.0);
		else
			Z = (fZ - 16.0 / 116.0) / 7.787;

		X *= (0.950456 * 255);
		Y *= 255;
		Z *= (1.088754 * 255);

		RR = 3.240479 * X - 1.537150 * Y - 0.498535 * Z;
		GG = -0.969256 * X + 1.875992 * Y + 0.041556 * Z;
		BB = 0.055648 * X - 0.204043 * Y + 1.057311 * Z;

		R = (float) (RR < 0 ? 0 : RR > 255 ? 255 : RR);
		G = (float) (GG < 0 ? 0 : GG > 255 ? 255 : GG);
		B = (float) (BB < 0 ? 0 : BB > 255 ? 255 : BB);

		return new Color((int) R, (int) G, (int) B);
	}
}
