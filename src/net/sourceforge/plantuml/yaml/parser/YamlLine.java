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
 */
package net.sourceforge.plantuml.yaml.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import net.sourceforge.plantuml.annotation.DuplicateCode;

public class YamlLine {

	private final int indent;
	private final String key;
	private final String value;
	private final List<String> values;
	private final boolean listItem;
	private final YamlValueType type;

	public static Optional<YamlLine> build(String line) {
		int count = 0;
		while (count < line.length() && line.charAt(count) == ' ')
			count++;

		String trimmedLine = removeYamlComment(line.substring(count).trim());

		if (trimmedLine.isEmpty())
			return Optional.empty();

		final boolean listItem = trimmedLine.startsWith("- ");

		if (listItem) {
			count += 2;
			trimmedLine = trimmedLine.substring(2);
		}

		final int colonIndex = trimmedLine.indexOf(':');
		if (colonIndex == -1)
			if (listItem)
				return Optional.of(new YamlLine(count, null, unquote(trimmedLine), null,
						YamlValueType.PLAIN_ELEMENT_LIST, listItem));
			else
				return Optional.empty();

		final String rawKey = trimmedLine.substring(0, colonIndex).trim();
		final String rawValue = trimmedLine.substring(colonIndex + 1).trim();

		YamlValueType type = YamlValueType.REGULAR;

		if (rawValue.isEmpty())
			type = YamlValueType.ABSENT;
		else if (rawValue.equals("|"))
			type = YamlValueType.BLOCK_STYLE;
		else if (rawValue.equals(">"))
			type = YamlValueType.FOLDED_STYLE;
		else if (rawValue.startsWith("[") && rawValue.endsWith("]"))
			return Optional.of(new YamlLine(count, unquote(rawKey), null,
					toList(rawValue.substring(1, rawValue.length() - 1)), YamlValueType.FLOW_SEQUENCE, listItem));

		return Optional.of(new YamlLine(count, unquote(rawKey), unquote(rawValue), null, type, listItem));

	}

	private static List<String> toList(String rawValue) {
		final List<String> result = new ArrayList<>();
		for (String s : rawValue.split(","))
			result.add(unquote(s));
		return result;
	}

	private YamlLine(int indent, String key, String value, List<String> values, YamlValueType type, boolean listItem) {
		this.indent = indent;
		this.key = key;
		this.value = value;
		this.values = values;
		this.type = type;
		this.listItem = listItem;
	}

	private static String unquote(String str) {
		if (str == null || str.length() < 2)
			return str;

		final char first = str.charAt(0);
		final char last = str.charAt(str.length() - 1);
		if ((first == '"' && last == '"') || (first == '\'' && last == '\''))
			return str.substring(1, str.length() - 1);

		return str;
	}

	public int getIndent() {
		return indent;
	}

	@DuplicateCode(reference = "YamlLines")
	private static String removeYamlComment(String s) {
		if (s == null || s.isEmpty())
			return s;

		char inQuoteChar = '\0';

		if (s.charAt(0) == '#')
			return "";

		for (int i = 0; i < s.length(); i++) {
			final char c = s.charAt(i);

			if (c == '\'' || c == '"')
				if (inQuoteChar == '\0')
					inQuoteChar = c;
				else if (c == inQuoteChar)
					inQuoteChar = '\0';

			if (inQuoteChar == '\0' && i < s.length() - 1 && c == ' ' && s.charAt(i + 1) == '#')
				return s.substring(0, i);

		}

		return s;
	}

	public String getKey() {
		return key;
	}

	public String getValue() {
		if (type == YamlValueType.REGULAR || type == YamlValueType.FLOW_SEQUENCE
				|| type == YamlValueType.PLAIN_ELEMENT_LIST)
			return value;
		throw new IllegalStateException(type.name());
	}

	public boolean isListItem() {
		return listItem;
	}

	public YamlValueType getType() {
		return type;
	}

	public List<String> getValues() {
		if (type == YamlValueType.FLOW_SEQUENCE)
			return Collections.unmodifiableList(values);
		throw new IllegalStateException(type.name());
	}

	@Override
	public String toString() {
		return "YamlLine(indent=" + indent + ", key=" + key + ", value=" + value + ")";
	}

}
