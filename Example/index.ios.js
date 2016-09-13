/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React, {Component} from 'react';
import {
    AppRegistry,
    StyleSheet,
    Text,
    View,
    Navigator,
    TouchableOpacity
} from 'react-native';

import LiveView from './live'

class MainPage extends Component {

  constructor(props) {
    super(props);
  }

  goVideoLive() {
    let navigator = this.props.navigator;

    if (navigator) {
      navigator.push({
        name: 'LiveView',
        component: LiveView
      });
    }
  }

  render() {
    return (
        <View style={styles.container}>
          <TouchableOpacity style={{margin:20,backgroundColor:'blue'}} onPress={this.goVideoLive.bind(this)}>
            <Text>退出 直播</Text>
          </TouchableOpacity>
        </View>
    );
  }
}

class Example extends Component {

  renderScene(route, navigator) {
    return <route.component navigator={navigator}  {...route.passProps} />;
  }

  render() {
    return (
        <Navigator
            style={{flex:1}}
            initialRoute={{component: MainPage}}
            renderScene={this.renderScene}/>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  live: {
    position: 'absolute', top: 0, left: 0, right: 0, bottom: 0
  },

  welcome: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  },
  instructions: {
    textAlign: 'center',
    color: '#333333',
    marginBottom: 5,
  }
});

AppRegistry.registerComponent('Example', () => Example);
