package echeverria.reynaldo.myfeelings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONException
import utilities.CustomBarDrawable
import utilities.CustomCircleDrawable
import utilities.Emociones
import utilities.JSONFile

class MainActivity : AppCompatActivity() {

        var jsonFile: JSONFile? = null
        var veryHappy = 0.0F
        var happy = 0.0F
        var neutral = 0.0F
        var sad = 0.0F
        var verySad = 0.0F
        var data: Boolean = false
        var lista = ArrayList<Emociones>()



    fun fetchingData(){
        try{
            var json :String = jsonFile?.getData(this) ?: ""
            if(json != ""){
                this.data = true
                var jsonArray:JSONArray = JSONArray(json)

                this.lista = parseJson(jsonArray)

                for(i in lista){
                    when(i.nombre){
                        "Muy Feliz" -> veryHappy = i.total
                        "Feliz" -> happy = i.total
                        "Neutral" -> neutral = i.total
                        "Triste"-> sad = i.total
                        "Muy Triste" -> verySad = i.total
                    }
                }
            }else{
                this.data = false
            }
        }catch(exception:JSONException){
            exception.printStackTrace()
        }
    }

    fun iconoMayoria() {

        if (happy > veryHappy && happy > neutral && happy > sad && happy > verySad) {
            icon.setImageDrawable(resources.getDrawable(R.drawable.ic_sentiment_satisfied_black_24dp))
        }
        if (veryHappy > happy && veryHappy > neutral && veryHappy > sad && veryHappy > verySad) {
            icon.setImageDrawable(resources.getDrawable(R.drawable.ic_sentiment_very_satisfied_black_24dp))
        }
        if (neutral > happy && neutral > happy && neutral > sad && happy > verySad) {
            icon.setImageDrawable(resources.getDrawable(R.drawable.ic_sentiment_neutral_black_24dp))
        }
        if (sad > happy && sad > neutral && sad > veryHappy && sad > verySad) {
            icon.setImageDrawable(resources.getDrawable(R.drawable.ic_sentiment_dissatisfied_black_24dp))
        }
        if (verySad > happy && verySad> neutral && verySad > sad && veryHappy < verySad) {
            icon.setImageDrawable(resources.getDrawable(R.drawable.ic_sentiment_very_dissatisfied_black_24dp))
        }
    }



    fun actualizarGrafica(){
        val total = veryHappy+happy+neutral+verySad+sad

        var pVH : Float= (veryHappy * 100 / total).toFloat()
        var pH :Float = (happy * 100/total).toFloat()
        var pN: Float = (neutral*100 /total).toFloat()
        var pS: Float = (verySad*100/ total).toFloat()

        Log.d("porcentajes","very happy "+pVH)
        Log.d("porcentajes"," happy "+pVH)
        Log.d("porcentajes","neutral "+pVH)
        Log.d("porcentajes","sad "+pVH)
        Log.d("porcentajes","very sad "+pVH)

        lista.clear()
        lista.add(Emociones("Muy feliz", pVH,R.color.mustard,veryHappy))
        lista.add(Emociones("Feliz", pVH,R.color.orange,happy))
        lista.add(Emociones("Neutral", pVH,R.color.greenie,neutral))
        lista.add(Emociones("Triste", pVH,R.color.blue,sad))
        lista.add(Emociones("Muy triste", pVH,R.color.deepBlue,verySad))

        val fondo = CustomCircleDrawable(this,lista)

        graphVeryHappy.background = CustomBarDrawable(this,Emociones("Muy feliz",pVH,R.color.mustard,veryHappy))
        graphHappy.background = CustomBarDrawable(this,Emociones("Feliz",pVH,R.color.orange,happy))
        graphNeutral.background = CustomBarDrawable(this,Emociones("Neutral",pVH,R.color.greenie,neutral))
        graphSad.background = CustomBarDrawable(this,Emociones("Triste",pVH,R.color.blue,sad))
        graphVerySad.background = CustomBarDrawable(this,Emociones("Muy triste",pVH,R.color.deepBlue,verySad))

        graph.background = fondo




    }

    fun parseJson(jsonArray: JSONArray):ArrayList<Emociones>{
        var lista = ArrayList<Emociones>()

        for(i in 0..jsonArray.length()){
            try{
                val nombre = jsonArray.getJSONObject(i).getString("nombre")
                val porcentaje = jsonArray.getJSONObject(i).getDouble("porcentaje").toFloat()
                val color = jsonArray.getJSONObject(i).getInt("color")
                val total = jsonArray.getJSONObject(i).getDouble("total").toFloat()
                var emocion = Emociones(nombre,porcentaje,color,total)
                lista.add(emocion)
            }catch(exception:JSONException){
                exception.printStackTrace()
            }
        }

        return lista
    }

    fun guardar(){

    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        jsonFile = JSONFile()

        fetchingData()
        if(!data){
            var emociones = ArrayList<Emociones>()
            val fondo = CustomCircleDrawable(this,emociones)
            graph.background = fondo
            graphVeryHappy.background = CustomBarDrawable(this,Emociones("Muy Feliz",0.0F,R.color.mustard,veryHappy))
            graphHappy.background = CustomBarDrawable(this,Emociones("Feliz",0.0F,R.color.orange,happy))
            graphNeutral.background = CustomBarDrawable(this,Emociones("Neutral",0.0F,R.color.greenie,neutral))
            graphSad.background = CustomBarDrawable(this,Emociones("Triste",0.0F,R.color.blue,sad))
            graphVerySad.background = CustomBarDrawable(this,Emociones("Muy Triste",0.0F,R.color.deepBlue,verySad))
        }else{
            actualizarGrafica()
            iconoMayoria()
        }

        guardarButton.setOnClickListener{
            guardar()
        }

        verySadButton.setOnClickListener{
            verySad--
            iconoMayoria()
            actualizarGrafica()
        }

        verySatisfiedButton.setOnClickListener{
            veryHappy++
            iconoMayoria()
            actualizarGrafica()
        }

        sadButton.setOnClickListener{
            sad--
            iconoMayoria()
            actualizarGrafica()
        }

        happyButton.setOnClickListener{
            happy++
            iconoMayoria()
            actualizarGrafica()
        }

        neutralButton.setOnClickListener{
            neutral++
            iconoMayoria()
            actualizarGrafica()

        }
    }


}
