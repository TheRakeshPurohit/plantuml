/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2024, Arnaud Roques
 *
 * Project Info:  https://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * https://plantuml.com/patreon (only 1$ per month!)
 * https://plantuml.com/paypal
 * 
 * This file is part of PlantUML.
 *
 * PlantUML is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PlantUML distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 *
 * Original Author:  Arnaud Roques
 * 
 *
 */
package net.sourceforge.plantuml.eggs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.PlainDiagram;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.jaws.JawsStrange;
import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColorSet;
import net.sourceforge.plantuml.klimt.color.HColorSimple;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.font.UFont;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.UDrawable;
import net.sourceforge.plantuml.klimt.shape.UPolygon;
import net.sourceforge.plantuml.klimt.shape.URectangle;
import net.sourceforge.plantuml.klimt.sprite.SpriteContainerEmpty;
import net.sourceforge.plantuml.preproc.PreprocessingArtifact;
import net.sourceforge.plantuml.skin.Pragma;
import net.sourceforge.plantuml.text.BackSlash;

// http://www.redblobgames.com/grids/hexagons/
public class PSystemColors extends PlainDiagram implements UDrawable {

	private final double rectangleHeight = 28;
	private final double rectangleWidth = 175;
	private final HColorSet colors = HColorSet.instance();
	private final String paletteCentralColor;
	private final double size = 60;

	public PSystemColors(UmlSource source, String option, PreprocessingArtifact preprocessing) {
		super(source, preprocessing);
		if (option == null)
			this.paletteCentralColor = null;
		else
			this.paletteCentralColor = option.replaceAll("\\#", "");

	}

	@Override
	protected UDrawable getRootDrawable(FileFormatOption fileFormatOption) {
		return this;
	}

	public DiagramDescription getDescription() {
		return new DiagramDescription("(Colors)");
	}

	public void drawU(UGraphic ug) {
		if (paletteCentralColor != null && colors.getColorOrWhite(paletteCentralColor) instanceof HColorSimple)
			drawPalette(ug);
		else
			drawFull(ug);
	}

	private void drawPalette(UGraphic ug) {
		double x = (centerHexa(2, 0).getX() + centerHexa(3, 0).getX()) / 2;
		double y = centerHexa(0, 2).getY() + corner(1).getY();
		ug = ug.apply(new UTranslate(x, y));
		final UPolygon hexa = getHexa();

		final List<String> friends = getColorsCloseTo(paletteCentralColor);
		int idx = 0;
		drawOneHexa(ug, friends.get(idx++), 0, 0, hexa);

		drawOneHexa(ug, friends.get(idx++), 1, 0, hexa);
		drawOneHexa(ug, friends.get(idx++), 0, 1, hexa);
		drawOneHexa(ug, friends.get(idx++), -1, 1, hexa);
		drawOneHexa(ug, friends.get(idx++), -1, 0, hexa);
		drawOneHexa(ug, friends.get(idx++), -1, -1, hexa);
		drawOneHexa(ug, friends.get(idx++), 0, -1, hexa);

		drawOneHexa(ug, friends.get(idx++), 2, 0, hexa);
		drawOneHexa(ug, friends.get(idx++), 1, 1, hexa);
		drawOneHexa(ug, friends.get(idx++), 1, 2, hexa);
		drawOneHexa(ug, friends.get(idx++), 0, 2, hexa);
		drawOneHexa(ug, friends.get(idx++), -1, 2, hexa);
		drawOneHexa(ug, friends.get(idx++), -2, 1, hexa);
		drawOneHexa(ug, friends.get(idx++), -2, 0, hexa);
		drawOneHexa(ug, friends.get(idx++), -2, -1, hexa);
		drawOneHexa(ug, friends.get(idx++), -1, -2, hexa);
		drawOneHexa(ug, friends.get(idx++), 0, -2, hexa);
		drawOneHexa(ug, friends.get(idx++), 1, -2, hexa);
		drawOneHexa(ug, friends.get(idx++), 1, -1, hexa);
	}

	private XPoint2D centerHexa(int i, int j) {
		final double width = getWidth();
		final double x = width * i + (j % 2 == 0 ? 0 : width / 2);
		final double y = size * j * 1.5;
		return new XPoint2D(x, y);

	}

	private double getWidth() {
		return Math.sqrt(3) / 2 * 2 * size;
	}

