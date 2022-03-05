package ru.db.app;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

public class country_edit extends androidx.appcompat.widget.AppCompatAutoCompleteTextView {

    public country_edit(Context context) {
        super(context);
    }

    public country_edit(Context arg0, AttributeSet arg1) {
        super(arg0, arg1);
    }

    public country_edit(Context arg0, AttributeSet arg1, int arg2) {
        super(arg0, arg1, arg2);
    }

    @Override
    public boolean enoughToFilter() {
        return true;
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction,
                                  Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if (focused && getFilter()!=null) {
            performFiltering(getText(), 0);
        }
    }

}