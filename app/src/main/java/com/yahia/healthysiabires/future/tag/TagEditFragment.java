package com.yahia.healthysiabires.future.tag;

import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.yahia.healthysiabires.R;import com.yahia.healthysiabires.partage.data.synchronisation.DataLoader;
import com.yahia.healthysiabires.partage.data.synchronisation.DataLoaderListener;
import com.yahia.healthysiabires.partage.data.database.ydk.Tagydk;
import com.yahia.healthysiabires.partage.data.database.entity.Tag;
import com.yahia.healthysiabires.partage.view.fragment.BaseDialogFragment;
import com.yahia.healthysiabires.partage.view.argument.DialogButton;
import com.yahia.healthysiabires.partage.data.premier.StringUs;
import com.yahia.healthysiabires.partage.view.ViewUs;

import butterknife.BindView;

public class TagEditFragment extends BaseDialogFragment {

    @BindView(R.id.input) EditText editText;

    private TagListener listener;

    public TagEditFragment() {
        super(R.string.tag_new, R.layout.dialog_input);
    }

    @Override
    public void onStart() {
        super.onStart();
        ViewUs.showKeyboard(editText);
    }

    public void setListener(TagListener listener) {
        this.listener = listener;
    }

    private void trySubmit() {
        final String name = editText.getText().toString();
        DataLoader.getInstance().load(getContext(), new DataLoaderListener<TagResult>() {
            @Override
            public TagResult onShouldLoad() {
                Tag tag = null;
                TagError error = findError(name);
                if (error == null) {
                    tag = createTag(name);
                }
                return new TagResult(tag, error);
            }
            @Override
            public void onDidLoad(TagResult result) {
                if (listener != null) {
                    listener.onResult(result.tag);
                }
                if (result.tag != null) {
                    ViewUs.hideKeyboard(editText);
                    dismiss();
                } else {
                    editText.setError(getString(result.error != null ? result.error.textResId : R.string.error_unexpected));
                }
            }
        });
    }

    @Nullable
    private TagError findError(String name) {
        if (StringUs.isBlank(name)) {
            return TagError.EMPTY;
        } else if (Tagydk.getInstance().getByName(name) != null) {
            return TagError.DUPLICATE;
        } else {
            return null;
        }
    }

    private Tag createTag(String name) {
        Tag tag = new Tag();
        tag.setName(name);
        Tagydk.getInstance().createOrUpdate(tag);
        return tag;
    }

    @Nullable
    @Override
    protected DialogButton createNegativeButton() {
        return new DialogButton(android.R.string.cancel, () -> {
            ViewUs.hideKeyboard(editText);
            dismiss();
        });
    }

    @Nullable
    @Override
    protected DialogButton createPositiveButton() {
        return new DialogButton(android.R.string.ok, this::trySubmit);
    }

    private enum TagError {
        EMPTY(R.string.validator_value_empty),
        DUPLICATE(R.string.tag_duplicate);

        @StringRes private int textResId;

        TagError(@StringRes int textResId) {
            this.textResId = textResId;
        }
    }

    private class TagResult {
        @Nullable private Tag tag;
        @Nullable private TagError error;

        private TagResult(@Nullable Tag tag, @Nullable TagError error) {
            this.tag = tag;
            this.error = error;
        }
    }

    public interface TagListener {
        void onResult(@Nullable Tag tag);
    }
}
