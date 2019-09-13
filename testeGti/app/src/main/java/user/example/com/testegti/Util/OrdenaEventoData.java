package user.example.com.testegti.Util;

public class OrdenaEventoData implements java.util.Comparator<Evento> {

    @Override
    public int compare(Evento o1, Evento o2) {

        try {
            if (o1.getDataDate().before(o2.getDataDate())){
                return -1;
            }else if (o1.getDataDate().after(o2.getDataDate())){
                return 1;
            }else{
                return 0;
            }

        } catch (Exception e) {
            return 0;
        }
    }
}
