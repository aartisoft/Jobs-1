package nithra.jobs.career.placement.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import nithra.jobs.career.placement.R;
import nithra.jobs.career.placement.activity.SubCategoryActivity;
import nithra.jobs.career.placement.engine.DBHelper;

/**
 * Created by Ravi Tamada on 21/02/17.
 * www.androidhive.info
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {
    private Context mContext;
    private List<String> values;
    DBHelper dbHelper;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView title;
        public RelativeLayout parentLay;
        ImageView imgLogo;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            parentLay = view.findViewById(R.id.parent_lay);
            imgLogo = view.findViewById(R.id.imgLogo);
        }
    }

    public CategoryAdapter(Context mContext, List<String> values) {
        this.mContext = mContext;
        this.values = values;
        dbHelper = new DBHelper(mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.title.setText(values.get(position));

        Cursor c = dbHelper.getQry("SELECT image FROM govtexams where board='" + values.get(position) + "'");
        c.moveToFirst();
        String imageName=c.getString(0);
        try {
            InputStream ims = mContext.getAssets().open("images/"+imageName+"");
            Drawable d = Drawable.createFromStream(ims, null);
            holder.imgLogo.setImageDrawable(d);
        }
        catch(IOException ex) {

        }
        holder.parentLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SubCategoryActivity.class);
                intent.putExtra("board",values.get(position));
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