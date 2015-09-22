package ovh.vii.logmyday.views;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import ovh.vii.logmyday.R;
import ovh.vii.logmyday.data.Field;

/**
 * Created by vigliag on 9/22/15.
 */
public class ValueFieldView extends LinearLayout implements DiscreteSeekBar.OnProgressChangeListener {

    private final DiscreteSeekBar dsb;
    private final TextView txt;

    public ValueFieldView(Context context, Field f) {
        super(context);

        View view = LayoutInflater.from(context).inflate(R.layout.field_value, this);

        dsb = (DiscreteSeekBar) findViewById(R.id.seekBar);
        txt = (TextView) findViewById(R.id.valueText);
        dsb.setOnProgressChangeListener(this);

        dsb.setMax(f.getMaxvalue());
        dsb.setMin(f.getMinvalue());

    }

    public void setValue(int value){
        dsb.setProgress(value);
        txt.setText(String.valueOf(value));
    }

    @Override
    public void onProgressChanged(DiscreteSeekBar discreteSeekBar, int value, boolean fromUser) {
        txt.setText(String.valueOf(value));
    }

    @Override
    public void onStartTrackingTouch(DiscreteSeekBar discreteSeekBar) {

    }

    @Override
    public void onStopTrackingTouch(DiscreteSeekBar discreteSeekBar) {

    }

    public int getValue() {
        return dsb.getProgress();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("instanceState", super.onSaveInstanceState());
        bundle.putInt("stateToSave", getValue());
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);

        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            setValue(bundle.getInt("stateToSave"));
            state = bundle.getParcelable("instanceState");
        }

        super.onRestoreInstanceState(state);
    }
}
