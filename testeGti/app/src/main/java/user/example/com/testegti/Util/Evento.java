package user.example.com.testegti.Util;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

public class Evento implements Parcelable {

    private int id = 0;
    private String nome = "";
    private String descricao = "";
    private Date dataDate ;
    private long data;
    private String rotaImagem = "";
    private byte[] imagem;

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(nome);
        parcel.writeString(descricao);
        parcel.writeLong(data);
        parcel.writeString(rotaImagem);
    }

    protected Evento(Parcel in) {
        id = in.readInt();
        nome = in.readString();
        descricao = in.readString();
        data = in.readLong();
        rotaImagem = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Evento> CREATOR = new Creator<Evento>() {
        @Override
        public Evento createFromParcel(Parcel in) {
            return new Evento(in);
        }

        @Override
        public Evento[] newArray(int size) {
            return new Evento[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Date getDataDate() {
        return dataDate;
    }

    public void setDataDate(Date dataDate) {
        this.dataDate = dataDate;
    }

    public long getData() {
        return data;
    }

    public void setData(long data) {
        this.data = data;
    }

    public String getCaminhoImagem() {
        return rotaImagem;
    }

    public void setCaminhoImagem(String caminhoImagem) {
        this.rotaImagem = caminhoImagem;
    }

    public byte[] getImagem() {
        return imagem;
    }

    public void setImagem(byte[] imagem) {
        this.imagem = imagem;
    }

    @Override
    public String toString() {
        return "Evento{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", descricao='" + descricao + '\'' +
                ", dataDate=" + dataDate +
                ", data=" + data +
                ", caminhoImagem='" + rotaImagem + '\'' +
                '}';
    }

    public void transforma(){

        try {
            imagem = recoverImageFromUrl(rotaImagem);
        }catch (Exception e){
            //pega imagem padrão
        }

        try {
            dataDate = new Date(data);
        }catch (Exception e){
            dataDate = new Date();
        }


    }

    public byte[] recoverImageFromUrl(String urlText) throws Exception {//caminho da imagem para imagem
        URL url = new URL(urlText);
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try (InputStream inputStream = url.openStream()) {
            int n = 0;
            byte [] buffer = new byte[ 1024 ];
            while (-1 != (n = inputStream.read(buffer))) {
                output.write(buffer, 0, n);
            }
        }

        return output.toByteArray();
    }

}
