package com.yahia.healthysiabires.future.tag;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

   
import com.yahia.healthysiabires.R;import com.yahia.healthysiabires.partage.data.synchronisation.DataLoader;
import com.yahia.healthysiabires.partage.data.synchronisation.DataLoaderListener;
import com.yahia.healthysiabires.partage.data.database.ydk.EntryTagDao;
import com.yahia.healthysiabires.partage.data.database.ydk.Tagydk;
import com.yahia.healthysiabires.partage.data.database.entity.EntryTag;
import com.yahia.healthysiabires.partage.data.database.entity.Tag;
import com.yahia.healthysiabires.partage.view.fragment.BaseFragment;
import com.yahia.healthysiabires.partage.view.recyclerview.decorations.LiniarDividerItemDecoration;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class TagListFragment extends BaseFragment implements TagListAdapter.TagListener {

    Boolean y=true;
     
    @BindView(R.id.list) RecyclerView list;
    @BindView(R.id.list_placeholder) View placeholder;

    private TagListAdapter listAdapter;

    public TagListFragment() {
        super(R.layout.fragment_tags, R.string.tags);


       //        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
//            @Override
//            public void onInitializationComplete(InitializationStatus initializationStatus) {
//            }
//        });
//         mInterstitialAd = new InterstitialAd(getContext());
//        mInterstitialAd.setAdUnitId("ca-app-pub-3808047780782051/4043570060");
//        mInterstitialAd.loadAd(new AdRequest.Builder().build());
//        mInterstitialAd.setAdListener(new AdListener() {
//            @Override
//            public void onAdClosed() {
//                super.onAdClosed();
//                finish();
//            }
//
//        });
//
//        }
//
//    @Override
//    public void onBackPressed() {
//        showIntewrestial();
//
//    }
//    @Override
//    public void onBackButtonPressed() {
//        if (y=true) {
//            showIntewrestial();
//        } else {
//            getActivity().onBackPressed();
//        }
//    }
//
//    public  void showIntewrestial(){
//        if(mInterstitialAd.isLoaded()){
//            mInterstitialAd.show();
//        }else {finish();
//        }

    }




    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initLayout();
        invalidateLayout();
        loadTags();


    }

  

    private void initLayout() {
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        list.addItemDecoration(new LiniarDividerItemDecoration(getContext()));
        listAdapter = new TagListAdapter(getContext());
        listAdapter.setTagListener(this);
        list.setAdapter(listAdapter);
    }

    private void invalidateLayout() {
        boolean isEmpty = listAdapter == null || listAdapter.getItemCount() == 0;
        placeholder.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
    }

    private void setTags(List<Tag> tags) {
        listAdapter.clear();
        listAdapter.addItems(tags);
        listAdapter.notifyDataSetChanged();
        invalidateLayout();
    }

    private void loadTags() {
        DataLoader.getInstance().load(getContext(), new DataLoaderListener<List<Tag>>() {
            @Override
            public List<Tag> onShouldLoad() {
                return Tagydk.getInstance().getAll();
            }
            @Override
            public void onDidLoad(List<Tag> data) {
                setTags(data);
            }
        });
    }

    private void addTag(Tag tag) {
        int position = 0;
        list.scrollToPosition(position);
        listAdapter.addItem(position, tag);
        listAdapter.notifyItemInserted(position);
    }

    private void removeTag(Tag tag) {
        int position = listAdapter.getItemPosition(tag);
        if (position >= 0) {
            listAdapter.removeItem(position);
            listAdapter.notifyItemRemoved(position);
        }
    }

    private void confirmTagDeletion(final Tag tag) {
        DataLoader.getInstance().load(getContext(), new DataLoaderListener<Long>() {
            @Override
            public Long onShouldLoad() {
                return EntryTagDao.getInstance().count(tag);
            }
            @Override
            public void onDidLoad(Long data) {
                new AlertDialog.Builder(getContext())
                        .setTitle(R.string.tag_delete)
                        .setMessage(String.format(getString(R.string.tag_delete_confirmation), data))
                        .setNegativeButton(R.string.cancel, (dialog, which) -> { })
                        .setPositiveButton(R.string.ok, (dialog, which) -> deleteTag(tag))
                        .create()
                        .show();
            }
        });
    }

    private void deleteTag(final Tag tag) {
        DataLoader.getInstance().load(getContext(), new DataLoaderListener<Tag>() {
            @Override
            public Tag onShouldLoad() {
                List<EntryTag> entryTags = EntryTagDao.getInstance().getAll(tag);
                EntryTagDao.getInstance().delete(entryTags);
                int result = Tagydk.getInstance().delete(tag);
                return result > 0 ? tag : null;
            }
            @Override
            public void onDidLoad(@Nullable Tag result) {
                if (result != null) {
                    removeTag(result);
                }
            }
        });
    }

    private void createTag() {
        if (getFragmentManager() != null) {
            TagEditFragment fragment = new TagEditFragment();
            fragment.setListener(result -> {
                if (result != null) {
                    addTag(result);
                }
            });
            fragment.show(getFragmentManager(), null);
        }
    }

    @Override
    public void onTagDeleted(Tag tag, View view) {
        confirmTagDeletion(tag);
    }

    @OnClick(R.id.fab)
    void onFabClick() {
        createTag();
    }
}
