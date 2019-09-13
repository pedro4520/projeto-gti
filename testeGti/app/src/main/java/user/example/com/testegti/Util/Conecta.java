package user.example.com.testegti.Util;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class Conecta {

    private int tempo = 0;
    private String token = "";

    public int getTempo() {
        return tempo;
    }

    public void setTempo(int tempo) {
        this.tempo = tempo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void verificaAcesso(String login, String senha){

        String url = "https://testapi.io/api/dti-triforce-trib/login";

        DefaultHttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("username", login));
        nameValuePairs.add(new BasicNameValuePair("pass", senha));
        try {
            post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        }catch(Exception e){}

        try{

            HttpResponse response = client.execute(post);

            JSONObject json = readJsonFromUrlPost(response);//pega objeto json

            //System.out.println(json.toString());
            try {
                token = json.get("token").toString();
                tempo = Integer.parseInt(json.get("tempoExpirar").toString());
            }catch (Exception e){e.printStackTrace();}


        }catch (Exception e){e.printStackTrace();}


    }

    public List<Evento> buscaEventos(){
        List<Evento> listaE = new ArrayList<>();

        try{

            String url = "https://testapi.io/api/dti-triforce-trib/eventos-mensais";//link dos eventos

            JSONObject json = readJsonFromUrl(url);//pega objeto json
            Gson g = new Gson();//biblioteca gson para ja pegar os objetos

            //System.out.println(json.toString());
            //System.out.println(json.get("eventos"));

            Type list = new TypeToken<List<Evento>>() {}.getType();//criando a lista
            listaE = new Gson().fromJson(json.get("eventos").toString(), list);//colocando o q esta no objeto eventos no list<Evento>

        }catch (Exception e){e.printStackTrace();}


        return listaE;
    }


    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }

    public static JSONObject readJsonFromUrlPost(HttpResponse response) throws  JSONException {
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

}
