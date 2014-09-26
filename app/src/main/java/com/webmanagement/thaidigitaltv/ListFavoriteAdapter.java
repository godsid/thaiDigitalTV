package com.webmanagement.thaidigitaltv;

/**
 * Created by SystemDLL on 25/9/2557.
 */


    import android.content.Context;
    import android.database.DataSetObserver;
    import android.util.Log;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.Adapter;
    import android.widget.BaseAdapter;
    import android.widget.ImageView;
    import android.widget.TextView;
    import android.widget.Toast;

    import com.androidquery.AQuery;

    import org.json.JSONException;
    import org.json.JSONObject;

    import java.util.ArrayList;

    /**
     * Created by Banpot.S on 9/4/14 AD.
     */
    public class ListFavoriteAdapter extends BaseAdapter {

        ArrayList <DataCustomListView> arrayList = new ArrayList<DataCustomListView>();
        private LayoutInflater mInflater;
        private int p;


        public ListFavoriteAdapter(Context context,ArrayList<DataCustomListView> arrayList){
            this.arrayList = arrayList;
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return arrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            p = position;
            if(convertView==null){
                convertView = mInflater.inflate(R.layout.custom_favorite_list,null);
            }


            TextView TV_fav_list_title = (TextView) convertView.findViewById(R.id.tv_fav_list_title);
            TextView TV_fav_time_show = (TextView) convertView.findViewById(R.id.tv_fav_time_show);
            TextView TV_fav_cha_title = (TextView) convertView.findViewById(R.id.tv_fav_cha_title);


              //  convertView.setTag(objectView);

            TV_fav_list_title.setText( arrayList.get(position).list_title);
            TV_fav_time_show.setText(arrayList.get(position).time_show);
            TV_fav_cha_title.setText(arrayList.get(position).cha_title);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("run",""+arrayList.get(p).list_id+","+arrayList.get(p).list_title+","+arrayList.get(p).cha_title);
                }
            });

            return convertView;
        }

        public void testtost () {

        }

    }




    class DataCustomListView{
        String list_title,cha_title,time_show;
        int list_id;

        public DataCustomListView(int list_id,String list_title,String cha_title,String time_show){
            this.list_id = list_id;
            this.list_title = list_title;
            this.cha_title = cha_title;
            this.time_show = time_show;
        }

        public int getListId() {
            return list_id;
        }

        public void setListId(int list_id) {
            this.list_id = list_id;
        }

        public String getListTitle() {
            return list_title;
        }

        public void setListTitle(String list_title) {
            this.list_title = list_title;
        }

        public String getChaTitle() {
            return cha_title;
        }

        public void setChaTitle(String cha_title) {
            this.cha_title = cha_title;
        }

        public String getTimeShow() {
            return time_show;
        }

        public void setTimeShow(String time_show) {
            this.time_show = time_show;
        }
    }




