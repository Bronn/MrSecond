package com.esperanza.vic.mrsecond


import android.os.Bundle
import android.os.StrictMode
import android.provider.DocumentsContract
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.esperanza.vic.kotlinxmlparsingusingxmlpullparser.com.esperanza.vic.mrsecond.RRO
import com.esperanza.vic.kotlinxmlparsingusingxmlpullparser.com.esperanza.vic.mrsecond.XmlPullParserHandler
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.w3c.dom.NodeList
import java.io.IOException
import java.util.*
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathFactory


class MainActivity : AppCompatActivity() {


    val URL   = "https://api.icndb.com/jokes/random"
    val URL_GW = "http://172.17.17.75:12702/lsoft"
    val URL_GW_LOG = "http://172.17.17.75:81/mt400gw_log"
    val URL_VPN = "http://172.17.17.33/vpn_status/"


    override fun onCreate(savedInstanceState: Bundle?) {
 // Пиздец Гугл заебал немного своими капризами про прайваси
  val policy: StrictMode.ThreadPolicy = StrictMode.ThreadPolicy.Builder().permitAll().build();
     /*   val policy = StrictMode.ThreadPolicy.Builder()
            .detectAll()
            .penaltyLog()
            .build()
       */
        StrictMode.setThreadPolicy(policy)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btRequest1.setOnClickListener() { run();   }
    }




    private val client = OkHttpClient()


    fun run() {
        val postBody ="<?xml version='1.0'?>\n" +
                "<srv_req>\n" +
                " <select greenlist=\"1\"/>\n" +
                "</srv_req>\n"
       .trimMargin()

        var     rro: List<RRO>? = null
        val request = Request.Builder()
            .url("http://172.17.17.33:12702/lsoft")
            .post(postBody.toRequestBody(MEDIA_TYPE_MARKDOWN))
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")
            // разбор
            try {
                val parser = XmlPullParserHandler()
                val isstream = response.body!!.byteStream()
                rro = parser.parse(isstream)
                val adapter = ArrayAdapter(
                    this,
                    android.R.layout.simple_list_item_1,
                    rro as List<RRO>
                )
                ListView.adapter=adapter
            }
            catch (e: IOException){
                e.printStackTrace()
            }
        }





    }

    companion object {
        val MEDIA_TYPE_MARKDOWN = "text/x-markdown; charset=utf-8".toMediaType()

    }





    fun getElementValuesByAttributeNameAndAttributeValue(
        doc: DocumentsContract.Document,
        attributeValue: String,
        attributeName: String
    ): List<String> {
        val xpFactory = XPathFactory.newInstance()
        val xPath = xpFactory.newXPath()
        //<item type="T1" count="1">Value1</item>
        val xpath = "/ItemSet/Item[contains(@$attributeName, '$attributeValue')]"
        val itemsTypeT1 = xPath.evaluate(xpath, doc, XPathConstants.NODESET) as NodeList
        val itemList: MutableList<String> = ArrayList()
        for (i in 0..itemsTypeT1.length - 1) {
         itemList.add(itemsTypeT1.item(i).textContent)
        }
        return ArrayList(itemList)

     }





}
