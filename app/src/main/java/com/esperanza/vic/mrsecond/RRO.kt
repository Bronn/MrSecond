package com.esperanza.vic.kotlinxmlparsingusingxmlpullparser.com.esperanza.vic.mrsecond

import org.xmlpull.v1.XmlPullParser

import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.IOException
import java.io.InputStream



class RRO {
    var id: Int = 0
    var name: String? = null
    var serial_number: String? = null
    var status:Boolean = false
    val l2git:Boolean = false
    override fun toString(): String {
        return " Id = $id\n Name = $name\n Serial Number = $serial_number"
    }
}

class CSHB_EVENT {
 //   var event_timestamp : timestamp;

//  TODO:


}

class XmlLogParserHandler {






}

class XmlPullParserHandler {
    private val rros = ArrayList<RRO>()
    private var rro: RRO? = null
    private var text: String? = null


    fun parse(inputStream: InputStream): List<RRO> {
        var i : Int =0;
        try {
            val factory = XmlPullParserFactory.newInstance()
            factory.isNamespaceAware = true
            val parser = factory.newPullParser()
            parser.setInput(inputStream, null)
            var eventType = parser.eventType
            while (eventType != XmlPullParser.END_DOCUMENT) {
                val tagname = parser.name
                when (eventType) {
                    XmlPullParser.START_TAG -> if (tagname.equals("dev", ignoreCase = true)) {
                        rro = RRO()
                        i++
                        rro!!.id =i
                        rro!!.name="ЭККА на ТТ Энская"
                        rro!!.serial_number =  parser.getAttributeValue(0)
                    }
                        XmlPullParser.TEXT-> text = parser.text
                        XmlPullParser.END_TAG -> if (tagname.equals("dev", ignoreCase = true)) {
                        rro?.let { rros.add(it) }
                        }

                    else -> {
                    }
                }
                eventType = parser.next()
            }
        } catch (e: XmlPullParserException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return rros
        }
    }





