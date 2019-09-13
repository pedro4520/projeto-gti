package user.example.com.testegti.Util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import user.example.com.testegti.R;


public class AdapterListViewEvento extends BaseAdapter {
    private Activity activity;
    private LayoutInflater mInflater;
    private List<Evento> itens;

    SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");


    public AdapterListViewEvento(Activity activity, List<Evento> itens)
    {
        this.activity = activity;
        this.itens = itens;
    }

    /**
     * Retorna a quantidade de itens
     *
     * @return
     */
    public int getCount()
    {
        int tamanho = 0;
        try{
           tamanho = itens.size();
        }catch (Exception e){

        }
        return tamanho;
    }

    public Evento getItem(int position)
    {
        return itens.get(position);
    }

    public long getItemId(int position)
    {
        return position;
    }

    public View getView(int position, View view, ViewGroup parent) {
        if (mInflater == null) {
            mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if (view == null) {
            view = mInflater.inflate(R.layout.item_listview_evento, null);
        }

        TextView tvNome = (TextView) view.findViewById(R.id.tvNome);
        TextView tvDescricao = (TextView) view.findViewById(R.id.tvDescricao);
        TextView tvData = (TextView) view.findViewById(R.id.tvData);
        ImageView imgInfo = (ImageView) view.findViewById(R.id.imgView);

        SpannableString spanString = new SpannableString(itens.get(position).getNome());
        spanString.setSpan(new UnderlineSpan(), 0, spanString.length(), 0);
        spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString.length(), 0);
        spanString.setSpan(new StyleSpan(Typeface.ITALIC), 0, spanString.length(), 0);

        try {
            Bitmap bitmap = BitmapFactory.decodeByteArray(itens.get(position).getImagem(), 0,
                    itens.get(position).getImagem().length);
            imgInfo.setImageBitmap(bitmap);
        }catch(Exception e){
            imgInfo.setImageResource(R.drawable.no_picture);
        }

        tvNome.setText(spanString);
        tvDescricao.setText(itens.get(position).getDescricao());
        tvData.setText("Data: "+fmt.format(itens.get(position).getDataDate()));

        return view;
    }

}
