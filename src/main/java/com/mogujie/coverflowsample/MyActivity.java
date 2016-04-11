package com.mogujie.coverflowsample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.dolphinwang.imagecoverflow.CoverFlowView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MyActivity extends Activity {

    protected static final String VIEW_LOG_TAG = "CoverFlowDemo";
    public static Map<Integer, Hero> heroes;
    private ArrayList<Hero> heroList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = LayoutInflater.from(this).inflate(R.layout.activity_main,
                null, false);
        setContentView(v);

        final CoverFlowView<MyCoverFlowAdapter> mCoverFlowView = (CoverFlowView<MyCoverFlowAdapter>) findViewById(R.id.coverflow);

        loadHeores();
        heroList = new ArrayList<Hero>(heroes.values());
        // specify an adapter (see also next example)
        Collections.sort(heroList, new Comparator<Hero>() {
            @Override public int compare(final Hero o1, final Hero o2) {
                if(o1.getLocalizedName() == null){
                    o1.setLocalizedName("");
                }
                if(o2.getLocalizedName() == null){
                    o2.setLocalizedName("");
                }
                return o1.getLocalizedName().compareTo(o2.getLocalizedName());
            }
        });

        final MyCoverFlowAdapter adapter = new MyCoverFlowAdapter(this,heroList);
        mCoverFlowView.setAdapter(adapter);
        mCoverFlowView
                .setCoverFlowListener(new CoverFlowView.CoverFlowListener<MyCoverFlowAdapter>() {

                    @Override
                    public void imageOnTop(
                            CoverFlowView<MyCoverFlowAdapter> view,
                            int position, float left, float top, float right,
                            float bottom) {
                        Log.e(VIEW_LOG_TAG, position + " on top!");
                    }

                    @Override
                    public void topImageClicked(
                            CoverFlowView<MyCoverFlowAdapter> view, int position) {

                        Intent push = new Intent(getApplicationContext(), HeroActivity.class);
                        push.putExtra("hero", heroList.get(position));
                        startActivity(push);

                    }

                    @Override
                    public void invalidationCompleted() {

                    }
                });

        mCoverFlowView
                .setTopImageLongClickListener(new CoverFlowView.TopImageLongClickListener() {

                    @Override
                    public void onLongClick(int position) {
                        Log.e(VIEW_LOG_TAG, "top image long clicked == >"
                                + position);
                    }
                });

        findViewById(R.id.change_bitmap_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.changeBitmap();
            }
        });
    }

    private void loadHeores() {
        heroes = new HashMap<Integer, Hero>();

        try {

            // check sizeAssetManager am = getAssets();
            InputStream inputStream = getAssets().open("npc_heroes.txt");
            InputStreamReader isr = new InputStreamReader(inputStream);
            BufferedReader br = new BufferedReader(isr);
            String line = "";
            Hero hero = null;
            int chave = 0;
            Pattern pattern = Pattern.compile(".*\\\"(.*)\\\".*\\\"(.*)\\\".*");
            Pattern heroPattern = Pattern.compile(".*\\\"(.*)\\\".*");

            while ((line = br.readLine()) != null) {
                String trimmedLine = line.trim();
                if (trimmedLine.startsWith("//"))
                    continue;

                Matcher heroMatcher = heroPattern.matcher(line);
                if (hero == null && heroMatcher.find() &&
                        heroMatcher.group(1).startsWith("npc_dota_hero_") &&
                        !heroMatcher.group(1).equals("npc_dota_hero_base")) {
                    hero = new Hero();
                    hero.setName(heroMatcher.group(1));
                    continue;
                }

                if (hero != null && trimmedLine.startsWith("{")) {
                    chave++;
                    continue;
                }

                if (hero != null && trimmedLine.startsWith("}")) {
                    if (chave == 1) {
                        heroes.put(hero.getHeroID(), hero);
                        hero = null;
                    }
                    chave--;
                    continue;
                }

                Matcher matcher = pattern.matcher(line);
                if (hero != null && matcher.find()) {
                    try {
                        String fieldName = matcher.group(1);
                        String fieldValue = matcher.group(2);
                        Field field = Hero.class.getDeclaredField(fieldName.substring(0, 1).toLowerCase() + fieldName.substring(1));
                        field.setAccessible(true);
                        if (field.getType().equals(int.class)) {
                            field.set(hero, Integer.parseInt(fieldValue));
                        } else if (Collection.class.isAssignableFrom(field.getType())) {
                            ParameterizedType listType = (ParameterizedType) field.getGenericType();
                            Class<?> listClass = (Class<?>) listType.getActualTypeArguments()[0];

                            if (listClass.equals(Integer.class)) {
                                List<Integer> intList = new ArrayList<Integer>();
                                for (String s : fieldValue.split(","))
                                    intList.add(Integer.valueOf(s));
                                field.set(hero, intList);
                            } else {
                                field.set(hero, Arrays.asList(fieldValue.split(",")));
                            }
                        } else {
                            field.set(hero, fieldValue);
                        }
                    } catch (Exception e) {

                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
