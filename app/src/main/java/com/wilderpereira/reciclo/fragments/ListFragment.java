package com.wilderpereira.reciclo.fragments;

import com.firebase.ui.database.FirebaseRecyclerAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.wilderpereira.reciclo.activities.RecipeActivity;
import com.wilderpereira.reciclo.models.Recipe;
import com.wilderpereira.reciclo.models.Resource;
import com.wilderpereira.reciclo.R;
import com.wilderpereira.reciclo.viewholder.ItemViewHolder;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Wilder on 10/07/16.
 */
public class ListFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private DatabaseReference mDatabase;
    private FirebaseRecyclerAdapter<Recipe, ItemViewHolder> adapter;

    @ListFragment.ListMode
    private int listMode = LIST_MODE_DEFAULT;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef( {LIST_MODE_FAVORITES, LIST_MODE_DEFAULT})
    public @interface ListMode{}

    public static final int LIST_MODE_DEFAULT = 0;
    public static final int LIST_MODE_FAVORITES = 1;

    public static final ListFragment newInstance(@ListMode int listMode, View view)
    {
        ListFragment fragment = new ListFragment();
        Bundle bundle = new Bundle(1);
        bundle.putInt(view.getContext().getString(R.string.LIST_MODE_KEY), listMode);
        fragment.setArguments(bundle);
        return fragment ;
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.recycle_fragment, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        if(getArguments().getInt(getString(R.string.LIST_MODE_KEY)) == LIST_MODE_DEFAULT){
            this.listMode = LIST_MODE_DEFAULT;
        }else{
            this.listMode = LIST_MODE_FAVORITES;
        }

        final Boolean isFavorite = listMode == LIST_MODE_FAVORITES;

        Query postsQuery = mDatabase.child("recipes");
        adapter = new FirebaseRecyclerAdapter<Recipe, ItemViewHolder>(Recipe.class, R.layout.item_item,
                ItemViewHolder.class, postsQuery) {
            @Override
            protected void populateViewHolder(final ItemViewHolder viewHolder, final Recipe model, final int position) {
                final DatabaseReference recipeRef = getRef(position);

                if(isFavorite){
                    viewHolder.imgStar.setImageResource(R.drawable.circle);
                }

                viewHolder.imgStar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(isFavorite){
                            /**
                             * Removes from user favorites and
                             * decreases the recipe favorites count.
                             * */
                            Map<String, Object> favoriteCount = new HashMap<String, Object>();
                            favoriteCount.put("favoriteCount", model.getFavoriteCount()-1);
                            recipeRef.updateChildren(favoriteCount);
                            //TODO: Add to favorites
                        }else{
                            /**
                             * Adds the recipe to user favorites and
                             * increases is favorites count.
                             * */
                            Map<String, Object> favoriteCount = new HashMap<String, Object>();
                            favoriteCount.put("favoriteCount", model.getFavoriteCount()+1);
                            recipeRef.updateChildren(favoriteCount);
                            //TODO: Add to favorites
                        }
                    }
                });

                // Set click listener for the whole post view
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Launch RecipeActivity
                        Intent intent = new Intent(v.getContext(),RecipeActivity.class);
                        intent.putExtra(getString(R.string.recipe_extra_key), model);
                        startActivity(intent);
                    }
                });

                // Bind Post to ViewHolder, setting OnClickListener for the star button
                viewHolder.bindToPost(model, new View.OnClickListener() {
                    @Override
                    public void onClick(View starView) {

                    }
                });
            }
        };




        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }


}
