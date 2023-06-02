package com.mydiabetesprt.diabetes.future.export.histoire;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.InterstitialAd;
import com.mydiabetesprt.diabetes.R;import com.mydiabetesprt.diabetes.partage.event.ets;
import com.mydiabetesprt.diabetes.partage.event.fichier.ExportHistoireDeleteEt;
import com.mydiabetesprt.diabetes.partage.event.permision.PermisionRequestEt;
import com.mydiabetesprt.diabetes.partage.event.permision.PermisionResponseEt;
import com.mydiabetesprt.diabetes.future.export.job.Export;
import com.mydiabetesprt.diabetes.partage.view.fragment.BaseFragment;
import com.mydiabetesprt.diabetes.partage.view.recyclerview.decorations.LiniarDividerItemDecoration;
import com.mydiabetesprt.diabetes.partage.data.file.FileUs;
import com.mydiabetesprt.diabetes.partage.data.permission.Permission;
import com.mydiabetesprt.diabetes.partage.data.permission.PermissionUC;
import com.mydiabetesprt.diabetes.partage.data.synchronisation.BasesynchronisationTask;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;

public class ExportHistoryFragment extends BaseFragment {
    Boolean y=true;
    private InterstitialAd mInterstitialAd;   private InterstitialAd mInterstitialAd2;
    private InterstitialAd mInterstitialAd3;
    private InterstitialAd mInterstitialAd4;
    @BindView(R.id.list) RecyclerView listView;
    @BindView(R.id.progressView) View progressView;

    private ExportHistoryListAdapter listAdapter;

    public ExportHistoryFragment() {
        super(R.layout.fragment_export_history, R.string.export_history);



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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ets.register(this);
        initLayout();
        checkPermissions();
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onBackButtonPressed() {

    }

    @Override
    public void onDestroy() {
        ets.unregister(this);
        super.onDestroy();
    }

    private void init() {
        listAdapter = new ExportHistoryListAdapter(getContext());
    }

    private void initLayout() {
        listView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        listView.addItemDecoration(new LiniarDividerItemDecoration(getContext()));
        listView.setAdapter(listAdapter);
        progressView.setVisibility(View.VISIBLE);
    }

    private void checkPermissions() {
        ets.post(
            new PermisionRequestEt(
                Permission.WRITE_EXTERNAL_STORAGE,
                PermissionUC.EXPORT_HISTORY
            )
        );
    }

    private void fetchHistory() {
        new FetchHistoryTask(getContext(), this::setHistory).execute();
    }

    private void setHistory(List<ExportHistoryListItem> listItems) {
        listAdapter.clear();
        listAdapter.addItems(listItems);
        listAdapter.notifyDataSetChanged();
        progressView.setVisibility(View.GONE);
    }

    private void deleteExportIfConfirmed(ExportHistoryListItem item) {
        if (getContext() != null) {
            new AlertDialog.Builder(getContext())
                .setTitle(R.string.export_delete)
                .setMessage(R.string.export_delete_desc)
                .setNegativeButton(R.string.cancel, (dialog, which) -> { })
                .setPositiveButton(R.string.delete, (dialog, which) -> deleteExport(item))
                .create()
                .show();
        }
    }

    private void deleteExport(ExportHistoryListItem item) {
        // Runtime permission should be granted here
        FileUs.deleteFile(item.getFile());

        int position = listAdapter.getItemPosition(item);
        listAdapter.removeItem(position);
        listAdapter.notifyItemRemoved(position);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PermisionResponseEt event) {
        if (event.context == Permission.WRITE_EXTERNAL_STORAGE &&
            event.useCase == PermissionUC.EXPORT_HISTORY &&
            event.isGranted
        ) {
            fetchHistory();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ExportHistoireDeleteEt event) {
        deleteExportIfConfirmed(event.context);
    }

    private static class FetchHistoryTask extends BasesynchronisationTask<Void, Void, List<ExportHistoryListItem>> {

        FetchHistoryTask(Context context, OnAsyncProgressListener<List<ExportHistoryListItem>> onAsyncProgressListener) {
            super(context, onAsyncProgressListener);
        }

        @Override
        protected List<ExportHistoryListItem> doInBackground(Void... voids) {
            File directory = FileUs.getPublicDirectory(getContext());
            File[] files = directory.listFiles();

            List<ExportHistoryListItem> listItems = new ArrayList<>();
            if (files != null) {
                for (File file : files) {
                    ExportHistoryListItem listItem = Export.getExportItem(file);
                    if (listItem != null) {
                        listItems.add(Export.getExportItem(file));
                    }
                }
            }

            Collections.sort(listItems, (first, second) ->
                second.getCreatedAt().compareTo(first.getCreatedAt())
            );

            return listItems;
        }
    }
}
