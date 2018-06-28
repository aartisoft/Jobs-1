package nithra.jobs.career.placement.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import nithra.jobs.career.placement.R;
import nithra.jobs.career.placement.activity.DetailActivity;
import nithra.jobs.career.placement.engine.DBHelper;

/**
 * Created by Ravi Tamada on 21/02/17.
 * www.androidhive.info
 */

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.MyViewHolder> {
    private Context mContext;
    private List<String> values;
    DBHelper dbHelper;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView title;
        public RelativeLayout parentLay;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            parentLay = view.findViewById(R.id.parent_lay);
        }
    }

    public SubCategoryAdapter(Context mContext, List<String> values) {
        this.mContext = mContext;
        this.values = values;
        dbHelper = new DBHelper(mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.subcategory_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.title.setText(values.get(position));
        holder.parentLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str;
                Cursor c=dbHelper.getQry("SELECT details FROM govtexams where exam = '" + values.get(position) +"'");
                c.moveToFirst();
                do{
                    str=c.getString(c.getColumnIndexOrThrow("details"));

                }while(c.moveToNext());
                c.close();

                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra("message", str);
                intent.putExtra("title", values.get(position));
                intent.putExtra("url", "");
                intent.putExtra("idd", "");
                intent.putExtra("Noti_add", 1);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

}