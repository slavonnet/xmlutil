/*
 * Copyright (c) 2018.
 *
 * This file is part of xmlutil.
 *
 * xmlutil is free software: you can redistribute it and/or modify it under the terms of version 3 of the
 * GNU Lesser General Public License as published by the Free Software Foundation.
 *
 * xmlutil is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with xmlutil.  If not,
 * see <http://www.gnu.org/licenses/>.
 */

package nl.adaptivity.xmlutil.serialization.canary

import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.SerialKind
import kotlinx.serialization.internal.MissingDescriptorException
import nl.adaptivity.xmlutil.serialization.XmlDefault

interface ExtSerialDescriptor: SerialDescriptor {
    fun getSafeElementDescriptor(index: Int): SerialDescriptor? {
        return try { getElementDescriptor(index) } catch (e: MissingDescriptorException) { null }
    }
}

/**
 * An implementation of SerialDescriptor
 *
 * @param base The serial class descriptor that is the basis. This is used for delegation so not directly accessible
 * @property extKind The SerialKind for the property
 * @param isChildNullable An array marking for all elements whether they are nullable
 * @param childDescriptors An array with all child descriptors
 */
internal class ExtSerialDescriptorImpl(
    private val base: SerialDescriptor,
    val extkind: SerialKind,
    private val childDescriptors: Array<SerialDescriptor>
) : ExtSerialDescriptor {

    override val isNullable: Boolean get() = false

    override val name: String get() = base.name
    override val kind: SerialKind get() = base.kind

    override fun getElementName(index: Int): String = base.getElementName(index)
    override fun getElementIndex(name: String): Int = base.getElementIndex(name)

    override fun getEntityAnnotations(): List<Annotation> = base.getEntityAnnotations()

    override fun getElementAnnotations(index: Int) =
            if (index < elementsCount) base.getElementAnnotations(index) else emptyList()

    override val elementsCount: Int get() = base.elementsCount

    override fun getElementDescriptor(index: Int): SerialDescriptor = childDescriptors[index]

    override fun isElementOptional(index: Int): Boolean = getElementAnnotations(index).any { it is XmlDefault }

    override fun toString(): String {
        return buildString {
            append(name)
            (0 until elementsCount).joinTo(this, prefix = "(", postfix = ")") { idx ->
                "${getElementName(idx)}:${getElementDescriptor(idx).name}${if (childDescriptors[idx].isNullable) "?" else ""}"
            }
        }
    }
}

class NullableSerialDescriptor(val original: SerialDescriptor): SerialDescriptor by original {
    override val isNullable: Boolean get() = true
}
