package com.aidilab.ble.interfaces;

import java.text.DecimalFormat;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aidilab.ble.R;
import com.aidilab.ble.fragment.FizzlyViewFragment;
import com.aidilab.ble.utils.SensorsValues;

public abstract class FizzlyFragment extends Fragment implements OnClickListener{

	public static FizzlyFragment mInstance = null;
	
	// Views elements
	
	// House-keeping
	private DecimalFormat decimal = new DecimalFormat("+0.00;-0.00");
	private FizzlyActivity mActivity;
	
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//	    mInstance = this;
//	    mActivity = (FizzlyActivity) getActivity();
//       
//	    View view = inflater.inflate(R.layout.fragment_device, container, false);
//	    	
//	    /**   GUI initialization - getting gui references */
//		
//	    mActivity.onViewInflated(view);
//	
//	    return view;
//	}
		
	public abstract void onCharatonCharacteristicChanged(String uuidStr, byte[] rawValue);
	public abstract void onCharatonCharacteristicChanged(String uuidStr, SensorsValues sv);
	public abstract void onGestureDetected(int gestureId);

}
