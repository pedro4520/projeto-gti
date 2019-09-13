package user.example.com.testegti.Telas;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import user.example.com.testegti.R;
import user.example.com.testegti.Util.Conecta;

public class Login extends AppCompatActivity {

    Button btLogin;
    EditText etLogin, etSenha;
    TextView tvAviso;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        etLogin = (EditText) findViewById(R.id.etLogin);
        etSenha = (EditText) findViewById(R.id.etSenha);
        tvAviso = (TextView) findViewById(R.id.tvAviso);
        tvAviso.setVisibility(TextView.INVISIBLE);

        btLogin = findViewById(R.id.btLogin);
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validar();
            }
        });

        progressDialog = new ProgressDialog(this);

    }

    private void validar(){

        if(verificaConexao()==false){//verificar se tem acesso a internet
            Toast.makeText(Login.this, "Sem acesso a internet", Toast.LENGTH_LONG).show();
            return;
        }

        if(verificaCaracteres()) {//verifica caracteres especiais e se campo de senha possui pelo menos 6 caracteres

            new AsyncVerificaAcesso().execute("");//executa busca do token e tempo de expiração

            tvAviso.setVisibility(TextView.INVISIBLE);
        }else{
            tvAviso.setVisibility(TextView.VISIBLE);//se for falso mostra aviso do erro
        }

    }


    private boolean verificaConexao() {
        boolean conectado;
        ConnectivityManager conectivtyManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conectivtyManager.getActiveNetworkInfo() != null
                && conectivtyManager.getActiveNetworkInfo().isAvailable()
                && conectivtyManager.getActiveNetworkInfo().isConnected()) {
            conectado = true;
        } else {
            conectado = false;
        }
        return conectado;
    }

    private boolean verificaCaracteres(){

        String senha = etSenha.getText().toString();

        if(senha.length()<6){
            return false;
        }
        return true;
    }

    private class AsyncVerificaAcesso extends AsyncTask<String, Void, String> {

        boolean acesso = false;

        @Override
        protected String doInBackground(String... params) {   // deve ser nessa ordem o sincroniza dados
            acesso = verificaAcesso();
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                progressDialog.dismiss();
            }catch (Exception e){
            }
            if(acesso==true){
                mostraEventos();
            }
        }

        @Override
        protected void onPreExecute() {
            try {
                progressDialog.setTitle("Aguarde");
                progressDialog.setMessage("Verificando acesso...");
                progressDialog.setCancelable(false);

                progressDialog.show();
            }catch (Exception e){
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {}

    }


    private boolean verificaAcesso(){

        String login = etLogin.getText().toString();
        String senha = etSenha.getText().toString();

        Conecta conecta = new Conecta();
        conecta.verificaAcesso(login,senha);

        SharedPreferences preferences = getSharedPreferences("preferencias", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("token",conecta.getToken());
        editor.putInt("tempo",conecta.getTempo());

        editor.apply();

        //SharedPreferences pref = getSharedPreferences("preferencias", Context.MODE_PRIVATE);
        //System.out.println("FICOU: "+pref.getString("token","não encontrado"));
        //System.out.println("SENHA: "+pref.getInt("tempo",0));

        return true;

    }

    private void mostraEventos(){
        Intent i = new Intent();
        i.setClass(this, ListaEventos.class);
        startActivity(i);
    }


}
