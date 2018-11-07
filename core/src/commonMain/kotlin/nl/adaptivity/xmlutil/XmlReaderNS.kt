/*
 * Copyright (c) 2018.
 *
 * This file is part of ProcessManager.
 *
 * ProcessManager is free software: you can redistribute it and/or modify it under the terms of version 3 of the
 * GNU Lesser General Public License as published by the Free Software Foundation.
 *
 * ProcessManager is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with ProcessManager.  If not,
 * see <http://www.gnu.org/licenses/>.
 */

@file:JvmName("XmlReaderNS")
package nl.adaptivity.xmlutil

import nl.adaptivity.xmlutil.EventType
import nl.adaptivity.xmlutil.XmlReader
import nl.adaptivity.xmlutil.skipPreamble
import nl.adaptivity.xmlutil.util.CompactFragment
import nl.adaptivity.xmlutil.util.ICompactFragment
import kotlin.jvm.JvmName

/**
 * Differs from [.siblingsToFragment] in that it skips the current event.
 *
 * @throws XmlException
 */
fun XmlReader.elementContentToFragment(): ICompactFragment {
    val r = this
    r.skipPreamble()
    if (r.hasNext()) {
        r.require(EventType.START_ELEMENT, null, null)
        r.next()
        return r.siblingsToFragment()
    }
    return CompactFragment("")
}

expect fun XmlReader.siblingsToFragment(): CompactFragment

@Suppress("DeprecatedCallableAddReplaceWith", "deprecation")
@Deprecated("This is inefficient in Javascript")
fun XmlReader.siblingsToCharArray() = siblingsToFragment().content

