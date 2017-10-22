/*
 * Copyright 2016 drakeet. https://github.com/drakeet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.drakeet.multitype.sample.bilibili;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;

import com.flying.multi.MultiTypeAdapter;
import com.flying.multi.TypeItem;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.sample.R;

/**
 * @author drakeet
 */
public class MainActivity extends AppCompatActivity {

    private static class JsonData {

        private final String PREFIX = "这是一条长长的达到两行的标题文字";

        private Post post00 = new Post(R.drawable.img_00, PREFIX + "post00");
        private Post post01 = new Post(R.drawable.img_01, PREFIX + "post01");
        private Post post10 = new Post(R.drawable.img_10, PREFIX + "post10");
        private Post post11 = new Post(R.drawable.img_11, PREFIX + "post11");

        Category category0 = new Category("title0");
        Post[] postArray = {post00, post01, post10, post11};

        List<Post> postList = new ArrayList<>();


        {
            postList.add(post00);
            postList.add(post00);
            postList.add(post00);
            postList.add(post00);
            postList.add(post00);
            postList.add(post00);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // @formatter:off
        JsonData data = new JsonData();
        List<TypeItem> items = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            /* You also could use Category as your CategoryItemContent directly */
            items.add(new TypeItem(new CategoryItemContent(data.category0), null));
            items.add(new TypeItem(new PostRowItemContent(data.postArray[0], data.postArray[1]), null));
            items.add(new TypeItem(new PostRowItemContent(data.postArray[2], data.postArray[3]), null));
            items.add(new TypeItem(new PostList(data.postList), null));
        }
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
        recyclerView.setAdapter(new MultiTypeAdapter(items));
        // @formatter:on
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }
}
