package nithra.jobs.career.placement.adapters;

/**
 * Created by nithra-apps on 8/11/17.
 */

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import nithra.jobs.career.placement.R;

public class MyAdapter extends BaseAdapter {

    PackageManager pm;
    java.util.List<ResolveInfo>  listApp;
    Context context;

    public MyAdapter(Context context1,PackageManager pManager, java.util.List<ResolveInfo>  listApp1) {
        pm = pManager;
        listApp = listApp1;
        context = context1;
    }

    class ViewHolder {
        ImageView ivLogo;
        TextView tvAppName;
        TextView tvPackageName;
    }

    @Override
    public int getCount() {
        return listApp.size();
    }

    @Override
    public Object getItem(int position) {
        return listApp.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_share_app, parent, false);
            holder.ivLogo = convertView.findViewById(R.id.iv_logo);
            holder.tvAppName = convertView.findViewById(R.id.tv_app_name);
            holder.tvPackageName = convertView.findViewById(R.id.tv_app_package_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ResolveInfo appInfo = listApp.get(position);
        holder.ivLogo.setImageDrawable(appInfo.loadIcon(pm));
        holder.tvAppName.setText(appInfo.loadLabel(pm));
        holder.tvPackageName.setText(appInfo.activityInfo.packageName);

        return convertView;
    }

}
