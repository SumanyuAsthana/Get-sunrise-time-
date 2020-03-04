package com.example.startup

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import javax.net.ssl.HttpsURLConnection

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    fun getSunRise(view: View) {
        var city = enterCity.text.toString()

        val url =
            "http://api.openweathermap.org/data/2.5/weather?q=$city&APPID=c73bcbf7966e0693904c855ade44b748"
        myAsyncTask().execute(url)
    }
    inner class myAsyncTask:AsyncTask<String,String,String>(){
        override fun onPreExecute() {

        }
        override fun doInBackground(vararg params: String?): String {
            try{
                val url=URL(params[0])
                val urlConnect=url.openConnection() as HttpURLConnection
                urlConnect.connectTimeout=7000
                var inString=ConvertStreamToString(urlConnect.inputStream)
                publishProgress(inString)
            }catch (ex:Exception){}
            return " "
        }

        override fun onPostExecute(result: String?) {

        }

        override fun onProgressUpdate(vararg values: String?) {
            try{
                var json=JSONObject(values[0])
                var sys=json.getJSONObject("sys")
                var sunrise=sys.getLong(  "sunrise")
                seeTime.text="Sunrise time for ${enterCity.text} is:-"+tString(sunrise)
            }catch (ex:Exception){seeTime.text="Error"}
        }
    }
    fun ConvertStreamToString(inputStream: InputStream):String{
        val bufferedReader=BufferedReader(InputStreamReader(inputStream))
        var line:String
        var AllString=""
        try{
            do{
                line=bufferedReader.readLine()
                if(line!=null){
                    AllString+=line
                }
            }while(line!=null)
            inputStream.close()
        }catch (ex:Exception){}
        return AllString
    }
    fun tString(l:Long):String?{

        var date=Date(l*1000)
        var hours=date.hours
        var minutes=date.minutes
        var seconds=date.seconds
        return "$hours:$minutes:$seconds am"
    }
}