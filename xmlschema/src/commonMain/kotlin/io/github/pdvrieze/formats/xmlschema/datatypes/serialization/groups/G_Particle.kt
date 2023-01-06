/*
 * Copyright (c) 2021.
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

package io.github.pdvrieze.formats.xmlschema.datatypes.serialization.groups

import io.github.pdvrieze.formats.xmlschema.datatypes.serialization.types.T_GroupRef
import io.github.pdvrieze.formats.xmlschema.datatypes.serialization.types.T_LocalElement

/**
 * Choice:  T_LocalElement, T_GroupRef, E_All, E_Choice, E_Sequence, E_Any
 */
interface G_Particle {

    val particle: Base

    interface Base
    sealed interface SealedBase : Base

    interface Element : SealedBase, T_LocalElement
    interface Group : SealedBase, T_GroupRef
    interface All : SealedBase
    interface Choice : SealedBase
    interface Sequence : SealedBase
    interface Any : SealedBase
}

