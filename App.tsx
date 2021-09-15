/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * Generated with the TypeScript template
 * https://github.com/react-native-community/react-native-template-typescript
 *
 * @format
 */

import React, {useEffect, useState} from 'react';
import {
  SafeAreaView,
  ScrollView,
  StatusBar,
  StyleSheet,
  Text,
  View,
  NativeModules,
  DeviceEventEmitter,
} from 'react-native';

import {
  Colors,
  Header,
  LearnMoreLinks,
} from 'react-native/Libraries/NewAppScreen';

const App = () => {
  const [hasProximity, setHasProximity] = useState(false);

  const backgroundStyle = {
    backgroundColor: Colors.lighter,
  };

  const {ProximitySensorModule} = NativeModules;

  function addListener(callback: any) {
    console.log({ProximitySensorModule, NativeModules});
    ProximitySensorModule.addListener();
    DeviceEventEmitter.addListener(
      ProximitySensorModule.EVENT_ON_SENSOR_CHANGE,
      e => callback(e),
    );
  }

  useEffect(() => {
    const callback = ({proximity}: any) => setHasProximity(!!proximity);
    addListener(callback);
  }, []);

  console.log({hasProximity});

  return (
    <SafeAreaView style={backgroundStyle}>
      <StatusBar barStyle={'dark-content'} />
      <ScrollView
        contentInsetAdjustmentBehavior="automatic"
        style={backgroundStyle}>
        <Header />
        <View
          style={{
            backgroundColor: Colors.white,
          }}>
          <Text style={styles.highlight}>App.tsx</Text>
          <LearnMoreLinks />
        </View>
      </ScrollView>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  sectionContainer: {
    marginTop: 32,
    paddingHorizontal: 24,
  },
  sectionTitle: {
    fontSize: 24,
    fontWeight: '600',
  },
  sectionDescription: {
    marginTop: 8,
    fontSize: 18,
    fontWeight: '400',
  },
  highlight: {
    fontWeight: '700',
  },
});

export default App;
