package com.yahia.healthysiabires.partage.view.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import androidx.annotation.CallSuper;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.yahia.healthysiabires.R;import com.yahia.healthysiabires.partage.event.ets;
import com.yahia.healthysiabires.partage.event.fichier.fichierProvidedEt;
import com.yahia.healthysiabires.partage.event.fichier.fichierProvidedFailedEt;
import com.yahia.healthysiabires.partage.event.permision.PermisionRequestEt;
import com.yahia.healthysiabires.partage.event.permision.PermisionResponseEt;
import com.yahia.healthysiabires.partage.SystemUs;
import com.yahia.healthysiabires.partage.view.ViewUs;
import com.yahia.healthysiabires.partage.data.premier.Victor2D;
import com.yahia.healthysiabires.partage.data.permission.Permission;
import com.yahia.healthysiabires.partage.data.permission.PermissionMn;
import com.yahia.healthysiabires.partage.data.permission.PermissionUC;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {

    private static final String TAG = BaseActivity.class.getSimpleName();

    public static final int REQUEST_CODE_BACKUP_IMPORT = 25151;

    static final String ARGUMENT_REVEAL_X = "revealX";
    static final String ARGUMENT_REVEAL_Y = "revealY";

    protected static <T extends BaseActivity> Intent getIntent(Class<T> clazz, Context context, @Nullable View source) {
        Intent intent = new Intent(context, clazz);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && source != null) {
            Victor2D position = ViewUs.getPositionOnScreen(source);
            intent.putExtra(BaseActivity.ARGUMENT_REVEAL_X, position.x + (source.getWidth() / 2));
            intent.putExtra(BaseActivity.ARGUMENT_REVEAL_Y, position.y + (source.getHeight() / 2));
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        }
        return intent;
    }

    @BindView(R.id.toolbar) protected Toolbar toolbar;
    @BindView(R.id.toolbar_title) protected TextView toolbarTitleView;
    @BindView(R.id.root) @Nullable protected ViewGroup rootLayout;

    private int layoutResourceId;
    private int revealX;
    private int revealY;

    private BaseActivity() {
        // Forbidden
    }

    public BaseActivity(@LayoutRes int layoutResourceId) {
        this();
        this.layoutResourceId = layoutResourceId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutResourceId);
        ButterKnife.bind(this);

        init();

        if (savedInstanceState == null) {
            reveal();
        } else {
            onViewShown();
        }
    }

    /**
     * Called after the activity is created and its view fully revealed
     */
    @CallSuper
    protected void onViewShown() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        ets.register(this);
    }

    @Override
    protected void onPause() {
        ets.unregister(this);
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] codes, @NonNull int[] grantResults) {
        PermissionUC useCase = PermissionUC.fromRequestCode(requestCode);
        if (useCase != null) {
            for (String code : codes) {
                Permission permission = Permission.fromCode(code);
                if (permission != null) {
                    boolean isGranted = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    ets.post(new PermisionResponseEt(permission, useCase, isGranted));
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_BACKUP_IMPORT:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        if (data != null && data.getData() != null) {
                            ets.post(new fichierProvidedEt(data.getData()));
                        } else {
                            ets.post(new fichierProvidedFailedEt());
                        }
                        break;
                    default:
                        // Ignore
                        break;
                }
                break;
            default:
                Log.d(TAG, "Ignoring unknown result with request code" + requestCode);
                break;
        }
    }

    @Override
    public void finish() {
        unreveal();
    }

    @Nullable
    public TextView getTitleView() {
        return toolbarTitleView;
    }

    @Override
    public void setTitle(CharSequence title) {
        if (toolbarTitleView != null) {
            toolbarTitleView.setText(title);
        } else {
            super.setTitle(title);
        }
    }

    @Override
    public void setTitle(int titleId) {
        setTitle(getString(titleId));
    }

    private void init() {
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        setTitle(SystemUs.getLabelForActivity(this));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getWindow().getDecorView().setImportantForAutofill(View.IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS);
        }
    }

    private void reveal() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            revealX = getIntent().getIntExtra(ARGUMENT_REVEAL_X, -1);
            revealY = getIntent().getIntExtra(ARGUMENT_REVEAL_Y, -1);
            if (rootLayout != null && revealX >= 0 && revealY >= 0) {
                rootLayout.setVisibility(View.INVISIBLE);
                ViewTreeObserver viewTreeObserver = rootLayout.getViewTreeObserver();
                if (viewTreeObserver.isAlive()) {
                    viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                        @Override
                        public void onGlobalLayout() {
                            rootLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                            rootLayout.setVisibility(View.VISIBLE);
                            ViewUs.reveal(rootLayout, revealX, revealY, true, new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    onViewShown();
                                }
                            });
                        }
                    });
                }
            } else {
                onViewShown();
            }
        } else {
            onViewShown();
        }
    }

    private void unreveal() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && rootLayout != null && revealX >= 0 && revealY >= 0) {
            ViewUs.reveal(rootLayout, revealX, revealY, false, new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    rootLayout.setVisibility(View.INVISIBLE);
                    BaseActivity.super.finish();
                    overridePendingTransition(0, 0);
                }
            });
        } else {
            super.finish();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PermisionRequestEt event) {
        if (PermissionMn.getInstance().hasPermission(this, event.context)) {
            ets.post(new PermisionResponseEt(event.context, event.useCase, true));
        } else {
            PermissionMn.getInstance().requestPermission(this, event.context, event.useCase);
        }
    }
}