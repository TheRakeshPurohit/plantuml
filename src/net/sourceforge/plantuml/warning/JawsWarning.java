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
package net.sourceforge.plantuml.warning;

import java.util.Arrays;
import java.util.List;

public enum JawsWarning {
	BACKSLASH_NEWLINE("\\n", "%newline() or %n()"), //
	BACKSLASH_LEFT("\\l", "%left_align()"), //
	BACKSLASH_RIGHT("\\r", "%right_align()"), //
	BACKSLASH_TABULATION("\\t", "%tab()"), //
	BACKSLASH_BACKSLASH("\\\\", "%backslash()");

	private final String ch;
	private final String function;

	JawsWarning(String ch, String function) {
		this.ch = ch;
		this.function = function;
	}

	public Warning toWarning() {

		final List<String> WARNINGS = Arrays.asList(
				"This diagram is using " + ch + " which is deprecated and will be removed in the future.",
				"You should use " + function + " instead in your diagram.");

		return new Warning(WARNINGS);
	}

}