	private void drawOneHexa(UGraphic ug, String colorName, int i, int j, UPolygon hexa) {
		final HColor color = colors.getColorOrWhite(colorName);
		ug = applyColor(ug, color);
		ug = ug.apply(UTranslate.point(centerHexa(i, j)));
		ug.draw(hexa);

		final UFont font = UFont.sansSerif(14).bold();

		TextBlock tt = getTextName(font, colorName, color);
		XDimension2D dimText = tt.calculateDimension(ug.getStringBounder());
		if (dimText.getWidth() > getWidth()) {
			tt = getTextName(font, findShortest(ug.getStringBounder(), font, colorName), color);
			dimText = tt.calculateDimension(ug.getStringBounder());
		}
		tt.drawU(ug.apply(new UTranslate(-dimText.getWidth() / 2, -dimText.getHeight() / 2)));
	}

	@JawsStrange
	private String findShortest(StringBounder stringBounder, UFont font, String colorName) {
		String result = null;
		double min = Double.MAX_VALUE;
		for (int i = 1; i < colorName.length() - 1; i++) {
			if (Character.isLowerCase(colorName.charAt(i)))
				continue;

			final String candidat = colorName.substring(0, i) + BackSlash.BS_BS_N + colorName.substring(i);
			final TextBlock tt = getTextName(font, candidat, (HColorSimple) HColors.BLACK);
			final double width = tt.calculateDimension(stringBounder).getWidth();
			if (width < min) {
				result = candidat;
				min = width;
			}
		}
		return result;
	}

	private UGraphic applyColor(UGraphic ug, HColor color) {
		return ug.apply(color).apply(color.bg());
	}

	private XPoint2D corner(int i) {
		double angle_deg = 60 * i + 30;
		double angle_rad = Math.PI / 180 * angle_deg;
		return new XPoint2D(size * Math.cos(angle_rad), size * Math.sin(angle_rad));
	}

	private UPolygon getHexa() {
		final UPolygon result = new UPolygon();
		for (int i = 0; i < 6; i++)
			result.addPoint(corner(i));

		return result;
	}

	private List<String> getColorsCloseTo(String other) {
		final List<String> result = new ArrayList<>(colors.names());
		for (Iterator<String> it = result.iterator(); it.hasNext();) {
			final String candidat = it.next();
			final String similar = candidat.replaceAll("Gray", "Grey");
			if (candidat.equals(similar))
				continue;

			if (result.contains(similar))
				it.remove();

		}
		if (containsCaseInsensitive(result, other) == false)
			result.add(other);

		Collections.sort(result, closeComparator(paletteCentralColor));
		return result;
	}

	private boolean containsCaseInsensitive(Collection<String> source, String target) {
		for (String s : source)
			if (s.equalsIgnoreCase(target))
				return true;

		return false;
	}

	private Comparator<String> closeComparator(String center) {
		final HColorSimple centerColor = (HColorSimple) colors.getColorOrWhite(center);
		return new Comparator<String>() {
			public int compare(String col1, String col2) {
				final int dist1 = centerColor.distanceTo((HColorSimple) colors.getColorOrWhite(col1));
				final int dist2 = centerColor.distanceTo((HColorSimple) colors.getColorOrWhite(col2));
				return (int) Math.signum(dist1 - dist2);
			}
		};
	}

	private void drawFull(UGraphic ug) {
		final UFont font = UFont.sansSerif(14).bold();

		ug = ug.apply(HColors.BLACK);
		int i = 0;
		int j = 0;
		for (String name : colors.names()) {
			UGraphic tmp = getPositioned(ug, i, j);
			final HColor color = colors.getColorOrWhite(name);
			applyColor(tmp, color).draw(URectangle.build(rectangleWidth, rectangleHeight));
			final TextBlock tt = getTextName(font, name, color);
			final XDimension2D dimText = tt.calculateDimension(ug.getStringBounder());
			final double dy = (rectangleHeight - dimText.getHeight()) / 2;
			final double dx = (rectangleWidth - dimText.getWidth()) / 2;
			tt.drawU(tmp.apply(new UTranslate(dx, dy)));
			if (j++ == 20) {
				j = 0;
				i++;
			}
		}
	}

	private TextBlock getTextName(final UFont font, String name, final HColor color) {
		final HColor opposite = color.opposite();
		final FontConfiguration fc = FontConfiguration.create(font, opposite, HColors.BLUE, UStroke.simple());
		final TextBlock tt = Display.getWithNewlines(Pragma.createEmpty(), name).create(fc, HorizontalAlignment.CENTER,
				new SpriteContainerEmpty());
		return tt;
	}

	private UGraphic getPositioned(UGraphic ug, int i, int j) {
		return ug.apply(new UTranslate(rectangleWidth * i, rectangleHeight * j));
	}

}
