package com.aidilab.ble.interfaces;

import java.text.DecimalFormat;

import com.aidilab.ble.DeviceActivity;
import com.aidilab.ble.fragment.FizzlyViewFragment;

import android.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public abstract class FizzlyFragment extends Fragment implements OnClickListener{

private static final String TAG = "FizzlyViewFragment";
	
	public static FizzlyViewFragment mInstance = null;
	
	// Views elements
	private TextView mStatus;
	
	// House-keeping
	private DecimalFormat decimal = new DecimalFormat("+0.00;-0.00");
	private DeviceActivity mActivity;
	
	@Override
	public void onClick(View arg0) {
		
	}
	
	public abstract void metodo();

}
