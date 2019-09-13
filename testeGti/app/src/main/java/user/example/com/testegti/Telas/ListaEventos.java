package user.example.com.testegti.Telas;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import user.example.com.testegti.R;
import user.example.com.testegti.Util.AdapterListViewEvento;
import user.example.com.testegti.Util.Conecta;
import user.example.com.testegti.Util.Evento;
import user.example.com.testegti.Util.OrdenaEventoData;

public class ListaEventos extends AppCompatActivity {

    private List<Evento> listaEvento = new ArrayList<>();
    private ProgressDialog progressDialog;
    private AdapterListViewEvento adapterListView;
    ListView lvEventos;

    Timer timer = null;//variavel para executar função de tempo para encerrar sessão
    long TEMPO = 1000 * 60 ;//1 minuto tempo do loop

    private AlertDialog alerta;//para avisar que a sessão expirou


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_eventos);

        getSupportActionBar().hide();

        lvEventos = (ListView) findViewById(R.id.lvEvento);

        progressDialog = new ProgressDialog(this);
        new AsyncCarregaEvento().execute("");

    }



    private class AsyncCarregaEvento extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {   // deve ser nessa ordem o sincroniza dados
            buscaLista();
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                progressDialog.dismiss();
                adapterListView = new AdapterListViewEvento(ListaEventos.this, listaEvento);
                lvEventos.setAdapter(adapterListView);
                adapterListView.notifyDataSetChanged();
            }catch (Exception e){

            }
            adapterListView.notifyDataSetChanged();

        }

        @Override
        protected void onPreExecute() {
            try {
                progressDialog.setTitle("Aguarde");
                progressDialog.setMessage("Buscando eventos...");
                progressDialog.setCancelable(false);

                progressDialog.show();
            }catch (Exception e){
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {}

    }

    private void buscaLista(){
        Conecta conecta = new Conecta();
        listaEvento = conecta.buscaEventos();

        for(Evento e : listaEvento){
            e.transforma();
        }

        Collections.sort(listaEvento, new OrdenaEventoData());

        SharedPreferences pref = getSharedPreferences("preferencias", Context.MODE_PRIVATE);
        //System.out.println("FICOU: "+pref.getString("token","não encontrado"));
        //System.out.println("SENHA: "+pref.getInt("tempo",0));

        iniciaTimer(pref.getInt("tempo",1));
    }

    private void iniciaTimer(final int tempo){
        TEMPO /= tempo;
        System.out.println("FICOU: "+TEMPO);
        if (timer == null) {
            timer = new Timer();
            TimerTask tarefa = new TimerTask() {
                public void run() {
                    try {
                        encerra();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            timer.scheduleAtFixedRate(tarefa, TEMPO, TEMPO);
        }
    }

    private void encerra(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                popUpEncerrarSessao();
            }
        });
    }

    private void popUpEncerrarSessao(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyDialogTheme);//Cria o gerador do AlertDialog
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        final Button bOption1 = new Button(this);

        bOption1.setText("Confirmar");

        bOption1.setTextSize(35);

        bOption1.setBackgroundResource(R.drawable.transparent_button);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0,0,0,10);

        bOption1.setLayoutParams(params);

        bOption1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            timer = null;
            finish();
            overridePendingTransition(0,0);
            alerta.dismiss();
            }
        });


        layout.setPadding(50, 40, 50, 10);
        layout.addView(bOption1);

        builder.setView(layout);
        builder.setTitle("Sessão");//define o titulo
        builder.setMessage("A sua sessão expirou.");
        //define um botão como positivo
        builder.setPositiveButton("Sair", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                timer = null;
                finish();
            }
        });

        alerta = builder.create();//cria o AlertDialog
        try {
            alerta.show();//Exibe
        }catch (Exception e){}
    }

}
