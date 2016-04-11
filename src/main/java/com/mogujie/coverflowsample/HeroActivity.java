package com.mogujie.coverflowsample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

public class HeroActivity extends Activity {

    private ImageView _imgHeroLarge;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hero);
        _imgHeroLarge = (ImageView)findViewById(R.id.imgHeroLarge);

        Context context = _imgHeroLarge.getContext();

        Intent intent = getIntent();
        Hero hero = (Hero) intent.getSerializableExtra("hero");

        String heroName = hero.getName().replace("npc_dota_hero_","");

        int id = this.getResources().getIdentifier(heroName + "_vert", "drawable", this.getPackageName());

        Bitmap bm = BitmapFactory.decodeResource(this.getResources(), id);
        _imgHeroLarge.setImageBitmap(bm);
        //Picasso.with(context).load(String.format("http://cdn.dota2.com/apps/dota2/images/heroes/%s_vert.jpg",heroName)).into();
    }
}
