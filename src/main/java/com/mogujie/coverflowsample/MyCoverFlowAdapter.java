package com.mogujie.coverflowsample;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.dolphinwang.imagecoverflow.CoverFlowAdapter;

import java.util.List;

public class MyCoverFlowAdapter extends CoverFlowAdapter {

    private Context mContext;
    private boolean dataChanged;
    private List<Hero> mData;
    private Bitmap result;
    private boolean loaded;

    public MyCoverFlowAdapter(Context context, List<Hero> data) {
        this.mContext = context;
        this.mData = data;
    }

    public void changeBitmap(List<Hero> newData) {
        dataChanged = true;
        mData = newData;

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Bitmap getImage(final int position) {
        Hero hero = mData.get(position);
        if(hero != null){
            String heroName = mData.get(position).getName().replace("npc_dota_hero_","");
            int id = mContext.getResources().getIdentifier(heroName + "_vert", "drawable", mContext.getPackageName());

            Bitmap bm = BitmapFactory.decodeResource(mContext.getResources(), id);
            return bm;
        }else
        {
            return null;
        }

    }
}
