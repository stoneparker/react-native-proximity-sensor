package com.prevent_screen;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactContext;  

import java.util.HashMap;
import java.util.Map;

public class ProximitySensorModule extends ReactContextBaseJavaModule implements SensorEventListener {

  private static final String TAG = "ProximitySensorModule";
  private static final String KEY_PROXIMITY = "proximity";
  private static final String KEY_DISTANCE = "distance";
  private static final String KEY_EVENT_ON_SENSOR_CHANGE = "EVENT_ON_SENSOR_CHANGE";
  private static final String EVENT_ON_SENSOR_CHANGE = "onSensorChanged";
  private final ReactApplicationContext reactContext;

  private SensorManager mSensorManager;
  private Sensor mProximity;

  public ProximitySensorModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
    mSensorManager = (SensorManager) reactContext.getSystemService(Context.SENSOR_SERVICE);
    mProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY, true);
  }

  public void sendEvent(String eventName, WritableMap params) {
    if (this.reactContext.hasActiveCatalystInstance()) {
      this.reactContext
              .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
              .emit(eventName, params);
    } else {
      Log.i(TAG, "Waiting for CatalystInstance");
    }
  }

  @ReactMethod
  public void addListener() {
    mSensorManager.registerListener(this, mProximity, SensorManager.SENSOR_DELAY_NORMAL);
  }

  @ReactMethod
  public void removeListener() {
    mSensorManager.unregisterListener(this);
  }

  @Override
  public String getName() {
    return "ProximitySensorModule";
  }

  @Override
  public Map<String, Object> getConstants() {
    final Map<String, Object> constants = new HashMap<>();
    constants.put(KEY_EVENT_ON_SENSOR_CHANGE, EVENT_ON_SENSOR_CHANGE);
    return constants;
  }

  @Override
  public void onSensorChanged(SensorEvent sensorEvent) {
    WritableMap params = Arguments.createMap();

    double distance = sensorEvent.values[0];
    double maximumRange = mProximity.getMaximumRange();
    boolean isNearDevice = distance < maximumRange;

    params.putBoolean(KEY_PROXIMITY, isNearDevice);
    params.putDouble(KEY_DISTANCE, distance);

    sendEvent(EVENT_ON_SENSOR_CHANGE, params);
  }

  @Override
  public void onAccuracyChanged(Sensor sensor, int i) {
    System.out.println("AccurancyChanged!!!!!");
  }
}