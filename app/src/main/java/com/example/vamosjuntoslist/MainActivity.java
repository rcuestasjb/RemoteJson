package com.example.vamosjuntoslist;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

//1) Definicion de la clase evento; esta clase la utilizaremos para rellenar una lista
class Evento {
    private String categoria;
    private String descripcion;
    private String dia;
    private String hora;
    private String id;
    private String titulo;

    public Evento(String titulo) {
        this.titulo = titulo;
    }

    public String getTitulo() { return this.titulo; }
}

public class MainActivity extends AppCompatActivity {

    //Definición de la interface de retrofit (aqui definimos nuestro endpoint)
    interface Api {
        //String ENDPOINT = "http://puigverd.org/";
        String ENDPOINT = "https://raw.githubusercontent.com/rcuestasjb/m07/master/";
        @GET("list.json")
        Call<List<Evento>> getEventos();
    }

    //Componente de la lista de eventos
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //1) Creacion de la activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //2) Obtenemos el id del list View, mas tarde la rellenaremos
        listView = (ListView) findViewById(R.id.listViewEventos);

        //3) Preparamos la llamada al endpoint con retrpfit-
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Api.ENDPOINT).
                addConverterFactory(GsonConverterFactory.create()).build();

        Api api = retrofit.create(Api.class);

        //3) Realizamos la llamada asincrona es decir no bloquea el programa
        Call<List<Evento>> call = api.getEventos();
        call.enqueue(new Callback<List<Evento>>() {
            //3.1) En caso de obtener una respuesta correcta, http responce 200
            @Override
            public void onResponse(Call<List<Evento>> call, Response<List<Evento>> response) {
                //3.2) Recojemos el resultadao de la peticion es decir el json
                List<Evento> eventosList = response.body();

                //3.3)Creamos un array de strings con el numero de eventos
                String[] eventos = new String[eventosList.size()];
                //Rellenamos el array con el Titulo del  los eventos
                for (int i = 0; i < eventosList.size(); i++) {
                    eventos[i] = eventosList.get(i).getTitulo();
                }

                //3.4) Asignamos el array de string al listview de la activity, simple_list_item_1 es el tipo de lista por defecto se puede diseñar una mas chuli
                listView.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, eventos));
            }

            //3.5) En caso de obtener una respuesta correcta, http responce 404 500 etc
            @Override
            public void onFailure(Call<List<Evento>> call, Throwable t) {
                Log.d("error","Error fatal!!!!!");
            }
        });
    }
}
