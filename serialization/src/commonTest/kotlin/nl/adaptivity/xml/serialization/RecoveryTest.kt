/*
 * Copyright (c) 2021.
 *
 * This file is part of xmlutil.
 *
 * This file is licenced to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You should have received a copy of the license with the source distribution.
 * Alternatively, you may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package nl.adaptivity.xml.serialization

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import nl.adaptivity.xmlutil.QName
import nl.adaptivity.xmlutil.serialization.UnknownChildHandler
import nl.adaptivity.xmlutil.serialization.XML
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class RecoveryTest {

    @Serializable
    data class Data(
        val a: String,
        val b: String
    )

    @OptIn(ExperimentalSerializationApi::class)
    @Test
    fun testDeserializeRecovering() {
        val serialized ="<Data a=\"foo\" c=\"bar\" />"

        val xml = XML {
            unknownChildHandler = UnknownChildHandler { input, inputKind, descriptor, name, candidates ->
                assertEquals(QName("c"), name)
                listOf(
                    XML.ParsedData<String?>(
                        1,
                        input.getAttributeValue(name!!)
                    )
                )
            }
        }
        val parsed:Data = xml.decodeFromString(serialized)
        val expected = Data("foo", "bar")
        assertEquals(expected, parsed)
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Test
    fun testDeserializeRecoveringNotProvidingRequired() {
        val serialized ="<Data a=\"foo\" c=\"bar\" />"

        val xml = XML {
            unknownChildHandler = UnknownChildHandler { input, inputKind, descriptor, name, candidates ->
                assertEquals(QName("c"), name)
                emptyList()
            }
        }
        val e = assertFailsWith<SerializationException> { xml.decodeFromString<Data>(serialized) }
        assertEquals("Field 'b' is required for type with serial name 'nl.adaptivity.xml.serialization.RecoveryTest.Data', but it was missing", e.message)
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Test
    fun testDeserializeRecoveringDuplicateData() {
        val serialized ="<Data a=\"foo\" c=\"bar\" />"

        val xml = XML {
            unknownChildHandler = UnknownChildHandler { input, inputKind, descriptor, name, candidates ->
                assertEquals(QName("c"), name)
                listOf(
                    XML.ParsedData<String?>(
                        1,
                        input.getAttributeValue(name!!)
                    ),
                    XML.ParsedData<String>(1, "baz"),
                )
            }
        }
        val d = xml.decodeFromString<Data>(serialized)
        assertEquals("baz", d.b)
    }

}
